import com.google.common.io.Resources;
import io.restassured.RestAssured;
import io.restassured.internal.http.HttpResponseException;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;

public class StoreBaseClass {
    public void createOrder(long id) throws IOException {
        URL file = Resources.getResource("order.json");
        String orderJson = Resources.toString(file, Charset.defaultCharset());
        JSONObject json = new JSONObject(orderJson);

        json.put("id", id);
        json.put("petId", id);

        given()
                .contentType("application/json")
                .body(json.toString())
                .when()
                .post("/store/order")
                .then()
                .statusCode(200);
    }
    public  void checkOrder(long id) throws IOException {
        given()
                .contentType("application/json")
                .when()
                .get("/store/order/{orderId}", id)
                .then()
                .statusCode(200);
    }
    public  void deleteOrder(long id) throws IOException {
        given()
                .contentType("application/json")
                .when()
                .delete("/store/order/{orderId}", id)
                .then()
                .statusCode(200);
    }

    public void getOrderThatDoesNotExist(long id) throws  IOException{
        given()
                .contentType("application/json; charset=UTF-8")
                .when()
                .get("/store/order/{orderId}", id)
                .then()
                .statusCode(404);
    }
    public void validateOrderDeletion(long id) throws  IOException{
        try{
            getOrderThatDoesNotExist(id);
        }
        catch (HttpResponseException ex)
        {
            assert ex.getStatusCode() == 404;
        }
    }
}
