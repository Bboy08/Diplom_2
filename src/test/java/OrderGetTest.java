import client.OrderApiClient;
import client.UserApiClient;
import io.restassured.response.Response;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static config.ConfigApp.*;
import static helper.UserGenerator.getRandomUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class OrderGetTest {
    CreateOrderRequest createOrderRequest;
    OrderApiClient orderApiClient;
    CreateUserRequest createUserRequest;
    LoginUserRequest loginUserRequest;
    UserApiClient userApiClient;

    @Before
    public void setup() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(BEEF_METEORITE);
        ingredients.add(CRATER_BUN);
        createOrderRequest = new CreateOrderRequest(ingredients);
        orderApiClient = new OrderApiClient();
        createUserRequest = getRandomUser();
        userApiClient = new UserApiClient();
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
    }

    @Test
    @DisplayName("Get Order from User with Authorization")
    @Description("This test verifies the functionality of retrieving an order from a user account with proper authorization.")
    public void orderGetFromUserWithAuthorization() {
        String accessToken = userApiClient.createUser(createUserRequest).body().jsonPath().getString("accessToken");
        orderApiClient.createOrderWithAuthorization(createOrderRequest, accessToken.substring(7));
        Response getResponse = orderApiClient.getOrderFromUser(accessToken.substring(7));
        assertEquals(SC_OK, getResponse.statusCode());
        GetOrderResponse getOrderResponse = getResponse.as(GetOrderResponse.class);
        assertTrue(getOrderResponse.success);
    }

    @Test
    @DisplayName("Get Order from User without Authorization")
    @Description("This test verifies the behavior when attempting to retrieve an order from a user account without proper authorization.")
    public void orderGetFromUserWithoutAuthorization() {
        Response getResponse = orderApiClient.getOrderWithoutAuthorization();
        assertEquals(SC_UNAUTHORIZED, getResponse.statusCode());
        GetOrderResponse getOrderResponse = getResponse.as(GetOrderResponse.class);
        assertFalse(getOrderResponse.success);
        userApiClient.createUser(createUserRequest);
    }

    @After
    public void cleanUp() {
        String accessToken = userApiClient.loginUser(loginUserRequest).body().jsonPath().getString("accessToken");
        userApiClient.deleteUser(accessToken.substring(7));
    }
}
