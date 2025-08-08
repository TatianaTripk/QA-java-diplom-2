package praktikum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.datafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.Order;
import praktikum.model.User;
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTests extends BaseTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private final UserSteps userSteps = new UserSteps();
    private User user;
    private boolean isUserCreated = false;
    Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        order = new Order();
    }

    // Создание заказа без авторизации (с ингредиентами)
    @Test
    public void shouldCreateOrderWithoutLoginTest() {
        String[] randomIngredients = orderSteps.getRandomIngredientIds(5);
        order.setIngredients(randomIngredients);
        orderSteps
                .createOrder(order)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true));
    }

    //Создание заказа с авторизацией (с игредиентами)
    @Test
    public void shouldCreateOrderWithLoginTest() {
        user = new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password())
                .setName(faker.name().username());
        userSteps
                .createUser(user);
        isUserCreated = true;
        String accessToken = userSteps.loginUser(user)
                .extract()
                .path("accessToken");
        String[] randomIngredients = orderSteps.getRandomIngredientIds(5);
        order.setIngredients(randomIngredients)
                .setAccessToken("Bearer " + accessToken);
        orderSteps
                .createOrder(order)
                .statusCode(SC_OK)
                .body("success", Matchers.is(true));
    }

    //Создание заказа без ингредиентов
    @Test
    public void shouldNotCreateOrderWithoutIngredientsTest() {
        orderSteps
                .createOrder(order)
                .statusCode(SC_BAD_REQUEST)
                .body("success", Matchers.is(false))
                .body("message", Matchers.equalTo("Ingredient ids must be provided"));
    }

    //Создание заказа с неверным хешем ингредиентов
    @Test
    public void shouldNotCreateOrderWithInvalidIngredientHashTest() {
        String[] wrongIngredients = {"invalid_hash_1", "invalid_hash_2"};
        order.setIngredients(wrongIngredients);
        orderSteps
                .createOrder(order)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (isUserCreated) {
            String token = userSteps.loginUser(user)
                    .extract().body().path("accessToken");
            user.setToken(token);
            userSteps.deleteUser(user);
        }
    }
}
