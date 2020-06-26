package com.basm.c19streamer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonReader {
	
	@Value("${json.file}")
	private String fileName;
	
	public List<CovidEntry> readCovidEntries() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<CovidEntry> covidEntries = mapper.readValue(new File(fileName), new TypeReference<List<CovidEntry>>() {});
		System.out.println("Found " + covidEntries.size() + " records to insert into kinesis");
		return covidEntries;
	}
}
