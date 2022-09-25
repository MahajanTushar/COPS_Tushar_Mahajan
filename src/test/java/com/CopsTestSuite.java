package com;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.enums.URIEndpoints;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.svc.BitcoinPriceCalculatorSvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CopsTusharMahajanApplication.class )
@ActiveProfiles("test")
public class CopsTestSuite {

	private static final Gson gson = new Gson();
	
	@Autowired
	private BitcoinPriceCalculatorSvc bitcoinPriceCalculatorSvc;
	

	private static final String basePath = "src/test/resources/mocks";
	
	private static final Logger logger = LoggerFactory.getLogger(CopsTestSuite.class);

	 
	@Test
	void getCurrentBitcoinRate() throws UnsupportedEncodingException, IOException {

		Collection<File>files =  FileUtils.listFiles(new File(basePath), new String[] {"json"}, true);
		Map<String, JsonObject> dataMap = new HashMap<>();
		
		files.stream().forEach(entry -> {
			try {
			JsonElement fileData =	JsonParser.parseReader(new FileReader(entry));
			String absoluateName = entry.getName();
			String fileName = absoluateName.substring(0,absoluateName.lastIndexOf("."));
			URIEndpoints endpointType =  URIEndpoints.resolveByType(fileName);
			dataMap.put(endpointType.getType(), fileData.getAsJsonObject());
			
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			 
			}
			
		});
		
		assertEquals(bitcoinPriceCalculatorSvc.getCurrentBTCRate("USD", dataMap) , 19120.4218);

	}

}
