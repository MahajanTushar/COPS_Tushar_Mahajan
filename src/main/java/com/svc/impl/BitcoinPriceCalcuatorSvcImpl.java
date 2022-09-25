package com.svc.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enums.URIEndpoints;
import com.exceptions.CurrencyNotConfiguredException;
import com.google.gson.JsonObject;
import com.svc.BitcoinPriceCalculatorSvc;
import com.utils.Utility;

@Component
public class BitcoinPriceCalcuatorSvcImpl implements BitcoinPriceCalculatorSvc {

	private static final Logger logger = LoggerFactory.getLogger(BitcoinPriceCalcuatorSvcImpl.class);

	@Autowired
	private Utility utility;

	@Override
	public Double getCurrentBTCRate(String requestedCurrency, Map<String, JsonObject> data)
			throws CurrencyNotConfiguredException {

		JsonObject currentPricingData = data.get(URIEndpoints.CURRENT_PRICE_ENDPOINT.getType());
		JsonObject bpiData = currentPricingData.get("bpi").getAsJsonObject();
		if (bpiData.has(requestedCurrency)) {
			JsonObject bitcoinStats = bpiData.get(requestedCurrency).getAsJsonObject();
			Double btcPrice = Double.valueOf(bitcoinStats.get("rate").getAsString().replace(",", ""));
			logger.info("the current price configured for bitcoin in {} is {}", requestedCurrency, btcPrice);
			return btcPrice;
		} else {
			throw new CurrencyNotConfiguredException("currency not configured");
		}

	}

	@Override
	public Double getLowestBTCRate(String requestedCurrency, Map<String, JsonObject> data)
			throws CurrencyNotConfiguredException {

		List<Double> btcRates = getBTCRate(requestedCurrency, data);
		logger.info("lowest BTC value in {} currency in 90 days duration is {}", requestedCurrency, btcRates.get(0));
		return btcRates.get(0);
	}

	@Override
	public Double getHighestBTCRate(String requestedCurrency, Map<String, JsonObject> data)
			throws CurrencyNotConfiguredException {
		List<Double> btcRates = getBTCRate(requestedCurrency, data);
		Double largetBtcRate = btcRates.get(btcRates.size() - 1);
		logger.info("highest BTC value in {} currency in 90 days duration is {}", requestedCurrency, largetBtcRate);
		return largetBtcRate;
	}

	private List<Double> getBTCRate(String requestedCurrency, Map<String, JsonObject> data) {
		JsonObject historicalBpiData = data.get(URIEndpoints.HISTORICAL_PRICE_ENDPOINT.getType());
		JsonObject dateAndPricingObject = historicalBpiData.get("bpi").getAsJsonObject();
		JsonObject currentPricingData = data.get(URIEndpoints.CURRENT_PRICE_ENDPOINT.getType());
		JsonObject bpiData = currentPricingData.get("bpi").getAsJsonObject();
		Map<String, String> currencyRateCurrentMap = new HashMap<>();
		List<Double> btcList = new ArrayList<>();

		bpiData.entrySet().forEach(entry -> currencyRateCurrentMap.put(entry.getKey(),
				entry.getValue().getAsJsonObject().get("rate").getAsString()));
		if (!bpiData.has(requestedCurrency)) {
			throw new CurrencyNotConfiguredException(
					"currency value is not configured in the data " + requestedCurrency);
		}

		dateAndPricingObject.entrySet().forEach(entry -> {

			LocalDate bpiDate = LocalDate.now();
			Period diffPeriod = Period.between(LocalDate.now(), bpiDate);
			int diffDays = diffPeriod.getDays();

			if (diffDays < 90) {
				Double conversionFactor = utility.getCurrencyConvertedPrice(requestedCurrency.toUpperCase(),
						currencyRateCurrentMap);
				btcList.add(conversionFactor * entry.getValue().getAsDouble());
			}
		});

		Collections.sort(btcList);

		return btcList;
	}

}
