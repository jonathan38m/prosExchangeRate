package com.pros.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRateId> {

	//Optional<ExchangeRate> findByFactor(ExchangeRateId exchangeRateId);
}
