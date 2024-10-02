package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi extends BaseApi {
    private Courier courier;

    public CourierApi(Courier courier) {
        this.courier = courier;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public void showCreateCourierRequestData() {
        System.out.println("Создание курьера");
        System.out.println("Логин: " + this.courier.getLogin());
        System.out.println("Пароль: " + this.courier.getPassword());
        System.out.println("Имя: " + this.courier.getFirstName());
        System.out.println();
    }

    public void showCreateCourierResponseData(Response response, String showResponseData) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showCreateCourierResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createCourier(String connectString) {
        showCreateCourierRequestData();

        setBaseURI();

        Response createResponse;
        createResponse = given()
                .contentType(ContentType.JSON)
                .body(this.courier)
                .when()
                .post(connectString);

        return createResponse;
    }
}
