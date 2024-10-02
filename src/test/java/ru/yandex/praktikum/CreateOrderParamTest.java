package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderParamTest {

    private static final String ORDER = "/api/v1/orders";
    private final List<String> color;

    public CreateOrderParamTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] requisites() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GRAY")},
                {Arrays.asList("BLACK", "GRAY")},
                {Arrays.asList("")},
        };
    }

    @Test
    @DisplayName("Проверка создания заказа")
    @Description("Проверка наличия номера заказа в теле ответа")
    //Создание заказа с параметрами
    public void orderTest() {

        CreateOrder createOrder = new CreateOrder("Вася", "Пупкин", "Москва, ул. Тестовая"
                , "4", "+7 499 123 45 67", 1, "2024-10-01", "Тестовый комментарий", color);

        //Создание заказа
        Response createOrderResponse;
        createOrderResponse = createOrder(createOrder);

        //Проверка, что тело ответа содержит track
        checkResponseTrack(createOrderResponse.path("track"));
    }

    @Step("Создание заказа")
    public Response createOrder(CreateOrder createOrder){
        CreateOrderApi createOrderApi = new CreateOrderApi(createOrder);

        Response createOrderResponse;
        createOrderResponse = createOrderApi.createOrder(ORDER);
        createOrderApi.showCreateOrderResponseData(createOrderResponse, "track");

        return createOrderResponse;
    }

    @Step("Проверка что ответ возвращает значение")
    public void checkResponseTrack(int responseOrderTrack){
        Assert.assertNotNull(responseOrderTrack);
    }
}