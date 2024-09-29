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

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CourierCreateParamTest {

    private final String login;
    private final String password;
    private final String firstName;

    private static final String COURIER = "/api/v1/courier";

    public CourierCreateParamTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {"TestL1", "", ""},
                {"", "12345", ""},
        };
    }

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка создания курьера, если одно из обязательных полей пустое")
    @Description("Проверка кода ответа и сообщения ответа, когда логин или пароль пустые")
    //Если одного из полей нет, запрос возвращает ошибку
    public void withoutLoginOrPasswordTest(){
        Courier courier = new Courier(login, password, firstName);

        Response createResponse;
        createResponse = courier.createCourier(COURIER);

        courier.showCreateCourierResponseData(createResponse, "message");

        //Проверка на правильный код ответа
        createResponse.then().statusCode(SC_BAD_REQUEST);
        //Проверка на правильное сообщение в ответе
        Assert.assertEquals("Недостаточно данных для создания учетной записи", createResponse.path("message"));
    }
}