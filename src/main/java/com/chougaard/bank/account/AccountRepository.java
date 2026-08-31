package com.chougaard.bank.account;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

	public Optional<Account> findByAccountNumber(String accountNumber) {
		return find("accountNumber", accountNumber).firstResultOptional();
	}

	// inherits basic CRUD methods from PanacheEntityBase, so only "special" custom queries needs to be defined here
}
