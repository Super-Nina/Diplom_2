package data;

import com.github.javafaker.Faker;

public class OrderData {
    public static final String BASE_URI = "https://stellarburgers.education-services.ru";

    static Faker user = new Faker();
    public static  String EMAIL = user.internet().emailAddress();
    public static final String PASSWORD = user.regexify("[0-9]{5}");
    public static final String NAME = user.name().lastName();

    public static final String USER_CREATE_ENDPOINT = "/api/auth/register";
    public static final String USER_LOGIN_ENDPOINT = "/api/auth/login";
    public static final String USER_DELETE_ENDPOINT = "/api/auth/user";

    public static final String ORDER_CREATE_ENDPOINT = "/api/orders";
    }
