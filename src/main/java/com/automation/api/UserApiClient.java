package com.automation.api;

import com.automation.api.models.User;
import com.automation.constants.AppConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class UserApiClient extends BaseApiClient {

    public UserApiClient() {
        super();
    }

    public Response getUserById(int userId) {
        log.info("Fetching user with ID: {}", userId);
        return RestAssured
                .given(requestSpec)
                .pathParam("id", userId)
                .when()
                .get(AppConstants.USERS_ENDPOINT + "/{id}")
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    public User getUserModelById(int userId) {
        return getUserById(userId).as(User.class);
    }

    public Response getAllUsers() {
        log.info("Fetching all users");
        return RestAssured
                .given(requestSpec)
                .when()
                .get(AppConstants.USERS_ENDPOINT)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }

    public List<User> getAllUsersAsList() {
        return Arrays.asList(getAllUsers().as(User[].class));
    }
}
