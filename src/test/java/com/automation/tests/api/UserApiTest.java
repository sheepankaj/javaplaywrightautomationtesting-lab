package com.automation.tests.api;

import com.automation.api.UserApiClient;
import com.automation.api.models.User;
import com.automation.config.AppConfig;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("JSONPlaceholder API")
@Feature("User API")
public class UserApiTest {

    private static final Logger log = LoggerFactory.getLogger(UserApiTest.class);

    private UserApiClient userApiClient;
    private User user;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        userApiClient = new UserApiClient();
        int userId = AppConfig.getApiTestUserId();
        log.info("Fetching user ID {} for API tests", userId);
        user = userApiClient.getUserModelById(userId);
        log.info("User fetched: {}", user);
    }

    @Test(groups = "api", description = "Verify user ID=2 returns HTTP 200")
    @Story("Get User By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /users/2 returns a 200 OK response")
    public void testGetUserReturns200() {
        Response response = userApiClient.getUserById(AppConfig.getApiTestUserId());

        assertThat(response.statusCode())
                .as("HTTP status should be 200 OK")
                .isEqualTo(200);

        log.info("HTTP 200 OK verified for user ID=2");
    }

    @Test(groups = "api", description = "Verify user ID matches requested ID")
    @Story("Get User By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the returned user has the correct ID")
    public void testUserIdIsCorrect() {
        assertThat(user.getId())
                .as("User ID should match requested ID")
                .isEqualTo(AppConfig.getApiTestUserId());

        log.info("User ID validated: {}", user.getId());
    }

    @Test(groups = "api", description = "Verify user name for ID=2")
    @Story("User Data Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the full name of user ID=2 matches expected value")
    public void testUserName() {
        String expectedName = AppConfig.getExpectedUserName();

        assertThat(user.getName())
                .as("User name should match expected value")
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedName);

        log.info("Name validated — expected: '{}', actual: '{}'", expectedName, user.getName());
    }

    @Test(groups = "api", description = "Verify username for ID=2")
    @Story("User Data Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the username of user ID=2 matches expected value")
    public void testUserUsername() {
        String expectedUsername = AppConfig.getExpectedUsername();

        assertThat(user.getUsername())
                .as("Username should match expected value")
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(expectedUsername);

        log.info("Username validated — expected: '{}', actual: '{}'", expectedUsername, user.getUsername());
    }

    @Test(groups = "api", description = "Verify email for ID=2")
    @Story("User Data Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the email of user ID=2 matches expected value")
    public void testUserEmail() {
        String expectedEmail = AppConfig.getExpectedEmail();

        assertThat(user.getEmail())
                .as("Email should match expected value")
                .isNotNull()
                .isNotEmpty()
                .isEqualToIgnoringCase(expectedEmail);

        log.info("Email validated — expected: '{}', actual: '{}'", expectedEmail, user.getEmail());
    }

    @Test(groups = "api", description = "Verify user has non-null address")
    @Story("User Data Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the user object contains a valid address")
    public void testUserHasAddress() {
        assertThat(user.getAddress())
                .as("User should have an address")
                .isNotNull();

        assertThat(user.getAddress().getCity())
                .as("Address should have a city")
                .isNotNull()
                .isNotEmpty();

        log.info("User address validated: {}", user.getAddress());
    }

    @Test(groups = "api", description = "Verify all users endpoint returns list")
    @Story("Get All Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users returns a non-empty list with status 200")
    public void testGetAllUsersReturnsNonEmptyList() {
        Response response = userApiClient.getAllUsers();

        assertThat(response.statusCode())
                .as("Status should be 200 OK")
                .isEqualTo(200);

        assertThat(userApiClient.getAllUsersAsList())
                .as("Users list should not be empty")
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(1);

        log.info("All users endpoint validated");
    }
}
