package com.pros.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class ExchangeRate {

	@EmbeddedId
	private ExchangeRateId id;

	private Long fromFactor;

	private int fromPresision;

	private Long toFactor;

	private int toPresision;

	public ExchangeRate() {
	}

	public ExchangeRate(ExchangeRateId id, Long fromFactor, int fromPresision, Long toFactor, int toPresision) {
		super();
		this.id = id;
		this.fromFactor = fromFactor;
		this.fromPresision = fromPresision;
		this.toFactor = toFactor;
		this.toPresision = toPresision;
	}

	public ExchangeRateId getId() {
		return id;
	}

	public void setId(ExchangeRateId id) {
		this.id = id;
	}

	public Long getFromFactor() {
		return fromFactor;
	}

	public void setFromFactor(Long fromFactor) {
		this.fromFactor = fromFactor;
	}

	public int getFromPresision() {
		return fromPresision;
	}

	public void setFromPresision(int fromPresision) {
		this.fromPresision = fromPresision;
	}

	public Long getToFactor() {
		return toFactor;
	}

	public void setToFactor(Long toFactor) {
		this.toFactor = toFactor;
	}

	public int getToPresision() {
		return toPresision;
	}

	public void setToPresision(int toPresision) {
		this.toPresision = toPresision;
	}

}
