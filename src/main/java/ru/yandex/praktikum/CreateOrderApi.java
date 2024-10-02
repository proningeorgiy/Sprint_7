package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CreateOrderApi extends BaseApi {

    private CreateOrder createOrder;

    public CreateOrderApi(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    public CreateOrder getCreateOrder() {
        return createOrder;
    }

    public void setCreateOrder(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    public void showCreateOrderRequestData() {
        System.out.println("Создание заказа");
        System.out.println("Имя: " + this.createOrder.getFirstName());
        System.out.println("Фамилия: " + this.createOrder.getLastName());
        System.out.println("Адрес: " + this.createOrder.getAddress());
        System.out.println("Метро: " + this.createOrder.getMetroStation());
        System.out.println("Телефон: " + this.createOrder.getPhone());
        System.out.println("Дней аренды: " + this.createOrder.getRentTime());
        System.out.println("Дата доставки: " + this.createOrder.getDeliveryDate());
        System.out.println("Комментарий: " + this.createOrder.getComment());
        System.out.println("Список цветов окраски: " + this.createOrder.getColorList());
        System.out.println();
    }

    public void showCreateOrderResponseData(Response response, String showResponseData) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showCreateOrderResponseData(Response response) {
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createOrder(String connectString) {
        showCreateOrderRequestData();

        setBaseURI();

        Response createOrder;
        createOrder = given()
                .contentType(ContentType.JSON)
                .body(this.createOrder)
                .when()
                .post(connectString);

        return createOrder;
    }

}
