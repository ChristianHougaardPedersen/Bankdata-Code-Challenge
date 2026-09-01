package com.chougaard.bank.transfer;

import com.chougaard.bank.account.Account;
import com.chougaard.bank.account.AccountService;
import com.chougaard.bank.common.exception.AccountNotFoundException;
import com.chougaard.bank.common.exception.InsufficientFundsException;
import com.chougaard.bank.common.exception.SameAccountTransferException;
import com.chougaard.bank.transfer.dto.TransferCreatedResponse;
import com.chougaard.bank.transfer.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

	private static final String ACCOUNT1 = "1111111111";
	private static final String ACCOUNT2 = "2222222222";

	private Account testAccount1;
	private Account testAccount2;
	private TransferRequest request;

	@Mock
	AccountService accountServiceMock;

	@Mock
	TransferRepository transferRepositoryMock;

	@InjectMocks
	TransferService transferService;

	@BeforeEach
	void setup() {
		request = new TransferRequest(ACCOUNT1, ACCOUNT2, new BigDecimal("50.00"));
		testAccount1 = new Account("Bob", "Builder", new BigDecimal("100.00"));
		testAccount2 = new Account("Wendy", "Worker", new BigDecimal("100.00"));
	}

	@Test
	void transfer_withdrawsCreditsAndPersists() {
		// Arrange
		ArgumentCaptor<Transfer> captor = ArgumentCaptor.forClass(Transfer.class);
		Mockito.when(accountServiceMock.findAccountByAccountNumber(ACCOUNT1)).thenReturn(testAccount1);
		Mockito.when(accountServiceMock.findAccountByAccountNumber(ACCOUNT2)).thenReturn(testAccount2);
		testAccount1.setAccountNumber(ACCOUNT1);
		testAccount2.setAccountNumber(ACCOUNT2);

		// Act
		TransferCreatedResponse response = transferService.transfer(request);

		// Assert
		Mockito.verify(accountServiceMock).withdraw(testAccount1, new BigDecimal("50.00"));
		Mockito.verify(accountServiceMock).credit(testAccount2, new BigDecimal("50.00"));
		Mockito.verify(transferRepositoryMock, Mockito.times(1)).persist(captor.capture());
		assertEquals(ACCOUNT1, captor.getValue().getFromAccountNumber());
		assertEquals(ACCOUNT2, captor.getValue().getToAccountNumber());
		assertEquals(0, new BigDecimal("50.00").compareTo(captor.getValue().getAmount()));
	}

	@Test
	void transfer_sameAccount_throwsExceptionAndPerformsNoComputations() {
		// Arrange
		TransferRequest sameRequest = new TransferRequest(ACCOUNT1, ACCOUNT1, new BigDecimal("50.00"));

		// Act
		assertThrows(SameAccountTransferException.class, () -> transferService.transfer(sameRequest));

		// Assert
		Mockito.verify(accountServiceMock, Mockito.never()).withdraw(Mockito.any(), Mockito.any());
		Mockito.verify(accountServiceMock, Mockito.never()).credit(Mockito.any(), Mockito.any());
		Mockito.verifyNoInteractions(transferRepositoryMock);
	}

	@Test
	void transfer_insufficientFunds_propagatesAndPersistsNoRecord() {
		// Arrange
		Mockito.doThrow(new InsufficientFundsException("TEST")).when(accountServiceMock).withdraw(Mockito.any(), Mockito.any());
		Mockito.when(accountServiceMock.findAccountByAccountNumber(ACCOUNT1)).thenReturn(testAccount1);
		Mockito.when(accountServiceMock.findAccountByAccountNumber(ACCOUNT2)).thenReturn(testAccount2);

		// Act
		assertThrows(InsufficientFundsException.class, () -> transferService.transfer(request));

		// Assert
		Mockito.verify(accountServiceMock).withdraw(testAccount1, new BigDecimal("50.00"));
		Mockito.verify(accountServiceMock, Mockito.never()).credit(Mockito.any(), Mockito.any());
		Mockito.verify(transferRepositoryMock, Mockito.never()).persist(Mockito.any(Transfer.class));
	}

	@Test
	void transfer_unknownAccount_throwsAndPersistsNoRecord() {
		// Arrange
		Mockito.doThrow(new AccountNotFoundException("TEST")).when(accountServiceMock).findAccountByAccountNumber(ACCOUNT1);

		// Act
		assertThrows(AccountNotFoundException.class, () -> transferService.transfer(request));

		// Assert
		Mockito.verify(accountServiceMock, Mockito.never()).withdraw(Mockito.any(), Mockito.any());
		Mockito.verify(accountServiceMock, Mockito.never()).credit(Mockito.any(), Mockito.any());
		Mockito.verifyNoInteractions(transferRepositoryMock);
	}


}