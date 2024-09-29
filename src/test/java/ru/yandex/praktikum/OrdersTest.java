package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class OrdersTest {

    private static final String ORDERS = "/api/v1/orders";

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Проверка наличия списка заказов в теле ответа")
    //В теле ответа возвращается список заказов
    public void ordersTest(){

        Response getOrders;
        getOrders = given()
                .contentType(ContentType.JSON)
                .get(ORDERS);

        Orders orders = getOrders.body().as(Orders.class);
        //Проверка, что в теле сообщения возвращается список заказов
        Assert.assertTrue(orders.getOrders().size() > 0);
    }
}