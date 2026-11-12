package com.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geo {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("lng")
    private String lng;

    public String getLat() { return lat; }
    public String getLng() { return lng; }

    @Override
    public String toString() {
        return "Geo{lat='" + lat + "', lng='" + lng + "'}";
    }
}
