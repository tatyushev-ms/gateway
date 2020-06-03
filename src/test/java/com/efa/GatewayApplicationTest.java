package com.efa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "authServiceUri=http://localhost:${wiremock.server.port}",
                "orderServiceUri=http://localhost:${wiremock.server.port}"
        })
@AutoConfigureWireMock(port = 0)
class GatewayApplicationTest {
    
    @Autowired
    private WebTestClient client;
    
    @Test
    void shouldRouteOrdersRequests() {
        stubFor(get(urlEqualTo("/api/v1/orders/"))
                .willReturn(aResponse()
                        .withBody("[{\"id\":\"f3863\",\"accountNumber\":\"a857c\",\"orderStatus\":\"PENDING\"}]")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        
        client.get().uri("/api/v1/orders/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("f3863");
    }
    
    @Test
    void shouldRouteInvoicesRequests() {
        stubFor(get(urlEqualTo("/api/v1/invoices/"))
                .willReturn(aResponse()
                        .withBody("[{\"id\":\"7gs23\",\"accountNumber\":\"a857c\",\"invoiceStatus\":\"CREATED\"}]")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        
        client.get().uri("/api/v1/invoices/")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("7gs23");
    }
    
    @Test
    public void shouldRouteAuthServiceRequests() {
        stubFor(get(urlEqualTo("/oauth/.well-known/openid-configuration"))
                .willReturn(aResponse()
                        .withBody("{\"issuer\":\"test\"}")
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        
        client.get().uri("/oauth/.well-known/openid-configuration")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.issuer").isEqualTo("test");
    }
    
}
