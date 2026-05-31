import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import model.UserModel;

import static data.OrderData.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTests extends BaseApiTest {
    private String token;

    @After
    public void tearDown() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя с валидными данными")
    @Description("Проверка успешного создания пользователя при передаче уникальных корректных данных")
    public void testCreateUserFunctionalitySuccess() {
        String uniqueEmail = System.currentTimeMillis() + EMAIL;
        UserModel user = new UserModel(uniqueEmail, PASSWORD, NAME);

        Response response = userSteps.createUser(user);
        token = response.jsonPath().get("accessToken");

        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(uniqueEmail))
                .body("user.name", equalTo(NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка, что нельзя создать пользователя, который уже зарегистрирован")
    public void testCreateExistingUserFails() {
        String uniqueEmail = System.currentTimeMillis() + EMAIL;
        UserModel user = new UserModel(uniqueEmail, PASSWORD, NAME);
        Response firstResponse = userSteps.createUser(user);
        token = firstResponse.jsonPath().get("accessToken");

        Response response = userSteps.createUser(user);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля email")
    @Description("Проверка, что нельзя создать пользователя с незаполненным обязательным полем email")
    public void testCreateUserWithoutEmailFails() {

        UserModel user = new UserModel(null, PASSWORD, NAME);

        Response response = userSteps.createUser(user);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля password")
    @Description("Проверка, что нельзя создать пользователя с незаполненным обязательным полем password")
    public void testCreateUserWithoutPasswordFails() {
        String uniqueEmail = System.currentTimeMillis() + EMAIL;
        UserModel user = new UserModel(uniqueEmail, null, NAME);

        Response response = userSteps.createUser(user);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля name")
    @Description("Проверка, что нельзя создать пользователя с незаполненным обязательным полем name")
    public void testCreateUserWithoutNameFails() {
        String uniqueEmail = System.currentTimeMillis() + EMAIL;
        UserModel user = new UserModel(uniqueEmail, PASSWORD, null);

        Response response = userSteps.createUser(user);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}