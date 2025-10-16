package com.banking.graphql.resolver;

import com.banking.graphql.model.Account;
import com.banking.graphql.model.AccountStatus;
import com.banking.graphql.model.AccountType;
import com.banking.graphql.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AccountResolver {

    private final AccountService accountService;

    @QueryMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @QueryMapping
    public Account getAccountById(@Argument Long id) {
        return accountService.getAccountById(id);
    }

    @QueryMapping
    public Account getAccountByNumber(@Argument String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    @QueryMapping
    public List<Account> getAccountsByCustomerId(@Argument Long customerId) {
        return accountService.getAccountsByCustomerId(customerId);
    }

    @QueryMapping
    public Double getBalance(@Argument String accountNumber) {
        return accountService.getBalance(accountNumber).doubleValue();
    }

    @MutationMapping
    public Account createAccount(@Argument Map<String, Object> input) {
        Long customerId = Long.parseLong(input.get("customerId").toString());
        AccountType accountType = AccountType.valueOf((String) input.get("accountType"));
        BigDecimal initialDeposit = BigDecimal.valueOf(
            ((Number) input.get("initialDeposit")).doubleValue()
        );
        
        return accountService.createAccount(customerId, accountType, initialDeposit);
    }

    @MutationMapping
    public Account updateAccountStatus(@Argument String accountNumber, 
                                       @Argument AccountStatus status) {
        return accountService.updateAccountStatus(accountNumber, status);
    }

    @MutationMapping
    public Boolean closeAccount(@Argument String accountNumber) {
        return accountService.closeAccount(accountNumber);
    }
}
