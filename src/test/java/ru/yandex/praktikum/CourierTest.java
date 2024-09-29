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

public class CourierTest {

    private static final String COURIER = "/api/v1/courier";
    int courierId;

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера и авторизация")
    @Description("Проверка кода ответа и сообщения ответа при создании курьера и авторизации")
    //Курьера можно создать, курьер может авторизоваться
    public void createCourierAndAuthorizeTest(){
        Courier courier = new Courier("TestL1", "12345", "Vasja");
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());

        //Создание курьера
        //чтобы создать курьера, в ручку передаются все обязательные поля
        Response createResponse;
        createResponse = courier.createCourier(COURIER);
        courier.showCreateCourierResponseData(createResponse, "ok");

        //Создание курьера - проверка на правильный код ответа
        createResponse.then().statusCode(SC_CREATED);
        //Создание курьера - проверка, что успешный запрос возвращает true
        Assert.assertTrue(createResponse.path("ok"));

        //Логин курьера
        //для авторизации передаются все обязательные поля
        Response loginResponse;
        loginResponse = courierLogin.loginCourier(COURIER+"/login");
        courierLogin.showLoginCourierResponseData(loginResponse, "id");

        //Логин курьера - проверка на правильный код ответа
        loginResponse.then().statusCode(SC_OK);

        //Логин курьера - проверка, что успешный запрос возвращает id
        courierId = loginResponse.path("id");
        Assert.assertNotNull(courierId);
    }

    @Test
    @DisplayName("Проверка, на то, что нельзя создать одинаковых курьеров")
    @Description("Проверка кода ответа и сообщения ответа, при попытке создать одинаковых курьеров вообще, и курьера с уже существующим логином")
    //Нельзя создать двух одинаковых курьеров
    public void createIdenticalCouriersTest(){
        Courier courier1 = new Courier("TestL1", "12345", "Vasja");
        Courier courier2 = new Courier("TestL1", "12345", "Vasja");
        CourierLogin courierLogin = new CourierLogin(courier1.getLogin(), courier1.getPassword());

        //Создание первого курьера
        Response createResponse1;
        createResponse1 = courier1.createCourier(COURIER);
        courier1.showCreateCourierResponseData(createResponse1, "ok");

        //Данные для последующего удаления записи
        Response loginResponse;
        loginResponse = courierLogin.loginCourier(COURIER+"/login");
        courierLogin.showLoginCourierResponseData(loginResponse);
        courierId = loginResponse.path("id");

        //Создание второго курьера
        Response createResponse2;
        createResponse2= courier2.createCourier(COURIER);
        courier2.showCreateCourierResponseData(createResponse2);

        //Проверка на правильный код ответа
        createResponse2.then().statusCode(SC_CONFLICT);
    }

    @After
    public void deleteCourier(){
        if(courierId != 0)
            given()
                .contentType(ContentType.JSON)
                .when()
                .delete(COURIER + "/" + courierId);
    }
}