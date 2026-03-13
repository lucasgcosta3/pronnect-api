package com.pronnect.account.mapper;

import com.pronnect.account.Account;
import com.pronnect.account.dto.AccountResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);

}
