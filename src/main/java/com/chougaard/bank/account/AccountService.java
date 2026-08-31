package com.chougaard.bank.account;

import com.chougaard.bank.account.dto.*;
import com.chougaard.bank.common.exception.AccountNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@ApplicationScoped
public class AccountService {

	private static final SecureRandom RANDOM = new SecureRandom();

	private final AccountRepository accountRepository;


	@Inject
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Transactional
	public AccountResponse createAccount(CreateAccountRequest accountRequest) {
		Account account = new Account(accountRequest.firstName(), accountRequest.lastName(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN));
		account.setAccountNumber(generateAccountNumber());
		accountRepository.persist(account);

		return mapToAccountResponse(account);
	}

	public AccountResponse getAccountByAccountNumber(String accountNumber) {
		Account account = findAccountByAccountNumber(accountNumber);
		return mapToAccountResponse(account);
	}

	@Transactional
	public AccountResponse deposit(String accountNumber, DepositRequest request) {
		Account account = findAccountByAccountNumber(accountNumber);
		account.setBalance(account.getBalance().add(request.amount()));
		return mapToAccountResponse(account);
	}

	private String generateAccountNumber() {
		long number = RANDOM.nextLong(1_000_000_000L, 10_000_000_000L);
		return String.valueOf(number);
	}

	private Account findAccountByAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("Requested account not found: " + accountNumber));
	}

	private AccountResponse mapToAccountResponse(Account account) {
		return new AccountResponse(account.getAccountNumber(), account.getOwner(), account.getBalance());
	}
}
