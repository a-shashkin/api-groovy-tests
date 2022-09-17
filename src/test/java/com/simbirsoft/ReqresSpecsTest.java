package com.simbirsoft;

import com.simbirsoft.lombok.LombokResourceData;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static com.simbirsoft.Specs.requestSpecification;
import static com.simbirsoft.Specs.responseSpecification;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
