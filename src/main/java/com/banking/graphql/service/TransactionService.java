package com.banking.graphql.service;

import com.banking.graphql.exception.AccountNotFoundException;
import com.banking.graphql.exception.BankingException;
import com.banking.graphql.exception.InsufficientBalanceException;
import com.banking.graphql.model.*;
import com.banking.graphql.repository.AccountRepository;
import com.banking.graphql.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BankingException("Account is not active");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, TransactionType.DEPOSIT, 
                                                   amount, description, account, account.getBalance());
        
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BankingException("Account is not active");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: " + account.getBalance() + ", Required: " + amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, TransactionType.WITHDRAWAL, 
                                                   amount, description, account, account.getBalance());
        
        return transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> transfer(String fromAccountNumber, String toAccountNumber, 
                                      BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Transfer amount must be positive");
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new BankingException("Cannot transfer to the same account");
        }

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountNumber));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(toAccountNumber));

        if (fromAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new BankingException("Source account is not active");
        }

        if (toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new BankingException("Destination account is not active");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Available: " + fromAccount.getBalance() + ", Required: " + amount);
        }

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create transfer-out transaction
        String transactionId1 = generateTransactionId();
        Transaction transferOut = new Transaction(transactionId1, TransactionType.TRANSFER_OUT, 
                                                   amount, description, fromAccount, fromAccount.getBalance());
        transferOut.setToAccountNumber(toAccountNumber);
        transferOut.setFromAccountNumber(fromAccountNumber);
        transactionRepository.save(transferOut);

        // Create transfer-in transaction
        String transactionId2 = generateTransactionId();
        Transaction transferIn = new Transaction(transactionId2, TransactionType.TRANSFER_IN, 
                                                 amount, description, toAccount, toAccount.getBalance());
        transferIn.setToAccountNumber(toAccountNumber);
        transferIn.setFromAccountNumber(fromAccountNumber);
        transactionRepository.save(transferIn);

        return List.of(transferOut, transferIn);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return transactionRepository.findByAccountIdOrderByTimestampDesc(account.getId());
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByType(String accountNumber, TransactionType type) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return transactionRepository.findByAccountIdAndType(account.getId(), type);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(String accountNumber, 
                                                        LocalDateTime startDate, 
                                                        LocalDateTime endDate) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return transactionRepository.findByAccountIdAndTimestampBetween(
            account.getId(), startDate, endDate);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
