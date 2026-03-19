package com.pronnect.auth.security;

import com.pronnect.account.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public Account getCurrentAccount() {

        AccountUserDetails userDetails =
                (AccountUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getAccount();
    }
}