package com.pros.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pros.model.ExchangeRate;
import com.pros.model.ExchangeRateId;
import com.pros.service.ExchangeRateService;

@RestController
@RequestMapping(path = "/exchangeRates")
public class ExchangeRateController {

	private ExchangeRateService exchangeRateService;

	@Autowired
	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}
	
	@GetMapping
	public List<ExchangeRate> getExchangeRates(){
		return this.exchangeRateService.getExchangeRates();
	}
	
	@PostMapping
	public ResponseEntity<Object> registryExchangeRate(@RequestBody ExchangeRate exchangeRate){
		return this.exchangeRateService.save(exchangeRate);
	}
	
	@PostMapping( path = "/changecurrency")
	public ResponseEntity<Object> changeCurrency(@RequestBody Map<String, ?> requestParams){
		
		ExchangeRateId exchangeRateId = new ExchangeRateId(requestParams.get("fromCurrencyCode").toString(),requestParams.get("toCurrencyCode").toString());
		
		BigDecimal value = BigDecimal.valueOf(Double.valueOf(requestParams.get("value").toString()));
		return this.exchangeRateService.findValue(exchangeRateId, value);
	}
	
	
}
