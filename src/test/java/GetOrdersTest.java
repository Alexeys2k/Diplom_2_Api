import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practikum.api.*;
import practikum.create.GenUsers;
import practikum.database.Ingredients;
import practikum.database.ListIng;
import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class GetOrdersTest {
    private Client userClient;
    private Users user;
    private Login login;
    private IngredientForOrder getIngredients;
    private Orders orderClient;
    private String token;
    private String bearerToken;
    private final static String ERROR_MESSAGE_NOT_AUTHORISED = "You should be authorised";

    @Before
    public void beforeCreateUserTest(){
        userClient = new Client();
        user = GenUsers.getSuccessCreateUser();
        login = new Login();

        getIngredients = new IngredientForOrder();
        orderClient = new Orders();

        ValidatableResponse responseCreate = userClient.createUserRequest(user);
        bearerToken = responseCreate.extract().path("accessToken");
        token = bearerToken.substring(7);

        userClient.loginUserRequest(Login.from(user));

        ValidatableResponse responseIngredients = getIngredients.getIngredientsRequest();
        Ingredients ingredient = responseIngredients.extract().body().as(Ingredients.class);
        ArrayList<Ingredients> list = ingredient.getData();
        ArrayList<String> listIngredients = new ArrayList<>();
        int max = list.size();
        for (int i = 0; i < max; i++)
        {
            listIngredients.add(list.get(i).get_id());
        }
        ListIng listIngredient = new ListIng(listIngredients);

        orderClient.createOrderRequest(listIngredient,token);
    }

    @After
    public void deleteUser() {
        if(token != null){
            userClient.deleteUserRequest(token);
        }
    }

    @Test
    @DisplayName("Check to get orders by user with login")
    @Description("Получение заказов конкретного пользователя с авторизацией")
    public void getOrdersUserWithLoginTest() {
        ValidatableResponse responseGetOrdersByUser = orderClient.getOrdersByUserRequest(token);
        int actualStatusCode = responseGetOrdersByUser.extract().statusCode();
        Boolean isOrdersGet  = responseGetOrdersByUser.extract().path("success");
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("Order is not created", isOrdersGet);
    }

    @Test
    @DisplayName("Check to get orders by user without login")
    @Description("Получение заказов конкретного пользователя без авторизации")
    public void getOrdersUserWithoutLoginTest() {
        ValidatableResponse responseGetOrdersByUser = orderClient.getOrdersByUserRequest("");
        int actualStatusCode = responseGetOrdersByUser.extract().statusCode();
        String actualMessage = responseGetOrdersByUser.extract().path("message");
        assertEquals("StatusCode is not 401", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Message is not correct", ERROR_MESSAGE_NOT_AUTHORISED, actualMessage);
    }
}