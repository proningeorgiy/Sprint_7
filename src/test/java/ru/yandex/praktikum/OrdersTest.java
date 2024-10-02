package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;

public class OrdersTest {

    private static final String ORDERS = "/api/v1/orders";

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Проверка наличия списка заказов в теле ответа")
    //В теле ответа возвращается список заказов
    public void ordersTest() {

        OrdersApi ordersApi = new OrdersApi();

        Orders orders = ordersApi.getOrders(ORDERS).body().as(Orders.class);
        //Проверка, что в теле сообщения возвращается список заказов
        Assert.assertTrue(orders.getOrders().size() > 0);
    }
}