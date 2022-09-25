package com.events;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.enums.URIEndpoints;
import com.google.gson.JsonObject;
import com.svc.BitcoinPriceCalculatorSvc;
import com.svc.impl.DataExtractor;

@Component
@Profile("dev")
public class EventListener implements ApplicationListener<ApplicationStartedEvent>{
	
	@Autowired
	private BitcoinPriceCalculatorSvc bitcoinPriceCalculatorSvc;
	
	@Autowired
	private DataExtractor dataExtractor;

	@Override
	public void onApplicationEvent(final ApplicationStartedEvent event) {
		 
		Scanner scanner  = new Scanner(System.in);
		System.out.println("give currency value");
		String currency= scanner.nextLine();
		JsonObject historicalData= dataExtractor.getDataForURL(URIEndpoints.HISTORICAL_PRICE_ENDPOINT.getEndpoint());
		JsonObject currentData = dataExtractor.getDataForURL(URIEndpoints.CURRENT_PRICE_ENDPOINT.getEndpoint());
		Map<String, JsonObject> dataMap = new HashMap<>();
		dataMap.put(URIEndpoints.HISTORICAL_PRICE_ENDPOINT.getType(), historicalData);
		dataMap.put(URIEndpoints.CURRENT_PRICE_ENDPOINT.getType(), currentData);
		
		String currencyCode = currency.toUpperCase();

		bitcoinPriceCalculatorSvc.getCurrentBTCRate(currencyCode,dataMap);
		
		bitcoinPriceCalculatorSvc.getLowestBTCRate(currencyCode, dataMap);
		
		bitcoinPriceCalculatorSvc.getHighestBTCRate(currencyCode, dataMap);
	}

	 
 
}
