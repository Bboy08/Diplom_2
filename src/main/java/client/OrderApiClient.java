package client;

import io.restassured.response.Response;
import model.CreateOrderRequest;
import static config.ConfigApp.BASE_URL;

public class OrderApiClient extends BaseApiClient {
    public Response createOrder(CreateOrderRequest createOrderRequest) {
        return getPostSpec()
                .body(createOrderRequest)
                .when()
                .post(BASE_URL + "/api/orders");
    }

    public Response createOrderWithAuthorization(CreateOrderRequest createOrderRequest, String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .and()
                .body(createOrderRequest)
                .post(BASE_URL + "/api/orders");
    }

    public Response createOrderWithoutIngredients() {
        return getPostSpec()
                .when()
                .post(BASE_URL + "/api/orders");
    }


    public Response getOrderFromUser(String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .get(BASE_URL + "/api/orders");
    }

    public Response getOrderWithoutAuthorization() {
        return getPostSpec()
                .when()
                .get(BASE_URL + "/api/orders");
    }
}
