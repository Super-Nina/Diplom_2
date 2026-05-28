package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.UserModel;
import model.UserModelForAuth;

import static data.OrderData.*;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создание пользователя")
    public static Response createUser(UserModel user) {
//        Response response = given()
              return given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_CREATE_ENDPOINT);
//        return response;
    }

    @Step("Создание пользователя и получение токена")
    public static String createUserAndGetToken(UserModel user) {
        Response response = given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_CREATE_ENDPOINT);

        response.then()
                .log().all();

        String token = response.jsonPath().get("accessToken");
        return token;
    }

    @Step("Авторизация пользователя")
    public static Response userAuthorization(UserModelForAuth userForAuth){
        Response response = given()
                .log().all()
                .header("Content-type", "application/json")
                .body(userForAuth)
                .when()
                .post(USER_LOGIN_ENDPOINT);
        return response;

    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String token) {
         Response response = given()
                .log().all()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .when()
                .delete(USER_DELETE_ENDPOINT);

         response.then()
                .log().all();
        return response;
    }

}
