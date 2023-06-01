package com.dh.userservice.RestAssured;

import com.dh.userservice.entities.Account;
import com.dh.userservice.entities.AppUserResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUsersRestAssured {
    String baseUrl = "http://localhost:8080/";
    @Test
    public void testFindUserById(){
        String userId = "1";
        String endpointGetUserById="users/"+userId;
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AppUserResponseDTO user = response.as(AppUserResponseDTO.class);
        Assertions.assertEquals(Integer.parseInt(userId), user.getId());
    }
    @Test
    public void testFailFindUserById(){
        String userId = "8888";
        String endpointGetUserById="users/"+userId;
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }
    @Test
    public void testFindUserByEmail(){
        String userEmail = "MIGUELMORENO@GMAIL.COM";
        String endpointGetUserById="users/email/"+userEmail;
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        AppUserResponseDTO user = response.as(AppUserResponseDTO.class);
        Assertions.assertEquals(userEmail, user.getEmail());

    }
    @Test
    public void testFailFindUserByEmail(){
        String userEmail = "FAILTEST@GMAIL.COM";
        String endpointGetUserById="users/email/"+userEmail;
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }
    @Test
    public void testFindUserWithAccount(){
        String userId = "1";
        String endpointGetUserById="users/"+userId+"/account";
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        JsonPath jsonPath = response.getBody().jsonPath();
        Account account = jsonPath.getObject("account", Account.class);
        Assertions.assertNotNull(account);

    }
    @Test
    public void testFailFindUserWithAccount(){
        String userId = "888888";
        String endpointGetUserById="users/"+userId+"/account";
        String url = baseUrl+ endpointGetUserById;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404,statusCode);
    }

    @Test
    public void testCreateUser () {
        String endpointCreateAccount = "users";
        String url= baseUrl+endpointCreateAccount;

        String firstName="miguel";
        String lastName="moreno";
        String dni ="12121212";
        String email="popkop@gmail.com";
        String phone="213123133";
        String password = "password";

        String requestBody= "{\"first_name\": \""+firstName+"\",\"last_name\": \""+lastName+"\"," +
                "\"dni\": \""+dni+"\",\"email\": \""+email+"\",\"phone\": \""+phone+"\",\"password\": \""+password+"\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);

        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        String responseBody = response.getBody().asString();
        Assertions.assertTrue(responseBody.contains(firstName));
        Assertions.assertTrue(responseBody.contains(lastName));
        Assertions.assertTrue(responseBody.contains(dni));
        Assertions.assertTrue(responseBody.contains(email));
        Assertions.assertTrue(responseBody.contains(phone));

        Long id = response.jsonPath().get("id");
        //todo delete
    }
    @Test
    public void testPatchUser () {
        String endpointCreateAccount = "users";
        String url= baseUrl+endpointCreateAccount;

        String id= "2";
        String firstName="miguel";
        String lastName="moreno";
        String dni ="12121212";
        String email="popkop@gmail.com";
        String phone="213123133";
        String password = "password";

        String requestBody= "{\"id\": \""+id+"\",\"first_name\": \""+firstName+"\",\"last_name\": \""+lastName+"\"," +
                "\"dni\": \""+dni+"\",\"email\": \""+email+"\",\"phone\": \""+phone+"\",\"password\": \""+password+"\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch(url);

        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200,statusCode);

        String responseBody = response.getBody().asString();
        Assertions.assertTrue(responseBody.contains(id));
        Assertions.assertTrue(responseBody.contains(firstName));
        Assertions.assertTrue(responseBody.contains(lastName));
        Assertions.assertTrue(responseBody.contains(dni));
        Assertions.assertTrue(responseBody.contains(email));
        Assertions.assertTrue(responseBody.contains(phone));
    }

    @Test
    public void testFailPatchUser () {
        String endpointCreateAccount = "users";
        String url= baseUrl+endpointCreateAccount;

        String id= "888888";
        String firstName="miguel";
        String lastName="moreno";
        String dni ="12121212";
        String email="popkop@gmail.com";
        String phone="213123133";
        String password = "password";

        String requestBody= "{\"id\": \""+id+"\",\"first_name\": \""+firstName+"\",\"last_name\": \""+lastName+"\"," +
                "\"dni\": \""+dni+"\",\"email\": \""+email+"\",\"phone\": \""+phone+"\",\"password\": \""+password+"\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .patch(url);

        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400,statusCode);

        String responseBody = response.getBody().asString();
        String expectedError = "We can´t update the user because the user don´t exist ";
        Assertions.assertEquals(expectedError,responseBody);

    }
}
