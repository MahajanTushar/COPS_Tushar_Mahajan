package com.svc;

import java.util.Map;

import com.google.gson.JsonObject;

public interface BitcoinPriceCalculatorSvc {
	
	public Double getCurrentBTCRate(String requestedCurrency, Map<String, JsonObject> data);
	
	public Double getLowestBTCRate(String requestedCurrency, Map<String, JsonObject> data);
	
	public Double getHighestBTCRate(String requestedCurrency, Map<String, JsonObject> data);
}
