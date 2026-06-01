package com.pronnect.payment.service;

import com.pronnect.account.entity.Account;
import com.pronnect.auth.security.AuthenticatedUserService;
import com.pronnect.exception.BusinessException;
import com.pronnect.exception.ForbiddenException;
import com.pronnect.exception.NotFoundException;
import com.pronnect.payment.entity.Payment;
import com.pronnect.payment.enums.PaymentStatus;
import com.pronnect.payment.gateway.PaymentGateway;
import com.pronnect.payment.gateway.PixPaymentData;
import com.pronnect.payment.repository.PaymentRepository;
import com.pronnect.servicecontract.entity.ServiceContract;
import com.pronnect.servicecontract.enums.ServiceContractStatus;
import com.pronnect.servicecontract.repository.ServiceContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    /** Pronnect charges 10% ON TOP of the proposal price */
    private static final BigDecimal PLATFORM_FEE_PERCENT = new BigDecimal("0.10");

    private final PaymentRepository paymentRepository;
    private final ServiceContractRepository serviceContractRepository;
    private final AuthenticatedUserService auth;
    private final PaymentGateway paymentGateway;

    @Transactional
    public Payment createPayment(UUID serviceContractId) {
        Account account = auth.getCurrentAccount();

        ServiceContract contract = serviceContractRepository.findById(serviceContractId)
                .orElseThrow(() -> new NotFoundException("Service contract not found"));

        if (!contract.getProposal().getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the company can make a payment");
        }

        if (contract.getStatus() != ServiceContractStatus.IN_PROGRESS) {
            throw new BusinessException("Payment can only be made for contracts in progress");
        }

        boolean alreadyPaid = paymentRepository.findByServiceContractId(contract.getId()).isPresent();
        if (alreadyPaid) {
            throw new BusinessException("Payment already exists for this contract");
        }

        BigDecimal proposalPrice = contract.getProposal().getPrice();
        BigDecimal platformFee = proposalPrice.multiply(PLATFORM_FEE_PERCENT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCharged = proposalPrice.add(platformFee);

        PixPaymentData pixData = paymentGateway.createPixPayment(
                totalCharged,
                "Pagamento para contrato " + contract.getId(),
                account.getEmail()
        );

        Payment payment = new Payment();
        payment.setServiceContract(contract);
        payment.setAmount(totalCharged);
        payment.setPlatformFee(platformFee);
        payment.setProfessionalAmount(proposalPrice);
        payment.setStatus(PaymentStatus.PENDING);
        
        payment.setExternalPaymentId(pixData.externalPaymentId());
        payment.setPaymentMethod("pix");
        payment.setPixQrCode(pixData.pixQrCode());
        payment.setPixCopyPaste(pixData.pixCopyPaste());
        payment.setExpiresAt(pixData.expiresAt());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment simulateApproval(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Payment must be PENDING to be approved");
        }

        payment.setStatus(PaymentStatus.HELD);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment cancel(UUID paymentId) {
        Account account = auth.getCurrentAccount();
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (!payment.getServiceContract().getProposal().getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the company can cancel the payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Only PENDING payments can be cancelled");
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment release(UUID serviceContractId) {
        Account account = auth.getCurrentAccount();

        ServiceContract contract = serviceContractRepository.findById(serviceContractId)
                .orElseThrow(() -> new NotFoundException("Service contract not found"));

        if (!contract.getProposal().getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the company can release the payment");
        }

        if (contract.getStatus() != ServiceContractStatus.VALIDATED) {
            throw new BusinessException("Service must be validated before releasing payment");
        }

        Payment payment = paymentRepository.findByServiceContractId(serviceContractId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.HELD) {
            throw new BusinessException("Payment must be HELD to be released");
        }

        payment.setStatus(PaymentStatus.RELEASED);
        payment.setReleasedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment refund(UUID serviceContractId) {
        Account account = auth.getCurrentAccount();

        ServiceContract contract = serviceContractRepository.findById(serviceContractId)
                .orElseThrow(() -> new NotFoundException("Service contract not found"));

        if (!contract.getProposal().getCompany().getAccount().getId().equals(account.getId())) {
            throw new ForbiddenException("Only the company can refund the payment");
        }

        Payment payment = paymentRepository.findByServiceContractId(serviceContractId)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.HELD) {
            throw new BusinessException("Payment must be HELD to be refunded");
        }

        paymentGateway.refundPayment(payment.getExternalPaymentId());

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}