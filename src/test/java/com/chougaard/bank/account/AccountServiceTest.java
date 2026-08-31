package com.chougaard.bank.account;

import com.chougaard.bank.account.dto.AccountResponse;
import com.chougaard.bank.account.dto.CreateAccountRequest;
import com.chougaard.bank.account.dto.DepositRequest;
import com.chougaard.bank.common.exception.AccountNotFoundException;
import com.chougaard.bank.common.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	private final static String ACCOUNT_NUMBER = "1000000001";

	@Mock
	AccountRepository repositoryMock;

	@InjectMocks
	AccountService accountService;

	@Test
	void createAccount_startsAtZeroBalance_withGeneratedNumber() {
		// Arrange
		ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
		CreateAccountRequest request = new CreateAccountRequest("Bob", "Builder");

		// Act
		AccountResponse response = accountService.createAccount(request);

		// Assert
		Mockito.verify(repositoryMock).persist(captor.capture());
		assertEquals(captor.getValue().getAccountNumber(), response.accountNumber());
		assertEquals(request.firstName() + " " + request.lastName(), captor.getValue().getOwner());
		assertNotNull(captor.getValue().getAccountNumber());
		assertEquals(0, new BigDecimal("0.00").compareTo(captor.getValue().getBalance()));
	}

	@Test
	void getAccount_returnsAccountResponse() {
		// Arrange
		Account account = new Account("Bob", "Builder", new BigDecimal("100.00"));
		account.setAccountNumber(ACCOUNT_NUMBER);
		Mockito.when(repositoryMock.findByAccountNumber(ACCOUNT_NUMBER))
				.thenReturn(Optional.of(account));

		// Act
		AccountResponse response =
				accountService.getAccountByAccountNumber(ACCOUNT_NUMBER);

		// Assert
		assertEquals(account.getAccountNumber(), response.accountNumber());
		assertEquals(0, account.getBalance().compareTo(response.balance()));
		assertEquals(account.getOwner(), response.owner());
	}

	@Test
	void getAccount_unknownAccount_throwsAccountNotFoundException() {
		// Arrange
		Mockito.when(repositoryMock.findByAccountNumber(ACCOUNT_NUMBER))
				.thenReturn(Optional.empty());

		// Act & assert
		assertThrows(AccountNotFoundException.class,
				() -> accountService.getAccountByAccountNumber(ACCOUNT_NUMBER));
	}

	@Test
	void deposit_addsAmountToBalance_andNormalizesScale() {
		// Arrange
		Account account = new Account("Bob", "Builder", new BigDecimal("100.00"));
		account.setAccountNumber(ACCOUNT_NUMBER);
		Mockito.when(repositoryMock.findByAccountNumber(ACCOUNT_NUMBER))
				.thenReturn(Optional.of(account));

		// Act
		AccountResponse response =
				accountService.deposit(
						ACCOUNT_NUMBER,
						new DepositRequest(new BigDecimal("50.5")));

		// Assert
		assertEquals(0, new BigDecimal("150.50").compareTo(response.balance()));
		Mockito.verify(repositoryMock).findByAccountNumber(ACCOUNT_NUMBER);
	}

	@Test
	void deposit_unknownAccount_throwsAccountNotFoundException() {
		// Arrange
		DepositRequest depositRequest = new DepositRequest(new BigDecimal("50.5"));
		Mockito.when(repositoryMock.findByAccountNumber(ACCOUNT_NUMBER))
				.thenReturn(Optional.empty());

		// Act & assert
		assertThrows(AccountNotFoundException.class,
				() -> accountService.deposit(ACCOUNT_NUMBER, depositRequest));
	}

	@Test
	void withdraw_sufficientFunds_subtractsAmount() {
		// Arrange
		Account account = new Account("Bob", "Builder", new BigDecimal("100.00"));

		// Act
		accountService.withdraw(account, new BigDecimal("30.00"));

		// Assert
		assertEquals(0, new BigDecimal("70.00").compareTo(account.getBalance()));
	}

	@Test
	void withdraw_insufficientFunds_throwsExceptionAndDoesNotSubtract() {
		// Arrange
		Account account = new Account("Bob", "Builder", new BigDecimal("100.00"));

		// Act & assert
		assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(account, new BigDecimal("150.00")));
		assertEquals(0, new BigDecimal("100.00").compareTo(account.getBalance()));
	}

	@Test
	void credit_addsAmount() {
		// Arrange
		Account account = new Account("Bob", "Builder", new BigDecimal("100.00"));

		// Act
		accountService.credit(account, new BigDecimal("25.50"));

		// Assert
		assertEquals(0, new BigDecimal("125.50").compareTo(account.getBalance()));
	}
}