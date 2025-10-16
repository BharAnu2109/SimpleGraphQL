# GraphQL Query Examples

This document contains comprehensive examples of all GraphQL queries and mutations available in the Banking Application.

## Table of Contents

1. [Customer Management](#customer-management)
2. [Account Management](#account-management)
3. [Transaction Management](#transaction-management)
4. [Complex Queries](#complex-queries)
5. [Error Scenarios](#error-scenarios)

## Customer Management

### Create Customer

```graphql
mutation CreateNewCustomer {
  createCustomer(input: {
    name: "Alice Johnson"
    email: "alice.johnson@example.com"
    phone: "+1555123456"
    address: "789 Elm Street, Springfield"
  }) {
    id
    name
    email
    phone
    address
  }
}
```

### Get All Customers with Accounts

```graphql
query GetAllCustomersWithAccounts {
  getAllCustomers {
    id
    name
    email
    phone
    address
    accounts {
      id
      accountNumber
      accountType
      balance
      status
      createdAt
    }
  }
}
```

### Get Customer by ID

```graphql
query GetCustomerDetails {
  getCustomerById(id: 1) {
    id
    name
    email
    phone
    address
    accounts {
      accountNumber
      accountType
      balance
      status
      transactions {
        transactionId
        type
        amount
        timestamp
      }
    }
  }
}
```

### Get Customer by Email

```graphql
query GetCustomerByEmail {
  getCustomerByEmail(email: "john.doe@example.com") {
    id
    name
    email
    accounts {
      accountNumber
      balance
    }
  }
}
```

### Update Customer Information

```graphql
mutation UpdateCustomerInfo {
  updateCustomer(input: {
    id: 1
    name: "John Smith Doe"
    phone: "+1555999888"
    address: "999 Updated Avenue, New City"
  }) {
    id
    name
    email
    phone
    address
  }
}
```

### Delete Customer

```graphql
mutation RemoveCustomer {
  deleteCustomer(id: 5)
}
```

## Account Management

### Create Savings Account

```graphql
mutation CreateSavingsAccount {
  createAccount(input: {
    customerId: 1
    accountType: SAVINGS
    initialDeposit: 5000.0
  }) {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
    customer {
      name
      email
    }
  }
}
```

### Create Checking Account

```graphql
mutation CreateCheckingAccount {
  createAccount(input: {
    customerId: 2
    accountType: CHECKING
    initialDeposit: 1500.0
  }) {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
  }
}
```

### Create Business Account

```graphql
mutation CreateBusinessAccount {
  createAccount(input: {
    customerId: 3
    accountType: BUSINESS
    initialDeposit: 10000.0
  }) {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
  }
}
```

### Get All Accounts

```graphql
query GetAllAccounts {
  getAllAccounts {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
    customer {
      id
      name
      email
    }
  }
}
```

### Get Account by Number with Full Details

```graphql
query GetAccountDetails {
  getAccountByNumber(accountNumber: "0123456789") {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
    customer {
      id
      name
      email
      phone
    }
    transactions {
      transactionId
      type
      amount
      description
      timestamp
      balanceAfter
      fromAccountNumber
      toAccountNumber
    }
  }
}
```

### Get Accounts for a Customer

```graphql
query GetCustomerAccounts {
  getAccountsByCustomerId(customerId: 1) {
    id
    accountNumber
    accountType
    balance
    status
    createdAt
  }
}
```

### Check Account Balance

```graphql
query CheckBalance {
  getBalance(accountNumber: "0123456789")
}
```

### Freeze Account

```graphql
mutation FreezeAccount {
  updateAccountStatus(accountNumber: "0123456789", status: FROZEN) {
    accountNumber
    status
  }
}
```

### Activate Account

```graphql
mutation ActivateAccount {
  updateAccountStatus(accountNumber: "0123456789", status: ACTIVE) {
    accountNumber
    status
  }
}
```

### Close Account (Balance must be zero)

```graphql
mutation CloseAccount {
  closeAccount(accountNumber: "0123456789")
}
```

## Transaction Management

### Deposit Funds

```graphql
mutation DepositFunds {
  deposit(input: {
    accountNumber: "0123456789"
    amount: 1000.0
    description: "Monthly salary deposit"
  }) {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
  }
}
```

### Withdraw Cash

```graphql
mutation WithdrawCash {
  withdraw(input: {
    accountNumber: "0123456789"
    amount: 200.0
    description: "ATM cash withdrawal"
  }) {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
  }
}
```

### Transfer Between Accounts

```graphql
mutation TransferMoney {
  transfer(input: {
    fromAccountNumber: "0123456789"
    toAccountNumber: "9876543210"
    amount: 500.0
    description: "Rent payment"
  }) {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
    fromAccountNumber
    toAccountNumber
  }
}
```

### Get Transaction History (Most Recent First)

```graphql
query GetRecentTransactions {
  getTransactionHistory(accountNumber: "0123456789") {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
    fromAccountNumber
    toAccountNumber
  }
}
```

### Get Deposits Only

```graphql
query GetDepositTransactions {
  getTransactionsByType(accountNumber: "0123456789", type: DEPOSIT) {
    transactionId
    amount
    description
    timestamp
    balanceAfter
  }
}
```

### Get Withdrawals Only

```graphql
query GetWithdrawalTransactions {
  getTransactionsByType(accountNumber: "0123456789", type: WITHDRAWAL) {
    transactionId
    amount
    description
    timestamp
    balanceAfter
  }
}
```

### Get Transfers In

```graphql
query GetTransfersIn {
  getTransactionsByType(accountNumber: "0123456789", type: TRANSFER_IN) {
    transactionId
    amount
    description
    timestamp
    balanceAfter
    fromAccountNumber
  }
}
```

### Get Transfers Out

```graphql
query GetTransfersOut {
  getTransactionsByType(accountNumber: "0123456789", type: TRANSFER_OUT) {
    transactionId
    amount
    description
    timestamp
    balanceAfter
    toAccountNumber
  }
}
```

### Get Monthly Statement (Current Month)

```graphql
query GetMonthlyStatement {
  getTransactionsByDateRange(
    accountNumber: "0123456789"
    startDate: "2025-10-01T00:00:00"
    endDate: "2025-10-31T23:59:59"
  ) {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
  }
}
```

### Get Yearly Statement

```graphql
query GetYearlyStatement {
  getTransactionsByDateRange(
    accountNumber: "0123456789"
    startDate: "2025-01-01T00:00:00"
    endDate: "2025-12-31T23:59:59"
  ) {
    transactionId
    type
    amount
    description
    timestamp
    balanceAfter
  }
}
```

## Complex Queries

### Customer with Complete Banking Information

```graphql
query CompleteCustomerProfile {
  getCustomerById(id: 1) {
    id
    name
    email
    phone
    address
    accounts {
      accountNumber
      accountType
      balance
      status
      createdAt
      transactions {
        transactionId
        type
        amount
        description
        timestamp
        balanceAfter
      }
    }
  }
}
```

### Account Summary with Recent Transactions

```graphql
query AccountSummary {
  getAccountByNumber(accountNumber: "0123456789") {
    accountNumber
    accountType
    balance
    status
    customer {
      name
      email
    }
  }
  
  getTransactionHistory(accountNumber: "0123456789") {
    transactionId
    type
    amount
    description
    timestamp
  }
}
```

### Create Customer and Account in Sequence

```graphql
# First mutation - Create customer
mutation Step1CreateCustomer {
  createCustomer(input: {
    name: "New Customer"
    email: "newcustomer@example.com"
    phone: "+1234567890"
    address: "123 New Street"
  }) {
    id
    name
    email
  }
}

# Second mutation - Create account (use customer ID from previous response)
mutation Step2CreateAccount {
  createAccount(input: {
    customerId: 4  # Use the ID returned from Step1
    accountType: SAVINGS
    initialDeposit: 1000.0
  }) {
    accountNumber
    balance
    customer {
      name
    }
  }
}
```

### Multiple Operations Example

```graphql
# Deposit to one account
mutation DepositToAccount1 {
  deposit(input: {
    accountNumber: "0123456789"
    amount: 500.0
    description: "Deposit"
  }) {
    transactionId
    balanceAfter
  }
}

# Then transfer to another account
mutation TransferToAccount2 {
  transfer(input: {
    fromAccountNumber: "0123456789"
    toAccountNumber: "9876543210"
    amount: 200.0
    description: "Transfer"
  }) {
    transactionId
    balanceAfter
  }
}

# Check balance of first account
query CheckFinalBalance {
  getBalance(accountNumber: "0123456789")
}
```

## Error Scenarios

### Attempting to Withdraw with Insufficient Balance

```graphql
mutation InsufficientFundsWithdrawal {
  withdraw(input: {
    accountNumber: "0123456789"
    amount: 999999.0  # More than available balance
    description: "Large withdrawal attempt"
  }) {
    transactionId
  }
}
```

Expected Error:
```json
{
  "errors": [
    {
      "message": "Insufficient balance. Available: 1000.00, Required: 999999.00"
    }
  ]
}
```

### Creating Customer with Duplicate Email

```graphql
mutation DuplicateEmailCustomer {
  createCustomer(input: {
    name: "Duplicate"
    email: "john.doe@example.com"  # Already exists
    phone: "+1234567890"
    address: "Some address"
  }) {
    id
  }
}
```

Expected Error:
```json
{
  "errors": [
    {
      "message": "Customer with email john.doe@example.com already exists"
    }
  ]
}
```

### Transfer to Same Account

```graphql
mutation SameAccountTransfer {
  transfer(input: {
    fromAccountNumber: "0123456789"
    toAccountNumber: "0123456789"  # Same account
    amount: 100.0
    description: "Invalid transfer"
  }) {
    transactionId
  }
}
```

Expected Error:
```json
{
  "errors": [
    {
      "message": "Cannot transfer to the same account"
    }
  ]
}
```

### Closing Account with Non-Zero Balance

```graphql
mutation CloseAccountWithBalance {
  closeAccount(accountNumber: "0123456789")  # Has balance > 0
}
```

Expected Error:
```json
{
  "errors": [
    {
      "message": "Cannot close account with non-zero balance"
    }
  ]
}
```

### Deleting Customer with Active Accounts

```graphql
mutation DeleteCustomerWithAccounts {
  deleteCustomer(id: 1)  # Has active accounts
}
```

Expected Error:
```json
{
  "errors": [
    {
      "message": "Cannot delete customer with active accounts"
    }
  ]
}
```

## Tips for Using GraphiQL

1. **Auto-complete**: Press `Ctrl+Space` to see available fields and types
2. **Documentation**: Click "Docs" in the top-right corner to explore the schema
3. **Query History**: Click the history icon to see previous queries
4. **Variables**: Use the Variables panel for parameterized queries
5. **Prettify**: Click the prettify button to format your query

## Using Variables in Queries

Instead of hardcoding values, you can use variables:

```graphql
mutation CreateCustomerWithVariables($input: CreateCustomerInput!) {
  createCustomer(input: $input) {
    id
    name
    email
  }
}
```

Variables (in the Variables panel):
```json
{
  "input": {
    "name": "Variable Customer",
    "email": "variable@example.com",
    "phone": "+1234567890",
    "address": "Variable Address"
  }
}
```

## Testing Workflow

1. Create a customer
2. Create an account for that customer
3. Perform deposits
4. Perform withdrawals
5. Transfer funds between accounts
6. Check transaction history
7. Check balance
8. Update account status
9. Close account (after withdrawing all funds)
10. Delete customer (after closing all accounts)
