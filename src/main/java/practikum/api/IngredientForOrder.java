package practikum.api;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static practikum.configuration.Env.BASE_URL;

public class IngredientForOrder {
    private static final String PATH_INGREDIENTS = "/api/ingredients";

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredientsRequest() {
        return given()
                .when()
                .get(BASE_URL + PATH_INGREDIENTS)
                .then();
    }
}