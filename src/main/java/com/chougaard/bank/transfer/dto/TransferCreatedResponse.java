package com.chougaard.bank.transfer.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferCreatedResponse(
		Long transferId,
		String fromAccountNumber,
		String toAccountNumber,
		BigDecimal amount,
		Instant timestamp,
		BigDecimal fromBalance,
		BigDecimal toBalance
) {}
