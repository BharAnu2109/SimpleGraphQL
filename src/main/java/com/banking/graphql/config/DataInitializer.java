package com.banking.graphql.config;

import com.banking.graphql.model.AccountType;
import com.banking.graphql.service.AccountService;
import com.banking.graphql.service.CustomerService;
import com.banking.graphql.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");
        
        try {
            // Create customers
            var customer1 = customerService.createCustomer(
                "John Doe", "john.doe@example.com", "+1234567890", "123 Main St, City, Country"
            );
            
            var customer2 = customerService.createCustomer(
                "Jane Smith", "jane.smith@example.com", "+0987654321", "456 Oak Ave, Town, Country"
            );
            
            var customer3 = customerService.createCustomer(
                "Bob Johnson", "bob.johnson@example.com", "+1122334455", "789 Pine Rd, Village, Country"
            );
            
            log.info("Created {} customers", 3);
            
            // Create accounts
            var account1 = accountService.createAccount(
                customer1.getId(), AccountType.SAVINGS, new BigDecimal("1000.00")
            );
            
            var account2 = accountService.createAccount(
                customer1.getId(), AccountType.CHECKING, new BigDecimal("500.00")
            );
            
            var account3 = accountService.createAccount(
                customer2.getId(), AccountType.SAVINGS, new BigDecimal("2000.00")
            );
            
            var account4 = accountService.createAccount(
                customer2.getId(), AccountType.BUSINESS, new BigDecimal("5000.00")
            );
            
            var account5 = accountService.createAccount(
                customer3.getId(), AccountType.CHECKING, new BigDecimal("750.00")
            );
            
            log.info("Created {} accounts", 5);
            
            // Perform some transactions
            transactionService.deposit(
                account1.getAccountNumber(), new BigDecimal("500.00"), "Initial bonus deposit"
            );
            
            transactionService.withdraw(
                account2.getAccountNumber(), new BigDecimal("100.00"), "ATM withdrawal"
            );
            
            transactionService.transfer(
                account3.getAccountNumber(), 
                account1.getAccountNumber(), 
                new BigDecimal("250.00"), 
                "Transfer to John's savings"
            );
            
            transactionService.deposit(
                account4.getAccountNumber(), new BigDecimal("1000.00"), "Business revenue"
            );
            
            log.info("Sample data initialization completed successfully!");
            log.info("Account Numbers Created:");
            log.info("  - John Doe Savings: {}", account1.getAccountNumber());
            log.info("  - John Doe Checking: {}", account2.getAccountNumber());
            log.info("  - Jane Smith Savings: {}", account3.getAccountNumber());
            log.info("  - Jane Smith Business: {}", account4.getAccountNumber());
            log.info("  - Bob Johnson Checking: {}", account5.getAccountNumber());
            
        } catch (Exception e) {
            log.error("Error initializing sample data", e);
        }
    }
}
