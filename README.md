# Banking Application with GraphQL

A comprehensive Spring Boot banking application with GraphQL support, demonstrating all major banking scenarios including customer management, account operations, and transactions.

## Features

### Customer Management
- Create new customers
- Update customer information
- View customer details
- Delete customers
- List all customers

### Account Management
- Create accounts (Savings, Checking, Business)
- View account details
- Check account balance
- Update account status (Active, Inactive, Frozen, Closed)
- Close accounts
- List accounts by customer

### Transaction Operations
- Deposit funds
- Withdraw funds
- Transfer between accounts
- View transaction history
- Filter transactions by type
- View transactions by date range

## Technology Stack

- **Spring Boot 3.1.5**
- **Spring GraphQL**
- **Spring Data JPA**
- **H2 Database** (In-memory)
- **Lombok**
- **Maven**
- **Java 17**

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build and Run

```bash
# Clone the repository
git clone https://github.com/BharAnu2109/SimpleGraphQL.git
cd SimpleGraphQL

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Points

- **GraphQL Endpoint**: `http://localhost:8080/graphql`
- **GraphiQL UI**: `http://localhost:8080/graphiql`
- **H2 Console**: `http://localhost:8080/h2-console`

## GraphQL Schema

### Types

```graphql
type Customer {
    id: ID!
    name: String!
    email: String!
    phone: String!
    address: String!
    accounts: [Account!]
}

type Account {
    id: ID!
    accountNumber: String!
    accountType: AccountType!
    balance: Float!
    createdAt: String!
    status: AccountStatus!
    customer: Customer!
    transactions: [Transaction!]
}

type Transaction {
    id: ID!
    transactionId: String!
    type: TransactionType!
    amount: Float!
    timestamp: String!
    description: String!
    balanceAfter: Float!
    toAccountNumber: String
    fromAccountNumber: String
}
```

### Enums

```graphql
enum AccountType {
    SAVINGS
    CHECKING
    BUSINESS
}

enum AccountStatus {
    ACTIVE
    INACTIVE
    FROZEN
    CLOSED
}

enum TransactionType {
    DEPOSIT
    WITHDRAWAL
    TRANSFER_IN
    TRANSFER_OUT
}
```

## Sample GraphQL Queries

### Customer Operations

#### Create a Customer

```graphql
mutation {
  createCustomer(input: {
    name: "John Doe"
    email: "john.doe@example.com"
    phone: "+1234567890"
    address: "123 Main St, City, Country"
  }) {
    id
    name
    email
    phone
    address
  }
}
```

#### Get All Customers

```graphql
query {
  getAllCustomers {
    id
    name
    email
    phone
    address
    accounts {
      accountNumber
      accountType
      balance
    }
  }
}
```

#### Get Customer by ID

```graphql
query {
  getCustomerById(id: 1) {
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

#### Update Customer

```graphql
mutation {
  updateCustomer(input: {
    id: 1
    name: "John Updated"
    phone: "+9876543210"
    address: "456 New Street"
  }) {
    id
    name
    phone
    address
  }
}
```

#### Delete Customer

```graphql
mutation {
  deleteCustomer(id: 1)
}
```

### Account Operations

#### Create an Account

```graphql
mutation {
  createAccount(input: {
    customerId: 1
    accountType: SAVINGS
    initialDeposit: 1000.0
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

#### Get All Accounts

```graphql
query {
  getAllAccounts {
    id
    accountNumber
    accountType
    balance
    status
    customer {
      name
      email
    }
  }
}
```

#### Get Account by Number

```graphql
query {
  getAccountByNumber(accountNumber: "0123456789") {
    id
    accountNumber
    accountType
    balance
    status
    customer {
      name
      email
    }
    transactions {
      transactionId
      type
      amount
      timestamp
    }
  }
}
```

#### Get Accounts by Customer

```graphql
query {
  getAccountsByCustomerId(customerId: 1) {
    accountNumber
    accountType
    balance
    status
  }
}
```

#### Get Balance

```graphql
query {
  getBalance(accountNumber: "0123456789")
}
```

#### Update Account Status

```graphql
mutation {
  updateAccountStatus(accountNumber: "0123456789", status: FROZEN) {
    accountNumber
    status
  }
}
```

#### Close Account

```graphql
mutation {
  closeAccount(accountNumber: "0123456789")
}
```

### Transaction Operations

#### Deposit

```graphql
mutation {
  deposit(input: {
    accountNumber: "0123456789"
    amount: 500.0
    description: "Salary deposit"
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

#### Withdraw

```graphql
mutation {
  withdraw(input: {
    accountNumber: "0123456789"
    amount: 100.0
    description: "ATM withdrawal"
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

#### Transfer

```graphql
mutation {
  transfer(input: {
    fromAccountNumber: "0123456789"
    toAccountNumber: "9876543210"
    amount: 200.0
    description: "Transfer to friend"
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

#### Get Transaction History

```graphql
query {
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

#### Get Transactions by Type

```graphql
query {
  getTransactionsByType(accountNumber: "0123456789", type: DEPOSIT) {
    transactionId
    type
    amount
    description
    timestamp
  }
}
```

#### Get Transactions by Date Range

```graphql
query {
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
  }
}
```

## Sample Data

The application comes with pre-populated sample data:

- **3 Customers** (John Doe, Jane Smith, Bob Johnson)
- **5 Accounts** (Various types: Savings, Checking, Business)
- **Sample Transactions** (Deposits, Withdrawals, Transfers)

Account numbers are displayed in the console logs when the application starts.

## Error Handling

The application includes comprehensive error handling for:

- Customer not found
- Account not found
- Insufficient balance
- Duplicate email addresses
- Invalid account status for operations
- Negative amounts
- Closed accounts

## Testing

Run the test suite:

```bash
mvn test
```

The project includes integration tests for:
- Customer creation and retrieval
- Account operations
- Deposits and balance checking
- GraphQL query execution

## Database

The application uses H2 in-memory database. You can access the H2 console at:

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:bankingdb
Username: sa
Password: (leave empty)
```

## Project Structure

```
src/
├── main/
│   ├── java/com/banking/graphql/
│   │   ├── config/          # Configuration classes
│   │   ├── exception/       # Custom exceptions
│   │   ├── model/           # Entity classes
│   │   ├── repository/      # JPA repositories
│   │   ├── resolver/        # GraphQL resolvers
│   │   └── service/         # Business logic
│   └── resources/
│       ├── graphql/
│       │   └── schema.graphqls  # GraphQL schema
│       └── application.properties
└── test/
    └── java/com/banking/graphql/
        └── GraphQLIntegrationTest.java
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source and available under the MIT License.
