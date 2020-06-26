package com.basm.c19streamer;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static ObjectMapper jsonMapper = new ObjectMapper();

	static {
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static byte[] convertCovidEntryToJsonBytesArray(CovidEntry entry) { 
        try {
            return jsonMapper.writeValueAsBytes(entry);
        } catch (IOException e) {
            return null;
        }
	}
	
}
