# Bank Account API

A REST API for a simple bank account system, built with Quarkus. Supports creating
accounts, deposits, atomic transfers between accounts, and a DKK→USD exchange rate
lookup via a third-party provider.

## Tech stack

- Java 21 / Quarkus
- Hibernate ORM with Panache (repository pattern)
- H2 in-memory database
- Hibernate Validator for request validation
- REST Client for third-party exchange-rate integration
- JUnit 5, Mockito, REST-assured for testing

## Prerequisites

- Java 21+
- (Bonus) A free API key from https://www.exchangerate-api.com for the exchange endpoint

## Configuration

The exchange-rate endpoint requires an API key, supplied via an environment variable.
Copy `.env.example` to `.env` and set your key:

    EXCHANGE_API_KEY=your-key-here

The core account and transfer endpoints work without any key.

## Running

    ./mvnw quarkus:dev

The app starts on `http://localhost:8080`. The database is in-memory and seeded with
three mock accounts on startup (see `src/main/resources/import.sql`):

| Account number | Owner              | Balance     |
|----------------|--------------------|-------------|
| 1000000001     | Anders And         | 1000.00     |
| 1000000002     | Andersine And      | 5000.00     |
| 1000000003     | Joakim Von And     | 9999999.00  |

Data resets on every restart.

## API

Base path: `/bank/api/v1`
There are two options for interacting with the API:
<details>
<summary>Postman</summary>
Import `Bank.postman_collection.json` into Postman and use the UI to interact using the template REST calls.

### Create an account
Returns `201 Created` with the new account (including a generated account number) and a
`Location` header.

### Get an account
Returns the account number, owner, and balance. `404` if not found.

### Deposit
Returns the updated account. `400` if the amount is missing or not positive.

### Transfer
Returns `201 Created` with the transfer record and both updated balances.
`400` for a same-account transfer, `404` for an unknown account, `409` for insufficient funds.

### Get a transfer
Returns the persisted transfer record. `404` if not found.

### Exchange rate (bonus)
Returns the current DKK→USD conversion, e.g. `{"DKK":100,"USD":14.61}`. Requires
`EXCHANGE_API_KEY` to be set.

</details>
<details>
<summary>cURL commands</summary>

### Create an account

    curl -X POST http://localhost:8080/bank/api/v1/accounts \
      -H "Content-Type: application/json" \
      -d '{"firstName":"Bob","lastName":"Builder"}'

Returns `201 Created` with the new account (including a generated account number) and a
`Location` header.

### Get an account

    curl http://localhost:8080/bank/api/v1/accounts/1000000001

Returns the account number, owner, and balance. `404` if not found.

### Deposit

    curl -X POST http://localhost:8080/bank/api/v1/accounts/1000000001/deposits \
      -H "Content-Type: application/json" \
      -d '{"amount":250.00}'

Returns the updated account. `400` if the amount is missing or not positive.

### Transfer

    curl -X POST http://localhost:8080/bank/api/v1/transfers \
      -H "Content-Type: application/json" \
      -d '{"fromAccountNumber":"1000000003","toAccountNumber":"1000000001","amount":100.00}'

Returns `201 Created` with the transfer record and both updated balances.
`400` for a same-account transfer, `404` for an unknown account, `409` for insufficient funds.

### Get a transfer

    curl http://localhost:8080/bank/api/v1/transfers/1

Returns the persisted transfer record. `404` if not found.

### Exchange rate (bonus)

    curl "http://localhost:8080/bank/api/v1/exchange/dkk-usd?amount=100"

Returns the current DKK→USD conversion, e.g. `{"DKK":100,"USD":14.61}`. Requires
`EXCHANGE_API_KEY` to be set.
</details>
