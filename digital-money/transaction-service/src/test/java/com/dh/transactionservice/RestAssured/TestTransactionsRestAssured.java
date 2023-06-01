package com.dh.transactionservice.RestAssured;

import com.dh.transactionservice.entities.Transaction;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestTransactionsRestAssured {
    String baseUrl="http://localhost:8083/";
    @Test
    public void testGetTransactionsByAccountIdStatusCode(){
        String accountId = "1";
        String endpointGetTransactionsByAccountId= "transactions/"+accountId;
        String url= baseUrl + endpointGetTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        List<Transaction> transactions = response.jsonPath().getList(".", Transaction.class);
        Assertions.assertTrue(transactions.size()>0 && transactions.size()<6);
    }
    @Test
    public void testFailGetTransactionsByAccountIdStatusCode(){
        String accountId = "88888";
        String endpointGetTransactionsByAccountId= "transactions/"+accountId;
        String url= baseUrl + endpointGetTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        List<Transaction> transactions = response.jsonPath().getList(".", Transaction.class);
        Assertions.assertFalse(transactions.size()>0 && transactions.size()<6);
    }
}
