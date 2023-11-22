import client.UserApiClient;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.CreateUserResponse;
import model.LoginUserRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static helper.UserGenerator.getRandomUser;
import static helper.UserGenerator.getRandomUserWithoutField;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;


public class UserCreateTest {
    CreateUserRequest createUserRequest;
    CreateUserRequest createUserWithoutFieldRequest;
    UserApiClient userApiClient;
    LoginUserRequest loginUserRequest;

    @Before
    public void setup() {
        createUserRequest = getRandomUser();
        createUserWithoutFieldRequest = getRandomUserWithoutField();
        userApiClient = new UserApiClient();
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
    }

    @Test
    @DisplayName("Create User")
    @Description("This test verifies the functionality of creating a new user.")
    public void userCreate() {
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Create User - Same User")
    @Description("This test verifies the behavior when attempting to create a user with the same information as an existing user.")
    public void userCreateTheSameUser() {
        userApiClient.createUser(createUserRequest);
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
    }

    @Test
    @DisplayName("Create User Without Required Field")
    @Description("This test verifies the behavior when attempting to create a user without providing a required field.")
    public void userCreateWithoutField() {
        Response createResponse = userApiClient.createUser(createUserWithoutFieldRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
        userApiClient.createUser(createUserRequest);
    }

    @After
    public void cleanUp() {
        String accessToken = userApiClient.loginUser(loginUserRequest).body().jsonPath().getString("accessToken");
        userApiClient.deleteUser(accessToken.substring(7));
    }

}
