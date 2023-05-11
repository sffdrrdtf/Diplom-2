package ru.check_api.order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CreateOrderApi {
    public static final String CREATE_ORDERS_API = "api/orders";
    public static final String INGREDIENT_ORDERS_API = "api/ingredients/";

    @Step("Создание заказа")
    public static ValidatableResponse createOrder(CreateOrder createOrder, String bearerToken) {
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", bearerToken)
                .body(createOrder)
                .post(CREATE_ORDERS_API)
                .then();
    }

    @Step("Создание заказа с ингредиентами")
    public static ValidatableResponse createIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .get(INGREDIENT_ORDERS_API)
                .then();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public ValidatableResponse createOrdersWithAuth(String bearerToken) {
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", bearerToken)
                .get(CREATE_ORDERS_API)
                .then();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public ValidatableResponse createOrdersWithoutAuth() {
        return given()
                .contentType(ContentType.JSON)
                .get(CREATE_ORDERS_API)
                .then();
    }
}
