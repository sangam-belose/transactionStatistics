package com.n26.statistics.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public class Transaction {

	@NotNull
	private double amount = 0.0d;
	@NotNull
	@Range(max = Long.MAX_VALUE, min = Long.MIN_VALUE)
	private long timestamp=0L;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
