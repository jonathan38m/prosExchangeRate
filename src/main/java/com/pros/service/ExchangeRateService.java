package com.pros.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pros.model.ExchangeRate;
import com.pros.model.ExchangeRateId;
import com.pros.model.ExchangeRateRepository;

@Service
public class ExchangeRateService {

	private final ExchangeRateRepository exchangeRateRepository;
	private List<ExchangeRate> allExchangeRates;

	@Autowired
	public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
		this.exchangeRateRepository = exchangeRateRepository;
	}

	public List<ExchangeRate> getExchangeRates() {
		return exchangeRateRepository.findAll();
	}

	public ResponseEntity<Object> save(ExchangeRate exchangeRate) {

		Optional<ExchangeRate> res = this.exchangeRateRepository.findById(exchangeRate.getId());
		List<ExchangeRate> allExchangeRates = this.getExchangeRates();
		List<ExchangeRate> list = allExchangeRates.stream()
				.filter(e -> e.getId().getFromCurrencyCode().equals(exchangeRate.getId().getToCurrencyCode())
						&& e.getId().getToCurrencyCode().equals(exchangeRate.getId().getFromCurrencyCode()))
				.toList();
		if (res.isPresent() || !list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		this.exchangeRateRepository.save(exchangeRate);
		return new ResponseEntity<>(exchangeRate, HttpStatus.CREATED);
	}

	public ResponseEntity<Object> findValue(ExchangeRateId exchangeRateId, BigDecimal value) {
		
		
		if (exchangeRateId.getFromCurrencyCode().equals(exchangeRateId.getToCurrencyCode())) {
			return new ResponseEntity<>(value + exchangeRateId.getFromCurrencyCode() + " -> " + value + exchangeRateId.getToCurrencyCode(), HttpStatus.OK);
		}
		BigDecimal aux = value.add(BigDecimal.ZERO);

		allExchangeRates = this.getExchangeRates();

		List<String> currencyReviewed = new ArrayList<>();
		currencyReviewed.add(exchangeRateId.getFromCurrencyCode());

		List<ExchangeRate> toCurrencyToList = allExchangeRates.stream()
				.filter(e -> e.getId().getToCurrencyCode().equals(exchangeRateId.getToCurrencyCode())).toList();

		List<ExchangeRate> fromCurrencyToList = allExchangeRates.stream()
				.filter(e -> e.getId().getFromCurrencyCode().equals(exchangeRateId.getToCurrencyCode())).toList();

		List<ExchangeRate> toCurrencyFromList = allExchangeRates.stream()
				.filter(e -> e.getId().getToCurrencyCode().equals(exchangeRateId.getFromCurrencyCode())).toList();

		List<ExchangeRate> fromCurrencyFromList = allExchangeRates.stream()
				.filter(e -> e.getId().getFromCurrencyCode().equals(exchangeRateId.getFromCurrencyCode())).toList();

		if ((toCurrencyToList.isEmpty() && fromCurrencyToList.isEmpty())
				|| (toCurrencyFromList.isEmpty() && fromCurrencyFromList.isEmpty())) {
			return new ResponseEntity<>("La moneda no existe", HttpStatus.CONFLICT);
		}
		List<ExchangeRate> currenciesToVisit = new ArrayList<>();
		currenciesToVisit.addAll(toCurrencyFromList);
		currenciesToVisit.addAll(fromCurrencyFromList);

//		getExhangeRequest("",currencyReviewed,value, exchangeRateId.getToCurrencyCode());

		BigDecimal result = null;
		for (ExchangeRate exchangeRate : currenciesToVisit) {

			String nextCurrency;
			if (exchangeRate.getId().getFromCurrencyCode().equals(exchangeRateId.getFromCurrencyCode())) {
				nextCurrency = exchangeRate.getId().getToCurrencyCode();
			} else {
				nextCurrency = exchangeRate.getId().getFromCurrencyCode();
			}

			result = getExhangeRequest(exchangeRate, nextCurrency, currencyReviewed, value,
					exchangeRateId.getToCurrencyCode());
			if (result != null) {
				return new ResponseEntity<>(aux + exchangeRateId.getFromCurrencyCode() + " -> " + result + exchangeRateId.getToCurrencyCode(), HttpStatus.OK);
			}

		}

		return new ResponseEntity<>("Error en el sistema", HttpStatus.CONFLICT);
	}

	public BigDecimal getExhangeRequest(ExchangeRate exchangeRate, String nextCurrency, List<String> currencyReviewed,
			BigDecimal value, String toCurrency) {

		

		currencyReviewed.add(nextCurrency);

		BigDecimal fromFactor = new BigDecimal(exchangeRate.getFromFactor())
				.movePointLeft(exchangeRate.getFromPresision());
		BigDecimal toFactor = new BigDecimal(exchangeRate.getToFactor()).movePointLeft(exchangeRate.getToPresision());

		if (exchangeRate.getId().getToCurrencyCode().equals(toCurrency)) {
			return value.multiply(toFactor).divide(fromFactor);
		} else if (exchangeRate.getId().getFromCurrencyCode().equals(toCurrency)) {
			return value.multiply(fromFactor).divide(toFactor);
		}
		
		String priviousCurrency;
		BigDecimal auxValue;
		if (exchangeRate.getId().getFromCurrencyCode().equals(nextCurrency)) {
			priviousCurrency = exchangeRate.getId().getToCurrencyCode();
			auxValue = value.multiply(fromFactor).divide(toFactor);
		} else {
			priviousCurrency = exchangeRate.getId().getFromCurrencyCode();
			auxValue = value.multiply(toFactor).divide(fromFactor);
		}

		List<ExchangeRate> currencyExcahngeList = allExchangeRates.stream()
				.filter(e -> e.getId().getToCurrencyCode().equals(nextCurrency)
						|| e.getId().getFromCurrencyCode().equals(nextCurrency))
				.toList();
		BigDecimal result = null;
		for (ExchangeRate auxExchangeRate : currencyExcahngeList) {
			if (auxExchangeRate.getId().getFromCurrencyCode().equals(priviousCurrency)
					|| auxExchangeRate.getId().getToCurrencyCode().equals(priviousCurrency)) {
				continue;
			}
			String auxNextCurrency;
			if (auxExchangeRate.getId().getFromCurrencyCode().equals(nextCurrency)) {
				auxNextCurrency = auxExchangeRate.getId().getToCurrencyCode();
			} else {
				auxNextCurrency = auxExchangeRate.getId().getFromCurrencyCode();
			}

			result = getExhangeRequest(auxExchangeRate, auxNextCurrency, currencyReviewed, auxValue, toCurrency);
			if (result != null) {
				return result;
			}

		}

		return result;
	}

	public List<ExchangeRate> getFromList(String currency) {

		return this.getExchangeRates().stream().filter(e -> e.getId().getFromCurrencyCode().equals(currency)).toList();
	}

	public List<ExchangeRate> getToList(String currency) {

		return this.getExchangeRates().stream().filter(e -> e.getId().getToCurrencyCode().equals(currency)).toList();
	}

}
