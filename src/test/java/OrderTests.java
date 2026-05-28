import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import steps.UserSteps;

import static data.OrderData.BASE_URI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTests extends BaseApiTest{
    private static UserModel userForAuth;
    protected static UserSteps userSteps;
    private static String token;

    @BeforeClass
    public static void startUp() {
        RestAssured.baseURI = BASE_URI;
        userForAuth = new UserModel("email" + System.currentTimeMillis() + "@test.com", "password", "name");
        userSteps = new UserSteps();
        token = userSteps.createUserAndGetToken(userForAuth);
    }
    @AfterClass
    public  static void cleanUp() {
        userSteps.deleteUser(token);
    }
    @Test
    @DisplayName("Создание заказа c авторизацией и с ингредиентами")
    @Description("Проверка, что можно создать заказ с авторизацией и ингредиентами")
    public void createOrderWithAuthAndWithIngredientsSuccess(){
        Response response = orderSteps.createOrderWithAuthAndIngredients(token);
        response.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.owner.email", equalTo(userForAuth.getEmail()));
    }

    @Test
    @DisplayName("Создание заказа c авторизацией и без ингредиентов")
    @Description("Проверка, что нельзя создать заказ с авторизацией, но без ингредиентов")
    public void createOrderWithAuthButWithoutIngredientsFails(){
        Response response = orderSteps.createOrderWithAuthButWithoutIngredients(token);
        response.then()
                .log().all()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c авторизацией и с неверным хешем ингредиентов)")
    @Description("Проверка, что нельзя создать заказ с авторизацией, но с неверным хешем ингредиентов")
    public void createOrderWithAuthButWithWrongHashIngredientsFails(){
        Response response = orderSteps.createOrderWithAuthAndWithWrongHash(token);
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации (с ингредиентами)")
    @Description("Проверка, что можно создать заказ без авторизации")
    public void createOrderWithoutAuthSuccess(){
        Response response = orderSteps.createOrderWithoutAuth();
        response.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
}
