package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class CourierTest {

    private static final String COURIER = "/api/v1/courier";
    int courierId;

    @Test
    @DisplayName("Создание курьера и авторизация")
    @Description("Проверка кода ответа и сообщения ответа при создании курьера и авторизации")
    //Курьера можно создать, курьер может авторизоваться
    public void createCourierAndAuthorizeTest() {

        Courier courier = new Courier("TestL1", "12345", "Vasja");

        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());

        //Создание курьера
        //чтобы создать курьера, в ручку передаются все обязательные поля
        Response createResponse;
        createResponse = createCourierWithResponse(courier);

        //Создание курьера - проверка на правильный код ответа
        checkResponseCode(createResponse, SC_CREATED);
        //Создание курьера - проверка, что успешный запрос возвращает true
        checkResponseTextTrue(createResponse, "ok");

        //Логин курьера
        //для авторизации передаются все обязательные поля
        Response loginResponse;
        loginResponse = loginCourier(courierLogin);

        //Логин курьера - проверка на правильный код ответа
        checkResponseCode(loginResponse, SC_OK);

        //Логин курьера - проверка, что успешный запрос возвращает id
        courierId = loginResponse.path("id");
        //Assert.assertNotNull(courierId);
        checkResponseId(courierId);
    }

    @Test
    @DisplayName("Проверка, на то, что нельзя создать одинаковых курьеров")
    @Description("Проверка кода ответа, при попытке создать одинаковых курьеров вообще, и курьера с уже существующим логином")
    //Нельзя создать двух одинаковых курьеров
    public void createIdenticalCouriersTest() {

        Courier courier1 = new Courier("TestL1", "12345", "Vasja");
        Courier courier2 = new Courier("TestL1", "12345", "Vasja");
        CourierLogin courierLogin = new CourierLogin(courier1.getLogin(), courier1.getPassword());

        //Создание первого курьера
        createCourier(courier1);

        //Данные для последующего удаления записи
        courierId = loginCourier(courierLogin).path("id");;

        //Создание второго курьера
        Response createResponse;
        createResponse = createCourierWithResponse(courier2);

        //Проверка на правильный код ответа
        checkResponseCode(createResponse, SC_CONFLICT);
    }

    @Step ("Создание курьера")
    public void createCourier(Courier courier){
        CourierApi courierApi = new CourierApi(courier);

        Response createResponse;
        createResponse = courierApi.createCourier(COURIER);
        courierApi.showCreateCourierResponseData(createResponse, "ok");
    }

    @Step ("Логин под записью курьера")
    public Response loginCourier(CourierLogin courierLogin){

        CourierLoginApi courierLoginApi = new CourierLoginApi(courierLogin);

        Response Response;
        Response = courierLoginApi.loginCourier(COURIER + "/login");
        courierLoginApi.showLoginCourierResponseData(Response);

        return Response;
    }

    @Step ("Создание курьера")
    public Response createCourierWithResponse(Courier courier){
        CourierApi courierApi = new CourierApi(courier);

        Response Response;
        Response = courierApi.createCourier(COURIER);
        courierApi.showCreateCourierResponseData(Response);

        return Response;
    }

    @Step("Проверка на правильный код ответа")
    public void checkResponseCode(Response response, int statusCode){
        response.then().statusCode(statusCode);
    }

    @Step("Проверка на наличие правильного значения в ответе")
    public void checkResponseTextTrue(Response response, String responseText){
        Assert.assertTrue(response.path(responseText));
    }

    @Step("Проверка что ответ возвращает значение")
    public void checkResponseId(int responseCourierId){
        Assert.assertNotNull(responseCourierId);
    }

    @After
    public void deleteCourier() {
        if (courierId != 0)
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(COURIER + "/" + courierId);
    }
}