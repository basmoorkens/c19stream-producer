package com.basm.c19streamer;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.spi.ILoggingEvent;

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

	public static byte[] convertILoggingEventToJsonBytesArray(ILoggingEvent event) {
		try {
			return jsonMapper.writeValueAsBytes(event);
		} catch (IOException e) {
			return null;
		}
	}

}
