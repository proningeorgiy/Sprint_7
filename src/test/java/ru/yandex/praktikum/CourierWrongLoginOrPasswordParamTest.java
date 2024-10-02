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
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@RunWith(Parameterized.class)
public class CourierWrongLoginOrPasswordParamTest {

    private static final String COURIER = "/api/v1/courier";
    private final String login;
    private final String password;
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

    @Test
    @DisplayName("Проверка авторизации на неверные реквизиты доступа")
    @Description("Проверка кода ответа и сообщения ответа, если переданы неверные реквизиты доступа")
    //Неверное значение логина или пароля
    public void wrongLoginOrPasswordTest() {

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
        checkResponseCode(loginResponseTest, SC_NOT_FOUND);
        //Проверка на правильное сообщение в ответе
        checkResponseText(loginResponseTest, "Учетная запись не найдена", "message");
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