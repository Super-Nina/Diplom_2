package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static data.OrderData.ORDER_CREATE_ENDPOINT;
import static io.restassured.RestAssured.given;


public class OrderSteps {
    @Step("Создание заказа с авторизацией и c ингредиентами")
    public static Response createOrderWithAuthAndIngredients(String token) {
        String json ="{\"ingredients\":[\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6f\"]}";
        return given()
                .log().all()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }

    @Step("Создание заказа с авторизацией и c неверным хешем ингредиентов")
    public static Response createOrderWithAuthAndWithWrongHash(String token) {
        String json ="{\"ingredients\":[\"61c0c5a71d1f82001bdaaa6d__12\", \"61c0c5a71d1f82001bdaaa6f__12\"]}";
        return given()
                .log().all()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }

    @Step("Создание заказа с авторизацией и без ингредиентов")
    public static Response createOrderWithAuthButWithoutIngredients(String token) {
       return given()
                .log().all()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }

    @Step("Создание заказа без авторизации c ингредиентами")
    public static Response createOrderWithoutAuth() {
        String json ="{\"ingredients\":[\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6f\"]}";
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post(ORDER_CREATE_ENDPOINT);
    }
}


