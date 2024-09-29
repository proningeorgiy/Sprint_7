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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@RunWith(Parameterized.class)
public class CourierWrongLoginOrPasswordParamTest {

    private final String login;
    private final String password;

    private static final String COURIER = "/api/v1/courier";
    int courierId;

    public CourierWrongLoginOrPasswordParamTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {"TestL202409261", "123456"},
                {"TestL202409262", "12345"},
        };
    }

    @Before
    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка авторизации на неверные реквизиты доступа")
    @Description("Проверка кода ответа и сообщения ответа, если переданы неверные реквизиты доступа")
    //Неверное значение логина или пароля
    public void wrongLoginOrPasswordTest(){
        Courier courier = new Courier("TestL202409261", "12345", "Vasja");
        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());
        CourierLogin courierLoginTest = new CourierLogin(login, password);

        //Создание курьера
        Response createResponse;
        createResponse = courier.createCourier(COURIER);
        courier.showCreateCourierResponseData(createResponse);

        //Логин курьера
        //Данные для последующего удаления записи
        Response loginResponse;
        loginResponse = courierLogin.loginCourier(COURIER+"/login");
        courierLogin.showLoginCourierResponseData(loginResponse);
        courierId = loginResponse.path("id");

        //Логин курьера
        //данные из проверяемых параметров
        Response loginResponseTest;
        loginResponseTest = courierLoginTest.loginCourier(COURIER+"/login");
        courierLoginTest.showLoginCourierResponseData(loginResponseTest, "message");

        //Проверка на правильный код ответа
        loginResponseTest.then().statusCode(SC_NOT_FOUND);
        //Проверка на правильное сообщение в ответе
        Assert.assertEquals("Учетная запись не найдена", loginResponseTest.path("message"));
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