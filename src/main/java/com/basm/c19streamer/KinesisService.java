package com.basm.c19streamer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.util.StringUtils;

@Component
public class KinesisService {

	private final static Logger log = LoggerFactory.getLogger(KinesisService.class);

	private final static String KINESIS_STREAM_NAME = "covid19-data";

	public void writeCovidEntriesToKinesisStream(List<CovidEntry> covidEntries) throws InterruptedException {
		preProcessRecords(covidEntries);
		// Get a configured kinesisclient for our env credentials and region eu-west-1
		AmazonKinesis kinesisClient = Util.getKinesisClient(Util.getAWSCredentialsFromEnv(), Regions.EU_WEST_1);
		PutRecordsRequest putRequest = new PutRecordsRequest();
		putRequest.setStreamName(KINESIS_STREAM_NAME);
		List<PutRecordsRequestEntry> kinesisEntries = new ArrayList<PutRecordsRequestEntry>();
		// we will count records that fail to insert
		int failedRecordsCounter = 0;
		int counter = 1;
		log.info("Writing " + covidEntries.size() + " covid records to stream " + KINESIS_STREAM_NAME);

		// loop over the entries we read from json
		for (CovidEntry covidEntry : covidEntries) {
			// if the entry doesnt have a date just discard it
			if (covidEntry.getDate() == null) {
				continue;
			}
			PutRecordsRequestEntry kinesisEntry = new PutRecordsRequestEntry();
			// kinesis expects its data as a byteBufferArray
			kinesisEntry.setData(ByteBuffer.wrap((JsonUtil.convertCovidEntryToJsonBytesArray(covidEntry))));
			kinesisEntry.setPartitionKey(covidEntry.getDate());
			kinesisEntries.add(kinesisEntry);
			// basic batching system, we create 500 records, push them to kinesis and then
			// wait 1s to make sure we do not hit the rate limit
			// the rate limit can be increased in kinesis ofc but this cost precious $$$
			if (counter % 500 == 0 || counter == covidEntries.size()) {
				putRequest.setRecords(kinesisEntries);
				PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRequest);
				log.info("Put Result" + putRecordsResult);
				failedRecordsCounter += putRecordsResult.getFailedRecordCount();
				Thread.sleep(1000);
				kinesisEntries.clear();
			}
			counter++;
		}
		log.info("Failed to write " + failedRecordsCounter + " records to the kinesis stream");
	}

	private void preProcessRecords(List<CovidEntry> entries) {
		for (CovidEntry entry : entries) {
			if (StringUtils.isNullOrEmpty(entry.getRegion())) {
				entry.setRegion("unknown");
			}
			if (StringUtils.isNullOrEmpty(entry.getProvince())) {
				entry.setProvince("unknown");
			}
		}
	}
}
