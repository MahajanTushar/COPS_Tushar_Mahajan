package com.svc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.utils.Utility;

@Component
public class DataExtractor {
	
	@Autowired
	private Utility utility;
		
	public JsonObject getDataForURL(String url) {
		return utility.getApiData(url);
	}
}
