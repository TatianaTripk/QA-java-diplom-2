package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.config.RestConfig;
import praktikum.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .when()
                .post(RestConfig.CREATE_USER)
                .then();
    }

    @Step
    public ValidatableResponse loginUser(User user) {
        return given()
                .body(user)
                .when()
                .post(RestConfig.LOGIN_USER)
                .then();
    }

    @Step
    public void deleteUser(User user) {
        given()
                .header("Authorization", user.getToken())
                .when()
                .delete(RestConfig.DELETE_USER)
                .then();
    }
}
