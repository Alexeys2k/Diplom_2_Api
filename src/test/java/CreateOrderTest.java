
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import practikum.api.*;
import practikum.create.GenUsers;
import practikum.database.Ingredients;
import practikum.database.ListIng;

public class CreateOrderTest {
    private Client userClient;
    private Users user;
    private Login login;
    private IngredientForOrder getIngredients;
    private Orders orderClient;
    private String token;
    private String bearerToken;

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
    }

    @After
    public void deleteUser() {
        if(token != null){
            userClient.deleteUserRequest(token);
        }
    }

    @Test
    @DisplayName("Check to create order with ingredients without login")
    @Description("Создание заказа с ингредиентами без авторизации")
    public void createOrderWithoutLoginTest() {
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

        ValidatableResponse responseCreateOrder = orderClient.createOrderRequest(listIngredient,"");
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        Boolean isOrderCreated = responseCreateOrder.extract().path("success");
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("Order is not created", isOrderCreated);
    }

    @Test
    @DisplayName("Check to create order with ingredients with login")
    @Description("Создание заказа с ингредиентами с авторизацией")
    public void createOrderWithLoginTest() {
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

        ValidatableResponse responseCreateOrder = orderClient.createOrderRequest(listIngredient,token);
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        Boolean isOrderCreated = responseCreateOrder.extract().path("success");
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("Order is not created", isOrderCreated);
    }

    @Test
    @DisplayName("Check to create order without ingredients")
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ArrayList<String> listString = new ArrayList<>();
        ListIng listIngredient = new ListIng(listString);

        ValidatableResponse responseCreateOrder = orderClient.createOrderRequest(listIngredient,token);
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        Boolean isOrderCreated = responseCreateOrder.extract().path("success");
        assertEquals("StatusCode is not 400", SC_BAD_REQUEST, actualStatusCode);
        assertFalse("Order is created", isOrderCreated);
    }

    @Test
    @DisplayName("Check to create order with incorrect hash of ingredients")
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectHashTest() {
        ValidatableResponse responseIngredients = getIngredients.getIngredientsRequest();
        Ingredients ingredient = responseIngredients.extract().body().as(Ingredients.class);
        ArrayList<Ingredients> list = ingredient.getData();
        ArrayList<String> listIngredients = new ArrayList<>();
        int max = list.size();
        Random random = new Random();
        for (int i = 0; i < max; i++)
        {
            listIngredients.add(list.get(i).get_id()+random.nextInt(50));
        }
        ListIng listIngredient = new ListIng(listIngredients);

        ValidatableResponse responseCreateOrder = orderClient.createOrderRequest(listIngredient,token);
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("StatusCode is not 500", SC_INTERNAL_SERVER_ERROR, actualStatusCode);
    }
}
