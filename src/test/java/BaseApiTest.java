import io.restassured.RestAssured;
import org.junit.Before;
import steps.OrderSteps;
import steps.UserSteps;

import static data.OrderData.BASE_URI;

public class BaseApiTest {
    protected static UserSteps userSteps;
    protected static OrderSteps orderSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
    }
}
