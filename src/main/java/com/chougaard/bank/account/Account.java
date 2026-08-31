package com.chougaard.bank.account;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class Account extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifier;

	@Version
	private Long version;

	@Column(unique = true, nullable = false)
	private String accountNumber;

	@NotNull
	private String userFirstName;
	@NotNull
	private String userLastName;

	@Column(precision = 19, scale = 2)
	private BigDecimal balance;

	protected Account() {
		// Used by Hibernate
	}

	public Account(String userFirstName, String userLastName, BigDecimal balance) {
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.balance = balance;
	}

	public String getOwner() {
		return userFirstName + " " + userLastName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal newBalance) {
		balance = newBalance.setScale(2, RoundingMode.HALF_EVEN);
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
