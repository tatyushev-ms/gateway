package com.efa;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class UriConfiguration {
    
    private String authServiceUri = "http://auth-service";
    
    private String orderServiceUri = "http://order-service/";
    
    public String getAuthServiceUri() {
        return authServiceUri;
    }
    
    public void setAuthServiceUri(String authServiceUri) {
        this.authServiceUri = authServiceUri;
    }
    
    public String getOrderServiceUri() {
        return orderServiceUri;
    }
    
    public void setOrderServiceUri(String orderServiceUri) {
        this.orderServiceUri = orderServiceUri;
    }
    
}
