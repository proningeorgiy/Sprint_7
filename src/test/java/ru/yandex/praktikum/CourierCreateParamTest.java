package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@RunWith(Parameterized.class)
public class CourierCreateParamTest {

    private static final String COURIER = "/api/v1/courier";
    private final String login;
    private final String password;
    private final String firstName;

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

    @Test
    @DisplayName("Проверка создания курьера, если одно из обязательных полей пустое")
    @Description("Проверка кода ответа и сообщения ответа, когда логин или пароль пустые")
    //Если одного из полей нет, запрос возвращает ошибку
    public void withoutLoginOrPasswordTest() {

        Courier courier = new Courier(login, password, firstName);

        Response createResponse;
        createResponse = createCourierWithResponse(courier);

        //Проверка на правильный код ответа
        checkResponseCode(createResponse, SC_BAD_REQUEST);

        //Проверка на правильное сообщение в ответе
        checkResponseText(createResponse, "Недостаточно данных для создания учетной записи", "message");
    }

    @Step("Создание курьера")
    public Response createCourierWithResponse(Courier courier){
        CourierApi courierApi = new CourierApi(courier);

        Response Response;
        Response = courierApi.createCourier(COURIER);
        courierApi.showCreateCourierResponseData(Response, "message");

        return Response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode){
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на правильное сообщение в ответе")
    public void checkResponseText(Response response, String expected, String actual){
        Assert.assertEquals(expected, response.path(actual));
    }
}