package apitest.orderstest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.checkapi.order.CreateOrder;
import ru.checkapi.order.CreateOrderApi;
import ru.checkapi.user.CreateUser;
import ru.checkapi.user.GeneratorRandom;
import ru.checkapi.user.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    CreateUser createUser;
    CreateOrder createOrder;
     User user;
     CreateOrderApi createOrderApi;
    private String bearerToken;

    @Before
    public void setUp() {
        user = new User();
        createUser = new GeneratorRandom().getCreateUser();
        createOrderApi = new CreateOrderApi();
        createOrder = new CreateOrder();
    }

    @Test
    @DisplayName("Создание заказа: эндпоинт api/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void createOrderTest() {
        listIngredient();
        ValidatableResponse response =  user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        User.requestCreateLogin(createUser);
        ValidatableResponse responseCreateOrder = CreateOrderApi.createOrder(createOrder,bearerToken);
        responseCreateOrder.assertThat().statusCode(HttpStatus.SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без добавления ингредиентов: эндпоинт api/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void createOrderWithoutIngredientTest() {
        ValidatableResponse response =  user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = CreateOrderApi.createOrder(createOrder, bearerToken);
        responseCreateOrder.assertThat().statusCode(HttpStatus.SC_BAD_REQUEST).body("success", is(false))
                .and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов: эндпоинт api/orders")
    @Description("Проверка ожидаемого результата: statusCode и body")
    public void createOrderWithIncorrectHashIngredientTest() {
        listHashIngredient();
        ValidatableResponse response =  user.requestCreateUser(createUser);
        bearerToken = response.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = CreateOrderApi.createOrder(createOrder, bearerToken);
        responseCreateOrder.assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    private void listIngredient() {
        ValidatableResponse responseList = CreateOrderApi.createIngredients();
        List<String> list = responseList.extract().path("data._id");
        List<String> ingredients = createOrder.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(2));

    }

    private void listHashIngredient() {
        ValidatableResponse responseList = CreateOrderApi.createIngredients();
        List<String> list = responseList.extract().path("data._id");
        List<String> ingredients = createOrder.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(4).repeat(1));
        ingredients.add(list.get(2).repeat(2));
    }
    @After
    public void tearDown() {
        user.userDelete(bearerToken);
    }

}


