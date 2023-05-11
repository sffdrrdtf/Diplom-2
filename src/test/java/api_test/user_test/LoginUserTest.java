package api_test.user_test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.check_api.user.CreateUser;
import ru.check_api.user.GeneratorRandom;
import ru.check_api.user.User;

import static org.hamcrest.CoreMatchers.is;


public class LoginUserTest {

    CreateUser createUser;
    CreateUser inCorrectMail;
    private User user;

    private String bearerToken;

    @Before
    public void setUp() {
        user = new User();
        createUser = new GeneratorRandom().getCreateUser();
        inCorrectMail = new GeneratorRandom().getCreateLogin();
    }

    @Test
    @DisplayName("Создание логин под существующим пользователем: эндпоинт /api/auth/login")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void requestCreateLoginTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse loginResponse = User.requestCreateLogin(createUser);
        loginResponse.assertThat().statusCode(HttpStatus.SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание логин с неверным емаил и пароль: эндпоинт логина /api/auth/login")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void requestCreateUserWithoutLoginOrPasswordTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse loginResponse = User.requestCreateLogin(inCorrectMail);
        loginResponse.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message", is("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        user.userDelete(bearerToken);
    }
}

