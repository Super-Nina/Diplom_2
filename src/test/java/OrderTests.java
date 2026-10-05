import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.UserModel;
import org.apache.http.HttpStatus;
import org.junit.*;
import steps.UserSteps;

import static data.OrderData.BASE_URI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTests extends BaseApiTest{
    private UserModel userForAuth;
    protected UserSteps userSteps;
    private String token;

    @Before
    public void startUp() {
        RestAssured.baseURI = BASE_URI;
        userForAuth = new UserModel("email" + System.currentTimeMillis() + "@test.com", "password", "name");
        userSteps = new UserSteps();
        token = userSteps.createUserAndGetToken(userForAuth);
    }
    @After
    public  void cleanUp() {
        userSteps.deleteUser(token);
    }
    @Test
    @DisplayName("Создание заказа c авторизацией и с ингредиентами")
    @Description("Проверка, что можно создать заказ с авторизацией и ингредиентами")
    public void createOrderWithAuthAndWithIngredientsSuccess(){
        Response response = orderSteps.createOrderWithAuthAndIngredients(token);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
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
                .statusCode(HttpStatus.SC_BAD_REQUEST)
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
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без авторизации (с ингредиентами)")
    @Description("Проверка, что можно создать заказ без авторизации")
    public void createOrderWithoutAuthSuccess(){
        Response response = orderSteps.createOrderWithoutAuth();
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }
}
