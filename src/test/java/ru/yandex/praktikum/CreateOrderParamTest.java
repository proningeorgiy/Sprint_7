package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderParamTest {

    private final List<String> color;

    private static final String ORDER = "/api/v1/orders";

    public CreateOrderParamTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GRAY")},
                {Arrays.asList("BLACK", "GRAY")},
                {Arrays.asList("")},
        };
    }

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка создания заказа")
    @Description("Проверка наличия номера заказа в теле ответа")
    //Создание заказа с параметрами
    public void orderTest(){
        CreateOrder createOrder = new CreateOrder("Вася", "Пупкин", "Москва, ул. Тестовая"
                , "4", "+7 499 123 45 67", 1, "2024-10-01", "Тестовый комментарий", color);

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrder.createOrder(ORDER);
        createOrder.showCreateOrderResponseData(createOrderResponse, "track");

        //Проверка, что тело ответа содержит track
        Assert.assertNotNull(createOrderResponse.path("track"));
    }
}