package com.basm.c19streamer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.util.StringUtils;


@Component
public class KinesisService {

	private final static String KINESIS_STREAM_NAME = "test";

	public void writeCovidEntriesToKinesisStream(List<CovidEntry> covidEntries) {
		preProcessRecords(covidEntries);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("aws_access_key_id"),
				System.getenv("aws_secret_access_key"));
		AmazonKinesis kinesisClient = AmazonKinesisClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.EU_WEST_1).build();
		PutRecordsRequest putRequest = new PutRecordsRequest();
		putRequest.setStreamName(KINESIS_STREAM_NAME);
		List<PutRecordsRequestEntry> kinesisEntries = new ArrayList<PutRecordsRequestEntry>();
		int counter = 1;
		System.out.println("Writing " + covidEntries.size() + " covid records to stream " + KINESIS_STREAM_NAME);
		for (CovidEntry covidEntry : covidEntries) {
			if(covidEntry.getDate()==null) {
				continue;
			}
			PutRecordsRequestEntry kinesisEntry = new PutRecordsRequestEntry();
			kinesisEntry.setData(ByteBuffer.wrap((JsonUtil.convertCovidEntryToJsonBytesArray(covidEntry))));
			kinesisEntry.setPartitionKey(covidEntry.getDate());
			kinesisEntries.add(kinesisEntry);
			if (counter % 100 == 0 || counter == covidEntries.size()) {
				putRequest.setRecords(kinesisEntries);
				PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRequest);
				System.out.println("Put Result" + putRecordsResult);
				kinesisEntries.clear();
			}
			counter++;
		}
	}
	
	private void preProcessRecords(List<CovidEntry> entries) {
		for(CovidEntry entry : entries) { 
			if(StringUtils.isNullOrEmpty(entry.getRegion())) {
				entry.setRegion("unknown");
			}
			if(StringUtils.isNullOrEmpty(entry.getProvince())) { 
				entry.setProvince("unknown");
			}
		}
	}
}
