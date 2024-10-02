package ru.yandex.praktikum;

import io.restassured.RestAssured;

public class BaseApi {

    public void setBaseURI() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
