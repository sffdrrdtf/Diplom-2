package apitest.usertest;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.checkapi.user.CreateUser;
import ru.checkapi.user.GeneratorRandom;
import ru.checkapi.user.User;
import ru.checkapi.user.UserCreds;

import static org.hamcrest.CoreMatchers.is;

public class ChangeDataUserTest {
    CreateUser createUser;
    UserCreds userCreds;
    private User user;
    private String bearerToken;

    @Before
    public void setUp() {
        user = new User();
        createUser = new GeneratorRandom().getCreateUser();
        userCreds = new  UserCreds();
    }
    @Test
    @DisplayName("Изменение данных авторизованного пользователя:эндпоинт api/auth/user")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void changeUserTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseUpdate = user.changeDataUserWithAuth(userCreds.randomDataUser(), bearerToken);
        responseUpdate.assertThat().statusCode(HttpStatus.SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных незалогиненного пользователя:эндпоинт api/auth/user")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void changeUserWithoutAuthTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseUpdate = user.changeDataUserWithoutAuth(userCreds);
        responseUpdate.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message", is("You should be authorised"));
    }
    @After
    public void tearDown()
    {
        user.userDelete(bearerToken);
    }
}
