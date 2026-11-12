package com.automation.api;

import com.automation.config.AppConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseApiClient {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiClient.class);
    protected final RequestSpecification requestSpec;
    protected final ResponseSpecification responseSpec;

    protected BaseApiClient() {
        RestAssured.baseURI = AppConfig.getApiBaseUrl();

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();

        log.info("API client initialized with base URI: {}", AppConfig.getApiBaseUrl());
    }
}
