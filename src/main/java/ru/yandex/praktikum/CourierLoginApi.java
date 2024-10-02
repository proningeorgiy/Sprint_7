package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierLoginApi extends BaseApi {
    private CourierLogin courierLogin;

    public CourierLoginApi(CourierLogin courierLogin) {
        this.courierLogin = courierLogin;
    }

    public CourierLogin getCourierLogin() {
        return courierLogin;
    }

    public void setCourierLogin(CourierLogin courierLogin) {
        this.courierLogin = courierLogin;
    }

    public void showLoginCourierRequestData() {
        System.out.println("Авторизация курьера");
        System.out.println("Логин: " + this.courierLogin.getLogin());
        System.out.println("Пароль: " + this.courierLogin.getPassword());
        System.out.println();
    }

    public void showLoginCourierResponseData(Response response, String showResponseData) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showLoginCourierResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response loginCourier(String connectString) {
        showLoginCourierRequestData();

        setBaseURI();

        Response loginResponse;
        loginResponse = given()
                .contentType(ContentType.JSON)
                .body(this.courierLogin)
                .when()
                .post(connectString);

        return loginResponse;
    }
}
