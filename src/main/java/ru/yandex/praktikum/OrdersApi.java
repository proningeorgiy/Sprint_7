package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersApi extends BaseApi {

    public Response getOrders(String connectString) {
        setBaseURI();

        Response getOrdersList;
        getOrdersList = given()
                .contentType(ContentType.JSON)
                .get(connectString);

        return getOrdersList;
    }
}
