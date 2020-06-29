package com.basm.c19streamer;


import java.nio.ByteBuffer;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class KinesisLogAppender extends AppenderBase<ILoggingEvent>{

	private String streamName; 
	
	private AmazonKinesis kinesisClient;
	
	private PutRecordRequest request;
	
	public KinesisLogAppender() {
		kinesisClient = Util.getKinesisClient(Util.getAWSCredentialsFromEnv(), Regions.EU_WEST_1);
	}
	
	@Override
	protected void append(ILoggingEvent eventObject) {
		request = new PutRecordRequest();
		request.setStreamName(streamName);
		request.setData(ByteBuffer.wrap(JsonUtil.convertILoggingEventToJsonBytesArray(eventObject)));
		request.setPartitionKey(eventObject.getLevel().levelStr);
		kinesisClient.putRecord(request);
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

}
