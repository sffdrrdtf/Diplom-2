package api_test.orders_test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.check_api.order.CreateOrder;
import ru.check_api.order.CreateOrderApi;
import ru.check_api.user.CreateUser;
import ru.check_api.user.GeneratorRandom;
import ru.check_api.user.User;

import static org.hamcrest.CoreMatchers.is;

public class OrdersTest {
    CreateUser createUser;
    CreateOrder createOrder;
    private User user;
    private CreateOrderApi createOrderApi;
    private String bearerToken;

    @Before
    public void setUp() {
        user = new User();
        createUser = new GeneratorRandom().getCreateUser();
        createOrderApi = new CreateOrderApi();
        createOrder = new CreateOrder();
    }

    @Test
    @DisplayName("Получение заказа авторизованного пользователя: эндпоинт api/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void getOrdersUserWithAuthTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        User.requestCreateLogin(createUser);
        CreateOrderApi.createOrder(createOrder, bearerToken);
        ValidatableResponse responseOrdersUser = createOrderApi.createOrdersWithAuth(bearerToken);
        responseOrdersUser.assertThat().statusCode(HttpStatus.SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя: эндпоинт api/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void getOrdersUserWithoutAuthTest() {
        ValidatableResponse response = user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseOrders = createOrderApi.createOrdersWithoutAuth();
        responseOrders.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED).body("success", is(false))
                .and().body("message", is("You should be authorised"));
    }

    @After
    public void tearDown() {
        user.userDelete(bearerToken);
    }
}
