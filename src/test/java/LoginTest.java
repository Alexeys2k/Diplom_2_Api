
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practikum.api.Client;
import practikum.api.Login;
import practikum.api.Users;
import practikum.create.GenUsers;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class LoginTest {
    private Client userClient;
    private Users User;
    private Login login;
    private String token;
    private String bearerToken;
    private final static String ERROR_MESSAGE_INCORRECT_FIELD = "email or password are incorrect";

    @Before
    public void beforeCreateUserTest(){
        userClient = new Client();
        User = GenUsers.getSuccessCreateUser();
        login = new Login();

        ValidatableResponse responseCreate = userClient.createUserRequest(User);
        bearerToken = responseCreate.extract().path("accessToken");
        token = bearerToken.substring(7);
    }

    @After
    public void deleteUser() {
        if(token != null){
            userClient.deleteUserRequest(token);
        }
    }

    @Test
    @DisplayName("Check to login an existing user")
    @Description("логин под существующим пользователем")
    public void loginExistingUserTest(){
        ValidatableResponse responseLogin = userClient.loginUserRequest(Login.from(User));
        int actualStatusCode = responseLogin.extract().statusCode();
        Boolean isUserlogged = responseLogin.extract().path("success");
        assertEquals("StatusCode is not 200", SC_OK, actualStatusCode);
        assertTrue("User is not login", isUserlogged);
    }

    @Test
    @DisplayName("Check to login a user with invalid Email")
    @Description("логин с неверным логином")
    public void loginWithInvalidEmailTest(){
        User.setEmail("7777");
        ValidatableResponse responseLogin = userClient.loginUserRequest(Login.from(User));
        int actualStatusCode = responseLogin.extract().statusCode();
        String actualMessage = responseLogin.extract().path("message");
        assertEquals("StatusCode is not 403", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Message is not correct", ERROR_MESSAGE_INCORRECT_FIELD, actualMessage);
    }

    @Test
    @DisplayName("Check to login a user with invalid Password")
    @Description("логин с неверным паролем")
    public void loginWithInvalidPasswordTest(){
        User.setPassword("6666");
        ValidatableResponse responseLogin = userClient.loginUserRequest(Login.from(User));
        int actualStatusCode = responseLogin.extract().statusCode();
        String actualMessage = responseLogin.extract().path("message");
        assertEquals("StatusCode is not 403", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Message is not correct", ERROR_MESSAGE_INCORRECT_FIELD, actualMessage);
    }
}