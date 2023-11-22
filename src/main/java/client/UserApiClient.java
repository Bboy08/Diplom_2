package client;


import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginUserRequest;
import static config.ConfigApp.BASE_URL;


public class UserApiClient extends BaseApiClient {
    @Step("Создать пользователя")
    public Response createUser(CreateUserRequest createUserRequest) {
        return getPostSpec()
                .body(createUserRequest)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }
    @Step("Авторизоваться")
    public Response loginUser(LoginUserRequest loginUserRequest) {
        return getPostSpec()
                .body(loginUserRequest)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }
    @Step("Удалить пользователя")
    public Response deleteUser(String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .delete(BASE_URL + "/api/auth/user");
    }

    @Step("Изменить данные пользователя")
    public Response dataChangeUser(String accessToken, CreateUserRequest createUserNewDataRequest) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .and()
                .body(createUserNewDataRequest)
                .patch(BASE_URL + "/api/auth/user");
    }
    @Step("Изменить данные пользователя без токена")
    public Response dataChangeUserWithoutToken(CreateUserRequest createUserNewDataRequest) {
        return getPostSpec()
                .when()
                .and()
                .body(createUserNewDataRequest)
                .patch(BASE_URL + "/api/auth/user");
    }

}
