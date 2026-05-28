import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.UserModel;
import model.UserModelForAuth;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import steps.UserSteps;

import static data.OrderData.BASE_URI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTests {
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
    @DisplayName("Пользователь может авторизоваться") // имя теста
    @Description("Проверка, что пользователь может авторизоваться с валидными данными")
    public void testUserAuthorizationSuccess() {
        String login = userForAuth.getEmail();
        UserModelForAuth userForAuth = new UserModelForAuth (login, "password");
        userSteps.userAuthorization(userForAuth)
                .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(userForAuth.getEmail()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Попытка авторизации с неверным логином")
    @Description("Проверка, что система вернет ошибку при авторизации с неверным логином")
    public void testUserAuthorizationWithWrongLoginFails() {
        String login = userForAuth.getEmail();
        String wrongLogin = login + "_wrong";
        UserModelForAuth userWithWrongLogin = new UserModelForAuth (wrongLogin, "password");

        userSteps.userAuthorization(userWithWrongLogin)
                .then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Попытка авторизации с неверным паролем")
    @Description("Проверка, что система вернет ошибку при авторизации с неверным паролем")
    public void testUserAuthorizationWithWrongPasswordFails() {
        String login = userForAuth.getEmail();
        String password = userForAuth.getPassword();
        String wrongPassword = password + "_wrong";
        UserModelForAuth userWithWrongLogin = new UserModelForAuth(login, wrongPassword);

        userSteps.userAuthorization(userWithWrongLogin)
                .then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
