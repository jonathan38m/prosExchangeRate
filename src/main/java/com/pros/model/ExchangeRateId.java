package com.pros.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ExchangeRateId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fromCurrencyCode;
	
	private String toCurrencyCode;

	public ExchangeRateId() {
	}

	public ExchangeRateId(String fromCurrencyCode, String toCurrencyCode) {
		super();
		this.fromCurrencyCode = fromCurrencyCode;
		this.toCurrencyCode = toCurrencyCode;
	}

	public String getFromCurrencyCode() {
		return fromCurrencyCode;
	}

	public void setFromCurrencyCode(String fromCurrencyCode) {
		this.fromCurrencyCode = fromCurrencyCode;
	}

	public String getToCurrencyCode() {
		return toCurrencyCode;
	}

	public void setToCurrencyCode(String toCurrencyCode) {
		this.toCurrencyCode = toCurrencyCode;
	}
	
}
