# Banking System

## Project Overview:
The Banking System project is a simple banking application
designed to simulate the key functionalities of a modern banking system.
It allows users to manage different types of accounts,
perform transactions like deposits, withdrawals, and fund transfers,
and maintain user roles like Admin and Customer for appropriate access control.
The project is built using Core Java, with optional database integration for persistence,
showcasing Object-Oriented Programming (OOP) principles, error handling, and data management.

## Features:
- **Account Types**: Savings, Checking, Business Accounts.
- **Transactions**: Deposits, withdrawals, fund transfers.
- **User Roles**: Admin (for managing accounts), Customer (for performing transactions).
- **Transaction History**: Record of all transactions per user.
  
## Technologies:
- **Core Java**: Classes for accounts, users, transactions.
- **DB**: Database integration (e.g., MySQL) for persistence.

## Skills:
- **OOP** (e.g., inheritance for different account types).
- **Exception handling** (e.g., insufficient funds, invalid account).
- **Database management** (for storing data).

## Object-Oriented Design:
- **Classes**: Separation of concerns using different classes for users, accounts, and transactions.
    - **_User_**: Represents a system user, either an Admin or Customer. 
    - **_Account_**: Represents different types of accounts (Savings, Checking, Business) with unique balances. 
    - **_Transaction_**: Tracks deposits, withdrawals, and transfers between accounts.
- **Inheritance**: Different account types inherit from a base Account class, using specific methods like deposit() and withdraw().
- **Encapsulation**:
Sensitive data (e.g., passwords, balances) are kept private and accessed through public methods.

## Error Handling:
- **Custom Exception Handling**:
Ensures smooth execution by handling errors such as insufficient funds, invalid accounts, and invalid user credentials.
Provides meaningful error messages to the user.

## Database Design:
- **Tables**:
  - _**users**_: Stores user details (ID, email, password, role). 
  - _**accounts**_: Stores account details (account ID, user ID, account type, balance). 
  - **_transactions_**: Logs all transactions, including deposits, withdrawals, and transfers, with timestamps.
- **SQL**:
SQL queries are used to interact with the database, including retrieving account balances, updating user data, and recording transactions.

## Testing:
### Best Practices:
In a professional environment, the best practice is
to test using mock databases or a dedicated test database to ensure that tests are isolated from real data.
This helps speed up testing and ensures that no unintended changes are made to production data.
Popular frameworks such as JUnit for unit testing and Mockito for mocking dependencies are commonly used in industry.

### Approach in This Project:
For the sake of simplicity in this project, I've chosen to test using the real database,
but with proper setup and tear down functions to maintain a clean testing environment.
This decision was made to keep the project lightweight and straightforward,
but I recognize the potential issues that come with this approach in larger, more complex systems.

- **Unit Testing**: Tests are written for individual functions such as login, deposits, and transfers, 
focusing on key functionality.
  However, instead of using mocks, these tests interact with the real database to ensure real-world behavior.
- **Database Interaction**:
Since the tests use the actual database, I have included functions to clean up test data after each test,
ensuring the database is reset for the next test run.
- **Consideration for Future Development**: If this project were to be expanded, I would refactor the tests to use mocks for database interactions or introduce a dedicated test database. This would follow industry standards and allow for faster, more reliable tests that are independent of external systems.

### Running the Tests:
- **Setup**: Ensure the database is running and accessible.
- **Testing**: Each test is run in isolation, with proper cleanup after every test.
- **Test Data**: Test users and accounts are created at the start of the tests, and all changes made during the test are reset once the test is complete.

## How to Run:
1. Clone the repository to your local machine.
2. Set up the database using the provided SQL schema and credentials.
3. Run the application using your preferred IDE or Java environment.
4. Interact with the console menu to perform operations like creating users, managing accounts, and making transactions.