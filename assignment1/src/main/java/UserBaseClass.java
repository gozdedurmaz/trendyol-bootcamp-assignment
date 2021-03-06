import com.google.common.io.Resources;
import io.restassured.RestAssured;
import io.restassured.internal.http.HttpResponseException;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

public class UserBaseClass {

    public void createUser(long id) throws IOException {
        URL userFile = Resources.getResource("user.json");
        String userJson = Resources.toString(userFile, Charset.defaultCharset());
        JSONObject userObject = new JSONObject(userJson);
        long userId = id;

        String username = "gozde" + String.valueOf(userId);
        userObject.put("id", userId);
        userObject.put("username", username);

        given()
                .contentType("application/json")
                .body(userObject.toString())
                .when()
                .post("/user")
                .then()
                .statusCode(200);
    }

    public void checkUser(long id) throws IOException {
        String username = "gozde" + String.valueOf(id);
        given()
                .contentType("application/json")
                .when()
                .get("/user/{username}", username)
                .then()
                .statusCode(200);
    }

    public void loginUser(String username, String password) throws IOException {
        given()
                .contentType("application/json")
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login")
                .then()
                .statusCode(200);
    }

    public void updateUser(long id, String value, String key) throws IOException {
        String username = "gozde" + String.valueOf(id);
        URL userFile = Resources.getResource("user.json");
        String userJson = Resources.toString(userFile, Charset.defaultCharset());
        JSONObject userObject = new JSONObject(userJson);
        userObject.put("username",username);
        userObject.put("id", id);
        userObject.put(key, value);
        given()
                .contentType("application/json")
                .body(userObject.toString())
                .when()
                .put("/user/{username}", username)
                .then()
                .statusCode(200);
    }

    public void checkUserUpdate(long id,String value, String key) throws IOException {
        String username = "gozde" + String.valueOf(id);
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/user/{username}", username)
                .then()
                .statusCode(200)
                .extract().response();
        assertThat(response.getBody().jsonPath().getString(key), Matchers.containsString(value));
    }

    public void deleteUser(String username) throws IOException {
        given()
                .contentType("application/json")
                .when()
                .delete("/user/{username}", username)
                .then()
                .statusCode(200);
    }

    public void getUserThatDoesNotExist(String username) throws IOException {
        given()
                .contentType("application/json; charset=UTF-8")
                .when()
                .get("/user/{username}", username)
                .then()
                .statusCode(404);
    }

    public void validateUserDeletion(String username) throws IOException {
        try {
            getUserThatDoesNotExist(username);
        } catch (HttpResponseException ex) {
            assert ex.getStatusCode() == 404;
        }
    }
}
