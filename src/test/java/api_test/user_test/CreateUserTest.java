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


public class CreateUserTest {
    private User createUser;
    CreateUser user;
    CreateUser inCorrectPass;

    private String bearerToken;

    @Before
    public void setUp() {
        createUser = new User();
        user = new GeneratorRandom().getCreateUser();
        inCorrectPass = new GeneratorRandom().getCreateUserWithoutPass();

    }

    @Test
    @DisplayName("Создание пользователя: эндпоинт создания /api/auth/register")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void createUserTest() {
        ValidatableResponse response = createUser.requestCreateUser(user);
        bearerToken = response.extract().path("accessToken");
        response.assertThat().statusCode(HttpStatus.SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован: эндпоинт /api/auth/register")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void requestCreateUserWithRepetitiveData() {
        ValidatableResponse responseUser1 = createUser.requestCreateUser(user);
        bearerToken = responseUser1.extract().path("accessToken");
        ValidatableResponse createResponse = createUser.requestCreateUser(user);
        createResponse.assertThat().statusCode(HttpStatus.SC_FORBIDDEN).body("success", is(false))
                .and().body("message", is("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без пароля: эндпоинт логина /api/auth/register")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void requestCreateUserWithoutPasswordTest() {
        ValidatableResponse responseUser1 = createUser.requestCreateUser(user);
        bearerToken = responseUser1.extract().path("accessToken");
        ValidatableResponse loginResponse = createUser.requestCreateUser(inCorrectPass);
        loginResponse.assertThat().statusCode(HttpStatus.SC_FORBIDDEN).body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        createUser.userDelete(bearerToken);
    }
}
