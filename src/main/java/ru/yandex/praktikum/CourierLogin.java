package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void showLoginCourierRequestData(){
        System.out.println("Авторизация курьера");
        System.out.println("Логин: " + this.login);
        System.out.println("Пароль: " + this.password);
        System.out.println();
    }

    public void showLoginCourierResponseData(Response response, String showResponseData){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showLoginCourierResponseData(Response response){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response loginCourier(String connectString){
        showLoginCourierRequestData();

        Response loginResponse;
        loginResponse = given()
                .contentType(ContentType.JSON)
                .body(this)
                .when()
                .post(connectString);

        return loginResponse;
    }
}
