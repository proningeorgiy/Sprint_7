package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrder {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> colorList;

    public CreateOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> colorList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.colorList = colorList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getColorList() {
        return colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
    }

    public void showCreateOrderRequestData(){
        System.out.println("Создание заказа");
        System.out.println("Имя: " + this.firstName);
        System.out.println("Фамилия: " + this.lastName);
        System.out.println("Адрес: " + this.address);
        System.out.println("Метро: " + this.metroStation);
        System.out.println("Телефон: " + this.phone);
        System.out.println("Дней аренды: " + this.rentTime);
        System.out.println("Дата доставки: " + this.deliveryDate);
        System.out.println("Комментарий: " + this.comment);
        System.out.println("Список цветов окраски: " + this.colorList);
        System.out.println();
    }

    public void showCreateOrderResponseData(Response response, String showResponseData){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showCreateOrderResponseData(Response response){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createOrder(String connectString){
        showCreateOrderRequestData();

        Response createOrder;
        createOrder = given()
                .contentType(ContentType.JSON)
                .body(this)
                .when()
                .post(connectString);

        return createOrder;
    }
}
