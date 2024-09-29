package ru.yandex.praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier(String login) {
        this.login = randomLogin(login);
        this.password = "12345";
        this.firstName = "Vasja";
    }

    public String randomLogin(String firstPartLogin){
        return firstPartLogin + RandomStringUtils.randomAlphanumeric(6, 10);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void showCreateCourierRequestData(){
        System.out.println("Создание курьера");
        System.out.println("Логин: " + this.login);
        System.out.println("Пароль: " + this.password);
        System.out.println("Имя: " + this.firstName);
        System.out.println();
    }

    public void showCreateCourierResponseData(Response response, String showResponseData){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе для параметра \"" + showResponseData + "\": " + response.path(showResponseData).toString());
        System.out.println();
    }

    public void showCreateCourierResponseData(Response response){
        System.out.println("Код ответа: " + response.statusCode());
        System.out.println("Сообщение в ответе " + response.body().asString());
        System.out.println();
    }

    public Response createCourier(String connectString){
        showCreateCourierRequestData();

        Response createResponse;
        createResponse = given()
                .contentType(ContentType.JSON)
                .body(this)
                .when()
                .post(connectString);

        return createResponse;
    }
}
