package com.simbirsoft;

import com.simbirsoft.lombok.LombokRegisterTokenData;
import com.simbirsoft.lombok.LombokResourceData;
import com.simbirsoft.models.ModelUser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static com.simbirsoft.Specs.requestSpecification;
import static com.simbirsoft.Specs.responseSpecification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasItem;

public class ReqresSpecsTest {

    @Test
    void getListUsersTest() {
        Integer response =
        given()
                        .spec(requestSpecification)
                .when()
                        .get("/users?page=2")
                .then()
                        .spec(responseSpecification)
                        .log().body()
                        .extract().path("total");

        assertEquals(12, response);
    }

    @Test
    void getSingleUserTest() {
        Response response =
                given()
                        .spec(requestSpecification)
                .when()
                        .get("/users/2")
                .then()
                        .spec(responseSpecification)
                        .extract().response();

        String name = response.path("data.first_name") + " " + response.path("data.last_name");
        Integer id = response.path("data.id");
        String email = response.path("data.email");

        assertEquals("Janet Weaver", name);
        assertEquals(2, id);
        assertEquals("janet.weaver@reqres.in", email);
    }

    @Test
    void getSingleUserModelTest() {
        ModelUser user =
                given()
                        .spec(requestSpecification)
                .when()
                        .get("/users/2")
                .then()
                        .spec(responseSpecification)
                        .extract().as(ModelUser.class);

        assertEquals(2, user.getData().getId());
        assertEquals("Janet Weaver", user.getData().getFirstName() + " " + user.getData().getLastName());
        assertEquals("janet.weaver@reqres.in", user.getData().getEmail());
    }

    @Test
    void getSingleUserGroovyTest() {
                given()
                        .spec(requestSpecification)
                .when()
                        .get("/users")
                .then()
                        .log().body()
                        .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                                hasItem("janet.weaver@reqres.in"));
    }

    @Test
    void getResourceList() {
        LombokResourceData resourceData =
                given()
                        .spec(requestSpecification)
                .when()
                        .get("/unknown")
                .then()
                        .spec(responseSpecification)
                        .log().body()
                        .extract().as(LombokResourceData.class);

        assertEquals(1, resourceData.getPage());
        assertEquals(6, resourceData.getPerPage());
        assertEquals(12, resourceData.getTotal());
        assertEquals(2, resourceData.getTotalPages());
    }

    @Test
    void registerUserTest() {
        Map<String, String> jsonBody = new HashMap<>();
        jsonBody.put("email", "eve.holt@reqres.in");
        jsonBody.put("password", "pistol");

        LombokRegisterTokenData response =
                given().
                        header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36").
                        spec(requestSpecification).
                        body(jsonBody).
                        when().
                        post("https://reqres.in/api/register").
                        then().
                        spec(responseSpecification).
                        extract().as(LombokRegisterTokenData.class);

        assertEquals(4, response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }
}
