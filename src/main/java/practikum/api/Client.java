package practikum.api;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static practikum.configuration.Env.BASE_URL;

public class Client {
    private static final String PATH_CREATE = "/api/auth/register";
    private static final String PATH_LOGIN = "/api/auth/login";
    private static final String PATH_USER = "/api/auth/user";

    @Step("Создать пользователя")
    public ValidatableResponse createUserRequest(Users user) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + PATH_CREATE)
                .then();
    }

    @Step("Логин")
    public ValidatableResponse loginUserRequest(Login loginUser) {
        return given()
                .contentType(ContentType.JSON)
                .body(loginUser)
                .when()
                .post(BASE_URL + PATH_LOGIN)
                .then();
    }

    @Step("Обновить пользователя")
    public ValidatableResponse updateUserRequest(Users user, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(BASE_URL + PATH_USER)
                .then();
    }

    @Step("Удалить пользователя")
    public ValidatableResponse deleteUserRequest(String token) {
        return given()
                .auth().oauth2(token)
                .when()
                .delete(BASE_URL + PATH_USER)
                .then();
    }
}
