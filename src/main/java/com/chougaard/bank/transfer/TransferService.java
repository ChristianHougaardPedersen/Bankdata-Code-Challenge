package com.chougaard.bank.transfer;

import com.chougaard.bank.account.Account;
import com.chougaard.bank.account.AccountService;
import com.chougaard.bank.common.exception.SameAccountTransferException;
import com.chougaard.bank.common.exception.TransferNotFoundException;
import com.chougaard.bank.transfer.dto.TransferCreatedResponse;
import com.chougaard.bank.transfer.dto.TransferRecordResponse;
import com.chougaard.bank.transfer.dto.TransferRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransferService {

	private final AccountService accountService;
	private final TransferRepository transferRepository;

	@Inject
	public TransferService(AccountService accountService, TransferRepository transferRepository) {
		this.accountService = accountService;
		this.transferRepository = transferRepository;
	}

	@Transactional
	public TransferCreatedResponse transfer(TransferRequest request) {
		if (request.fromAccountNumber().equals(request.toAccountNumber())) {
			throw new SameAccountTransferException("TO and FROM accounts identical. Operation aborted.");
		}

		Account fromAccount = accountService.findAccountByAccountNumber(request.fromAccountNumber());
		Account toAccount = accountService.findAccountByAccountNumber(request.toAccountNumber());

		accountService.withdraw(fromAccount, request.amount());
		accountService.credit(toAccount, request.amount());

		Transfer transfer = new Transfer(fromAccount.getAccountNumber(), toAccount.getAccountNumber(), request.amount());
		transferRepository.persist(transfer);

		return new TransferCreatedResponse(transfer.getId(), transfer.getFromAccountNumber(), transfer.getToAccountNumber(), transfer.getAmount(), transfer.getCreatedAt(), fromAccount.getBalance(), toAccount.getBalance());
	}

	public TransferRecordResponse getTransfer(Long id) {
		Transfer transfer = transferRepository.findByIdOptional(id).orElseThrow(() -> new TransferNotFoundException("Transfer not found: " + id));

		return new TransferRecordResponse(
				transfer.getId(),
				transfer.getFromAccountNumber(),
				transfer.getToAccountNumber(),
				transfer.getAmount(),
				transfer.getCreatedAt()
		);
	}
}
