package com.dh.cardsservice.restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCard {

    String baseUrl = "http://localhost:8085/";
    @Test
    public void testDeleteCardById(){
        Integer carId = 6;
        String endpointDeleteCardById="cards/"+carId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);
    }

    @Test
    public void testDeleteFailedCardById(){
        Integer carId = 60;
        String endpointDeleteCardById="cards/"+carId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }
}
