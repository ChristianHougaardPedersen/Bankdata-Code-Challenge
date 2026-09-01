package com.chougaard.bank.exchange.dto;

import java.util.List;

public record HistoricalRatesResponse(List<YearlyRate> rates) {
}
