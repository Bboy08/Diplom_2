import client.UserApiClient;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginUserRequest;
import model.LoginUserResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static helper.UserGenerator.getRandomUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTest {
    CreateUserRequest createUserRequest;
    UserApiClient userApiClient;
    LoginUserRequest loginUserRequest;
    @Before
    public void setup() {
        createUserRequest = getRandomUser();
        userApiClient = new UserApiClient();
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
    }
    @Test
    public void userLogin() {
        userApiClient.createUser(createUserRequest);
        Response loginResponse = userApiClient.loginUser(loginUserRequest);
        assertEquals(SC_OK, loginResponse.statusCode());
        LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
        assertTrue(loginUserResponse.success);
    }

    @Test
    public void userLoginWithIncorrectLogAndPass() {
        Response loginResponse = userApiClient.loginUser(loginUserRequest);
        assertEquals(SC_UNAUTHORIZED, loginResponse.statusCode());
        LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
        assertFalse(loginUserResponse.success);
        userApiClient.createUser(createUserRequest);
    }

    @After
    public void cleanUp() {
        String accessToken = userApiClient.loginUser(loginUserRequest).body().jsonPath().getString("accessToken");
        userApiClient.deleteUser(accessToken.substring(7));
    }
}
