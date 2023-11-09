package client;


import io.restassured.response.Response;
import model.CreateUserRequest;
import model.LoginUserRequest;
import static config.ConfigApp.BASE_URL;


public class UserApiClient extends BaseApiClient {
    public Response createUser(CreateUserRequest createUserRequest) {
        return getPostSpec()
                .body(createUserRequest)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }

    public Response loginUser(LoginUserRequest loginUserRequest) {
        return getPostSpec()
                .body(loginUserRequest)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }

    public Response deleteUser(String accessToken) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .delete(BASE_URL + "/api/auth/user");
    }


    public Response dataChangeUser(String accessToken, CreateUserRequest createUserNewDataRequest) {
        return getPostSpec()
                .when()
                .auth().oauth2(accessToken)
                .and()
                .body(createUserNewDataRequest)
                .patch(BASE_URL + "/api/auth/user");
    }

    public Response dataChangeUserWithoutToken(CreateUserRequest createUserNewDataRequest) {
        return getPostSpec()
                .when()
                .and()
                .body(createUserNewDataRequest)
                .patch(BASE_URL + "/api/auth/user");
    }

}
