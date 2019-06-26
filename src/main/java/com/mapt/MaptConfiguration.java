package com.mapt;

import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaptConfiguration extends Configuration implements AssetsBundleConfiguration
{
    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = AssetsConfiguration.builder().build();
    
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
    
    @Valid
    @NotNull
    @JsonProperty
    private String awsAccessKey = "";
    
    @Valid
    @NotNull
    @JsonProperty
    private String awsSecretKey = "";
    
    @Valid
    @NotNull
    @JsonProperty
    private String topicArn = "";
    
    
    @Override
    public AssetsConfiguration getAssetsConfiguration()
    {
    	return assets;
    }
    
    public DataSourceFactory getDataSourceFactory()
    {
        return database;
    }
    
    public String getAwsAccessKey()
    {
        return awsAccessKey;
    }
    
    public String getAwsSecretKey()
    {
        return awsSecretKey;
    }
    
    public String getTopicArn()
    {
        return awsSecretKey;
    }
}
