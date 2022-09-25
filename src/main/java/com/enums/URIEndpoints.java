package com.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum URIEndpoints {

	CURRENT_PRICE_ENDPOINT("CurrentData", "https://api.coindesk.com/v1/bpi/currentprice/eur.json"),

	HISTORICAL_PRICE_ENDPOINT("HistoricalData",
			"https://api.coindesk.com/v1/bpi/historical/close.json?start=2013-09-01&end=2013-09-05&currency=eur");

	private String type;
	private String endpoint;

	private URIEndpoints(String type, String endpoint) {
		this.endpoint = endpoint;
		this.type = type;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public String getType() {
		return type;
	}

	private static Map<String, URIEndpoints> map = new HashMap<>();

	static {
		Arrays.stream(URIEndpoints.values()).forEach(entry -> map.put(entry.getType(), entry));
	}

	public  static URIEndpoints resolveByType(String type) {
		
		return map.containsKey(type) ? map.get(type): null;

	}

}
