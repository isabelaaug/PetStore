package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {
    // Endereço da entidade pet
    String uri = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    // POST
    @Test(priority = 1)
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet1.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Bob"))
                .body("status", is("available"))
                .body("tags.name", contains("vacinado"))
                .body("category.name", is("Dog"))
                .body("category.name", containsString("Dog"));
    }

    // GET
    @Test(priority = 2)
    public void consultarPet(){
        String petId = "199678586521548500";

        String token =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Bob"))
                .body("status", is("available"))
        .extract()
                .path("category.name");

        System.out.println("o token é " + token);
    }

    // PUT
    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("src/test/resources/db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
       .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Bob"))
                .body("status", is("unavailable"));
    }
}
