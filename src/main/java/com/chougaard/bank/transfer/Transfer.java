package com.chougaard.bank.transfer;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Transfer extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fromAccountNumber;

	@Column(nullable = false)
	private String toAccountNumber;

	@Column(precision = 19, scale = 2, nullable = false)
	private BigDecimal amount;

	@CreationTimestamp
	private Instant createdAt;

	protected  Transfer() {}

	public Transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}
}
