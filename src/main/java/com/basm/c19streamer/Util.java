package com.basm.c19streamer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;


public class Util {

	public static BasicAWSCredentials getAWSCredentialsFromEnv() { 
		return new BasicAWSCredentials(System.getenv("aws_access_key_id"),
				System.getenv("aws_secret_access_key"));	
	}
	
	public static AmazonKinesis getKinesisClient(BasicAWSCredentials credentials, Regions region) { 
		AmazonKinesisClientBuilder kinesisClientBuilder = AmazonKinesisClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region);
		return kinesisClientBuilder.build();
	}
	
}
