package practikum.api;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import practikum.database.ListIng;
import static io.restassured.RestAssured.given;
import static practikum.configuration.Env.BASE_URL;

public class Orders {
    private static final String PATH_ORDERS = "/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrderRequest(ListIng listIngredient, String token) {
        return given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(listIngredient)
                .when()
                .post(BASE_URL + PATH_ORDERS)
                .then();
    }

    @Step("Получение заказа пользователя")
    public ValidatableResponse getOrdersByUserRequest(String token) {
        return given()
                .auth().oauth2(token)
                .when()
                .get(BASE_URL + PATH_ORDERS)
                .then();
    }
}