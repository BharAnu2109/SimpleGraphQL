package com.banking.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
public class GraphQLIntegrationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testGetAllCustomers() {
        this.graphQlTester
            .document("""
                query {
                    getAllCustomers {
                        id
                        name
                        email
                    }
                }
                """)
            .execute()
            .path("getAllCustomers")
            .entityList(Object.class)
            .hasSizeGreaterThan(0);
    }

    @Test
    void testCreateCustomer() {
        String email = "test" + System.currentTimeMillis() + "@example.com";
        
        this.graphQlTester
            .document("""
                mutation CreateCustomer($input: CreateCustomerInput!) {
                    createCustomer(input: $input) {
                        id
                        name
                        email
                        phone
                        address
                    }
                }
                """)
            .variable("input", java.util.Map.of(
                "name", "Test User",
                "email", email,
                "phone", "+1234567890",
                "address", "Test Address"
            ))
            .execute()
            .path("createCustomer")
            .entity(Object.class)
            .satisfies(customer -> {
                assertThat(customer).isNotNull();
            });
    }

    @Test
    void testGetAllAccounts() {
        this.graphQlTester
            .document("""
                query {
                    getAllAccounts {
                        id
                        accountNumber
                        accountType
                        balance
                        status
                    }
                }
                """)
            .execute()
            .path("getAllAccounts")
            .entityList(Object.class)
            .hasSizeGreaterThan(0);
    }

    @Test
    void testDeposit() {
        // First get an account number
        var accountResponse = this.graphQlTester
            .document("""
                query {
                    getAllAccounts {
                        accountNumber
                    }
                }
                """)
            .execute()
            .path("getAllAccounts[0].accountNumber")
            .entity(String.class)
            .get();

        // Then perform deposit
        this.graphQlTester
            .document("""
                mutation Deposit($input: DepositInput!) {
                    deposit(input: $input) {
                        transactionId
                        type
                        amount
                        description
                    }
                }
                """)
            .variable("input", java.util.Map.of(
                "accountNumber", accountResponse,
                "amount", 100.0,
                "description", "Test deposit"
            ))
            .execute()
            .path("deposit.type")
            .entity(String.class)
            .isEqualTo("DEPOSIT");
    }

    @Test
    void testGetBalance() {
        // First get an account number
        var accountResponse = this.graphQlTester
            .document("""
                query {
                    getAllAccounts {
                        accountNumber
                    }
                }
                """)
            .execute()
            .path("getAllAccounts[0].accountNumber")
            .entity(String.class)
            .get();

        // Then get balance
        this.graphQlTester
            .document("""
                query GetBalance($accountNumber: String!) {
                    getBalance(accountNumber: $accountNumber)
                }
                """)
            .variable("accountNumber", accountResponse)
            .execute()
            .path("getBalance")
            .entity(Double.class)
            .satisfies(balance -> {
                assertThat(balance).isNotNull();
                assertThat(balance).isGreaterThanOrEqualTo(0.0);
            });
    }
}
