package com.dh.accountservice.RestAssure;
import com.dh.accountservice.entities.AccountDTO;
import com.dh.accountservice.entities.AccountTransactionsDTO;
import com.dh.accountservice.entities.Transaction;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.repository.IAccountRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.List;



public class TestAccountsRestAssured {
    String baseUrl="http://localhost:8084/";
    @Autowired
    IAccountRepository repository;


    @Test
    public void testFindTransactionsByAccountId(){
        String accountId = "1";
        String endpointGetFindTransactionsByAccountId="accounts/"+accountId+"/transactions";
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AccountTransactionsDTO account = response.as(AccountTransactionsDTO.class);
        Assertions.assertEquals(Integer.parseInt(accountId), account.getId());

        List<Transaction>  transactions = account.getTransactions();
        Assertions.assertTrue(transactions.size()>0 && transactions.size()<6);
    }
    @Test
    public void testFailFindTransactionsByAccountId(){
        String accountId = "88888";
        String endpointGetFindTransactionsByAccountId="accounts/"+accountId+"/transactions";
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);

    }
    @Test
    public void testFindAccountById(){
        String accountId = "1";
        String endpointGetFindTransactionsByAccountId="accounts/"+accountId;
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AccountDTO account = response.as(AccountDTO.class);
        Assertions.assertEquals(Integer.parseInt(accountId), account.getId());

    }
    @Test
    public void testFailFindAccountById(){
        String accountId = "8888";
        String endpointGetFindTransactionsByAccountId="accounts/"+accountId;
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);

    }
    @Test
    public void testFindAccountByUserId(){
        String userId = "1";
        String endpointGetFindTransactionsByAccountId="accounts/user/"+userId;
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AccountDTO account = response.as(AccountDTO.class);
        Assertions.assertEquals(Integer.parseInt(userId), account.getUser_id());
    }
    @Test
    public void testFailFindAccountByUserId(){
        String userId = "88888";
        String endpointGetFindTransactionsByAccountId="accounts/user/"+userId;
        String url = baseUrl+ endpointGetFindTransactionsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }
    @Test
    public void testCreateAccount() throws BadRequestException, IOException {

        String endpointCreateAccount = "accounts";
        String url= baseUrl+endpointCreateAccount;

        String requestBody= "{\"cvu\": \"121212222\", \"user_id\": 1}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

    }
    @Test
    public void testFailCreateAccountByUserId() throws BadRequestException, IOException {
        String endpointCreateAccount = "accounts";
        String url= baseUrl+endpointCreateAccount;

        String requestBody= "{\"cvu\": \"123456789\", \"user_id\": 1}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400,statusCode);

        String responseBody=response.getBody().asString();
        String errorMsg="We can´t create the user account because the user associated in the account its already in use ";
        Assertions.assertEquals(errorMsg,responseBody);
    }
    @Test
    public void testFailCreateAccountByCvu() throws BadRequestException, IOException {
        String endpointCreateAccount = "accounts";
        String url= baseUrl+endpointCreateAccount;

        String requestBody= "{\"cvu\": \"121212222\", \"user_id\": 88888}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400,statusCode);

        String responseBody=response.getBody().asString();
        String errorMsg="We can´t create the user account because the cvu associated in the account its already in use ";
        Assertions.assertEquals(errorMsg,responseBody);
    }
    @Test
    public void testPatchAccount () throws IOException, BadRequestException{
        String accountId ="1";
        String endpointPatchAccount="accounts/"+accountId ;
        String url = baseUrl+ endpointPatchAccount;

        Response response = RestAssured.patch(url);

        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AccountDTO updatedAccount = response.as(AccountDTO.class);
        Assertions.assertEquals(accountId, String.valueOf(updatedAccount.getId()));

    }
    @Test
    public void testFailPatchAccount () throws IOException, BadRequestException {
        String accountId ="888888";
        String endpointPatchAccount="accounts/"+accountId ;
        String url = baseUrl+ endpointPatchAccount;

        Response response = RestAssured.patch(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400,statusCode);

        String expectedErrorMessage = "We can´t update the user account because the user don´t exist";
        String actualErrorMessage = response.getBody().asString();
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage);
    }



}
