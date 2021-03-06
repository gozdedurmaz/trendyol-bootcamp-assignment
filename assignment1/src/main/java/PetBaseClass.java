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

public class PetBaseClass {

    public void createPet(long id) throws IOException {
        URL file = Resources.getResource("pet.json");
        String myJson = Resources.toString(file, Charset.defaultCharset());
        JSONObject json = new JSONObject(myJson);

        json.put("id", id);
        json.getJSONObject("category").put("id", id);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/pet")
                .then()
                .statusCode(200);
    }

    public  void checkPet(long id) throws IOException {
        given()
                .contentType("application/json")
                .when()
                .get("/pet/{petId}", id)
                .then()
                .statusCode(200);
    }

    public  void updatePet(long id,String value,String key) throws IOException {
        URL userFile = Resources.getResource("pet.json");
        String petJson = Resources.toString(userFile, Charset.defaultCharset());
        JSONObject petObject = new JSONObject(petJson);
        petObject.put("id", id);
        petObject.getJSONObject("category").put("id", id);
        petObject.put(key,value);

        given()
                .contentType("application/json")
                .body(petObject.toString())
                .when()
                .put("/pet")
                .then()
                .statusCode(200);
    }

    public void checkPetUpdate(long id,String value,String key) throws IOException {
        Response response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/pet/{petId}", id)
                .then()
                .statusCode(200)
                .extract().response();
                 //response.prettyPeek();
       assertThat(response.getBody().jsonPath().getString(key), Matchers.containsString(value));
    }

    public  void deletePet(long id) throws IOException {
        given()
                .contentType("application/json")
                .when()
                .delete("/pet/{petId}", id)
                .then()
                .statusCode(200);
    }

    public void getPetThatDoesNotExist(long id) throws  IOException{
        given()
                .contentType("application/json; charset=UTF-8")
                .when()
                .get("/pet/{petId}", id)
                .then()
                .statusCode(404);
    }

    public void validatePetDeletion(long id) throws  IOException{
        try {
            getPetThatDoesNotExist(id);
        }
        catch (HttpResponseException ex)
        {
            assert ex.getStatusCode() == 404;
        }
    }
}
