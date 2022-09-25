package com.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.enums.CurrencyCodes;
import com.exceptions.CurrencyNotConfiguredException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Component
public class Utility {

	@Autowired
	private RestTemplate restTemplate;

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public JsonObject getApiData(String url) {
		logger.info("requesting API data from {}", url);
		String result = restTemplate.getForObject(url, String.class);
		JsonObject data = gson.fromJson(result, JsonObject.class);
		return data;
	}

	public Double getCurrencyConvertedPrice(String destinationCurrency, Map<String, String> currencyRateCurrentMap) {

		logger.debug("converting currency from EUR to destination currency");
		boolean doesCurrencyExist = currencyRateCurrentMap.keySet().parallelStream()
				.filter(key -> key.contains(destinationCurrency)).findAny().isPresent() ? true : false;

		if (!doesCurrencyExist) {
			throw new CurrencyNotConfiguredException("currency not configured");
		}

		if (destinationCurrency.equals(CurrencyCodes.EUR.name())) {
			logger.debug("destination currency is EUR too, no conversion required");
			return 1.00;
		}

		String destinationCurrencyMapped = currencyRateCurrentMap.get(destinationCurrency).replace(",", "");

		Double destinationCurrencyRate = Double.valueOf(destinationCurrencyMapped);

		return destinationCurrencyRate
				/ Double.parseDouble(currencyRateCurrentMap.get(CurrencyCodes.EUR.name()).replace(",", ""));

	}
}
