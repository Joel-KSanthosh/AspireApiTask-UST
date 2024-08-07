package com.aspire.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.aspire.api.event.ZonedTimeReadConverter;
import com.aspire.api.event.ZonedTimeWriteConverter;


@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "aspiredb";
    }
    
    // @Override
    // protected boolean autoIndexCreation(){
    //     return true;
    // }

    @Bean
    public MongoCustomConversions customConversions(){
        return new MongoCustomConversions(
            Arrays.asList(new ZonedTimeReadConverter(),new ZonedTimeWriteConverter())
        );
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(){
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    
}
