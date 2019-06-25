package com.mapt.modules;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mapt.MaptConfiguration;

import javax.inject.Named;

public class AWSModule implements Module
{
	@Override
    public void configure(Binder binder) {}

    @Provides
    @Named("awsSNS")
    public AmazonSNS provideSNS(MaptConfiguration configuration)
    {
    	System.setProperty("aws.accessKeyId", configuration.getAwsAccessKey());
    	System.setProperty("aws.secretKey", configuration.getAwsSecretKey());
    	
    	AmazonSNS client = AmazonSNSClient.builder()
    			.withRegion("us-east-1")
                .build();
    	
        return client;
    }
    
    @Provides
    @Named("topicArn")
    public String provideArn(MaptConfiguration configuration)
    {
    	return configuration.getTopicArn();
    }
}
