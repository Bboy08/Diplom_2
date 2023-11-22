package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CreateOrderRequest;
import static config.ConfigApp.BASE_URL;

public class OrderApiClient extends BaseApiClient {
    @Step("Создать заказ")
    public Response createOrder(CreateOrderRequest createOrderRequest) {
        return getPostSpec()
                .body(createOrderRequest)
                .when()
                .post(BASE_URL + "/api/orders");
    }
    @Step("Создать заказ с авторизацией")
    public Response createOrderWithAuthorization(CreateOrderRequest createOrderRequest, String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .and()
                .body(createOrderRequest)
                .post(BASE_URL + "/api/orders");
    }
    @Step("Создать заказ без ингридиентов")
    public Response createOrderWithoutIngredients() {
        return getPostSpec()
                .when()
                .post(BASE_URL + "/api/orders");
    }

    @Step("Получить список заказа от пользователя")
    public Response getOrderFromUser(String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .get(BASE_URL + "/api/orders");
    }
    @Step("Получить список заказа без авторизации")
    public Response getOrderWithoutAuthorization() {
        return getPostSpec()
                .when()
                .get(BASE_URL + "/api/orders");
    }
}
