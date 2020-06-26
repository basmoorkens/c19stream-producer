package com.basm.c19streamer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
 
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private KinesisService kinesisService;
	
	
    @Override
    public void run(String...args) throws Exception {
    	List<CovidEntry> readCovidEntries = jsonReader.readCovidEntries();
    	kinesisService.writeCovidEntriesToKinesisStream(readCovidEntries);
    }
}