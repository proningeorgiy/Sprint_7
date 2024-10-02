package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

@RunWith(Parameterized.class)
public class CourierEmptyLoginOrPasswordParamTest {

    private static final String COURIER = "/api/v1/courier";
    private final String login;
    private final String password;
    int courierId;

    public CourierEmptyLoginOrPasswordParamTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {"TestL202409261", ""},
                {"", "12345"},
        };
    }

    @Test
    @DisplayName("Проверка авторизации если логин или пароль пустые")
    @Description("Проверка кода ответа и сообщения ответа, если передан пустой логин или пароль")
    //Если какого-то поля нет, запрос возвращает ошибку
    public void withoutLoginOrPasswordTest() {

        Courier courier = new Courier("TestL202409261", "12345", "Vasja");

        CourierLogin courierLogin = new CourierLogin(courier.getLogin(), courier.getPassword());

        CourierLogin courierLoginTest = new CourierLogin(login, password);

        //Создание курьера
        createCourier(courier);

        //Логин курьера
        //Данные для последующего удаления записи
        Response loginResponse;
        loginResponse = loginCourier(courierLogin);
        courierId = loginResponse.path("id");

        //Логин курьера
        //данные из проверяемых параметров
        Response loginResponseTest;
        loginResponseTest = loginCourierTest(courierLoginTest);

        //Проверка на правильный код ответа
        checkResponseCode(loginResponseTest, SC_BAD_REQUEST);
        //Проверка на правильное сообщение в ответе
        checkResponseText(loginResponseTest, "Недостаточно данных для входа", "message");
    }

    @Step("Создание курьера")
    public void createCourier(Courier courier){
        CourierApi courierApi = new CourierApi(courier);

        Response createResponse;
        createResponse = courierApi.createCourier(COURIER);
        courierApi.showCreateCourierResponseData(createResponse, "ok");
    }

    @Step ("Получение данных для последующего удаления записи о курьере")
    public Response loginCourier(CourierLogin courierLogin){

        CourierLoginApi courierLoginApi = new CourierLoginApi(courierLogin);

        Response Response;
        Response = courierLoginApi.loginCourier(COURIER + "/login");
        courierLoginApi.showLoginCourierResponseData(Response);

        return Response;
    }

    @Step ("Логин под тестируемыми значениями")
    public Response loginCourierTest(CourierLogin courierLogin){

        CourierLoginApi courierLoginApi = new CourierLoginApi(courierLogin);

        Response Response;
        Response = courierLoginApi.loginCourier(COURIER + "/login");
        courierLoginApi.showLoginCourierResponseData(Response, "message");

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

    @After
    public void deleteCourier() {
        if (courierId != 0)
            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(COURIER + "/" + courierId);
    }
}