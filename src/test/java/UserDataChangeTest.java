import client.UserApiClient;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.CreateUserResponse;
import model.LoginUserRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static helper.UserGenerator.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserDataChangeTest {
    CreateUserRequest createUserRequest;
    CreateUserRequest createUserRequestNewData;
    UserApiClient userApiClient;
    LoginUserRequest loginUserRequest;
    LoginUserRequest loginUserNewDataRequest;
    @Before
    public void setup() {
        createUserRequest = getRandomUser();
        createUserRequestNewData = getRandomUser();
        userApiClient = new UserApiClient();
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
        loginUserNewDataRequest = new LoginUserRequest(createUserRequestNewData.getEmail(), createUserRequestNewData.getPassword());
    }

    @Test
    public void userDataChange() {
        String accessToken = userApiClient.createUser(createUserRequest).body().jsonPath().getString("accessToken");
        Response dataChangeResponse = userApiClient.dataChangeUser(accessToken.substring(7), createUserRequestNewData);
        assertEquals(SC_OK, dataChangeResponse.statusCode());
        CreateUserResponse createUserResponse = dataChangeResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.success);
    }

    @Test
    public void userDataChangeUnauthorized() {
        userApiClient.createUser(createUserRequest);
        Response dataChangeResponse = userApiClient.dataChangeUserWithoutToken(createUserRequestNewData);
        assertEquals(SC_UNAUTHORIZED, dataChangeResponse.statusCode());
        CreateUserResponse createUserResponse = dataChangeResponse.as(CreateUserResponse.class);
        assertFalse(createUserResponse.success);
        String accessToken = userApiClient.loginUser(loginUserRequest).body().jsonPath().getString("accessToken");
        userApiClient.dataChangeUser(accessToken.substring(7), createUserRequestNewData);
    }

    @After
    public void cleanUp() {
        String accessToken = userApiClient.loginUser(loginUserNewDataRequest).body().jsonPath().getString("accessToken");
        userApiClient.deleteUser(accessToken.substring(7));
    }
}
