package praktikum.steps;

import io.restassured.response.ValidatableResponse;
import praktikum.config.RestConfig;
import praktikum.model.Order;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private static final Random random = new Random();

    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(RestConfig.CREATE_ORDER)
                .then();
    }

    public String[] getRandomIngredientIds(int maxCount) {
        List<String> allIngredients = given()
                .get(RestConfig.GET_INGREDIENTS)
                .then()
                .extract()
                .jsonPath()
                .getList("data._id");
        int count = 1 + random.nextInt((Math.min(maxCount, allIngredients.size())));
        String[] selected = new String[count];

        for (int i = 0; i < count; i++) {
            selected[i] = allIngredients.get(random.nextInt(allIngredients.size()));
        }
        return selected;
    }
}
