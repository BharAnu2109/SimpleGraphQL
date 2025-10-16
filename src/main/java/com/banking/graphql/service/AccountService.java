package com.banking.graphql.service;

import com.banking.graphql.exception.AccountNotFoundException;
import com.banking.graphql.exception.BankingException;
import com.banking.graphql.exception.CustomerNotFoundException;
import com.banking.graphql.model.Account;
import com.banking.graphql.model.AccountStatus;
import com.banking.graphql.model.AccountType;
import com.banking.graphql.model.Customer;
import com.banking.graphql.repository.AccountRepository;
import com.banking.graphql.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Account createAccount(Long customerId, AccountType accountType, BigDecimal initialDeposit) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankingException("Initial deposit cannot be negative");
        }

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, accountType, initialDeposit, 
                                      customer, AccountStatus.ACTIVE);
        
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        return account.getBalance();
    }

    @Transactional
    public Account updateAccountStatus(String accountNumber, AccountStatus status) {
        Account account = getAccountByNumber(accountNumber);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    @Transactional
    public boolean closeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BankingException("Cannot close account with non-zero balance");
        }
        
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        return true;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%010d", random.nextInt(1000000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
