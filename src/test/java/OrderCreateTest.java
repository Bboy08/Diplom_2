import client.OrderApiClient;
import client.UserApiClient;
import io.restassured.response.Response;
import model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static config.ConfigApp.*;
import static helper.UserGenerator.getRandomUser;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class OrderCreateTest {
    CreateOrderRequest createOrderRequest;
    CreateOrderRequest createOrderWithInvalidHashRequest;
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
        List<String> ingredientsInvalidHash = new ArrayList<>();
        ingredientsInvalidHash.add(INVALID_HASH);
        createOrderWithInvalidHashRequest = new CreateOrderRequest(ingredientsInvalidHash);
        orderApiClient = new OrderApiClient();
        createUserRequest = getRandomUser();
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
        userApiClient = new UserApiClient();
    }

    @Test
    @DisplayName("Create Order with Authorization")
    @Description("This test verifies the functionality of creating an order with proper authorization.")
    public void orderCreateWithAuthorization() {
        String accessToken = userApiClient.createUser(createUserRequest).body().jsonPath().getString("accessToken");
        Response createResponse = orderApiClient.createOrderWithAuthorization(createOrderRequest, accessToken.substring(7));
        assertEquals(SC_OK, createResponse.statusCode());
        CreateOrderResponse createOrderResponse = createResponse.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.success);
        userApiClient.deleteUser(accessToken.substring(7));
    }

    @Test
    @DisplayName("Create Order without Authorization")
    @Description("This test verifies the behavior when attempting to create an order without proper authorization.")
    public void orderCreateWithoutAuthorization() {
        Response createResponse = orderApiClient.createOrder(createOrderRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateOrderResponse createOrderResponse = createResponse.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.success);
    }

    @Test
    @DisplayName("Create Order without Ingredients")
    @Description("This test verifies the behavior when attempting to create an order without specifying any ingredients.")
    public void orderCreateWithoutIngredients() {
        Response createResponse = orderApiClient.createOrderWithoutIngredients();
        assertEquals(SC_BAD_REQUEST, createResponse.statusCode());
        CreateOrderResponse createOrderResponse = createResponse.as(CreateOrderResponse.class);
        assertFalse(createOrderResponse.success);
    }

    @Test
    @DisplayName("Create Order with Invalid Hash")
    @Description("This test verifies the behavior when attempting to create an order with an invalid hash.")
    public void orderCreateWithInvalidHash() {
        Response createResponse = orderApiClient.createOrder(createOrderWithInvalidHashRequest);
        assertEquals(SC_INTERNAL_SERVER_ERROR, createResponse.statusCode());
    }
}
