package com.pronnect.account.validation;

public class CpfCnpjValidator {

    private static final int[] WEIGHT_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_CNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int digit1 = calculateDigit(cpf.substring(0, 9), WEIGHT_CPF);
        int digit2 = calculateDigit(cpf.substring(0, 9) + digit1, WEIGHT_CPF);

        return cpf.equals(cpf.substring(0, 9) + digit1 + digit2);
    }

    public static boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int digit1 = calculateDigit(cnpj.substring(0, 12), WEIGHT_CNPJ);
        int digit2 = calculateDigit(cnpj.substring(0, 12) + digit1, WEIGHT_CNPJ);

        return cnpj.equals(cnpj.substring(0, 12) + digit1 + digit2);
    }

    private static int calculateDigit(String str, int[] weight) {
        int sum = 0;
        for (int i = str.length() - 1, digit; i >= 0; i--) {
            digit = Integer.parseInt(str.substring(i, i + 1));
            sum += digit * weight[weight.length - str.length() + i];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }
}
