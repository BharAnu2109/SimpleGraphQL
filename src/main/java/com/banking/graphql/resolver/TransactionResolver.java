package com.banking.graphql.resolver;

import com.banking.graphql.model.Transaction;
import com.banking.graphql.model.TransactionType;
import com.banking.graphql.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TransactionResolver {

    private final TransactionService transactionService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @QueryMapping
    public List<Transaction> getTransactionHistory(@Argument String accountNumber) {
        return transactionService.getTransactionHistory(accountNumber);
    }

    @QueryMapping
    public List<Transaction> getTransactionsByType(@Argument String accountNumber, 
                                                    @Argument TransactionType type) {
        return transactionService.getTransactionsByType(accountNumber, type);
    }

    @QueryMapping
    public List<Transaction> getTransactionsByDateRange(@Argument String accountNumber,
                                                         @Argument String startDate,
                                                         @Argument String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        return transactionService.getTransactionsByDateRange(accountNumber, start, end);
    }

    @MutationMapping
    public Transaction deposit(@Argument Map<String, Object> input) {
        String accountNumber = (String) input.get("accountNumber");
        BigDecimal amount = BigDecimal.valueOf(
            ((Number) input.get("amount")).doubleValue()
        );
        String description = (String) input.get("description");
        
        return transactionService.deposit(accountNumber, amount, description);
    }

    @MutationMapping
    public Transaction withdraw(@Argument Map<String, Object> input) {
        String accountNumber = (String) input.get("accountNumber");
        BigDecimal amount = BigDecimal.valueOf(
            ((Number) input.get("amount")).doubleValue()
        );
        String description = (String) input.get("description");
        
        return transactionService.withdraw(accountNumber, amount, description);
    }

    @MutationMapping
    public List<Transaction> transfer(@Argument Map<String, Object> input) {
        String fromAccountNumber = (String) input.get("fromAccountNumber");
        String toAccountNumber = (String) input.get("toAccountNumber");
        BigDecimal amount = BigDecimal.valueOf(
            ((Number) input.get("amount")).doubleValue()
        );
        String description = (String) input.get("description");
        
        return transactionService.transfer(fromAccountNumber, toAccountNumber, amount, description);
    }
}
