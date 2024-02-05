import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utills.Pet;
import java.io.File;
import static io.restassured.RestAssured.given;


public class TestPet {

    Pet newpet = new Pet();
    private long ide;


    @BeforeTest
    public void createPet(){

        newpet.setId(1);
        newpet.setCategory(1,"Dog");
        newpet.setName("Tiger");
        newpet.setPhotoUrls(new String[]{"https://www.shutterstock.com/image-photo/close-portrait-angry-tiger-600nw-2320794599.jpg"});
        newpet.setTags(1,"new German Shepherd");
        newpet.setStatus("aviavble");
        Gson gson = new Gson();
        gson.toJson(newpet);
    }

    @Test
    public void postPet() {

        ide = given()
                .baseUri("https://petstore.swagger.io/v2")
                .contentType("application/json")
                .body(newpet)
                .when()
                .post("/pet")
                .jsonPath().getInt("id");

        System.out.println(ide);
    }

    @Test(priority = 2)
    public void uploadImage() {

        File image = new File("/Users/ankitsharma/Downloads/pexels-pixabay-162173 (1).jpg");

        given()
                .baseUri("https://petstore.swagger.io/v2")
                .pathParam("petId", ide)
                .multiPart("additionalMetadata", "Updated image of the pet")
                .multiPart("file", image, "image/jpeg")
                .contentType("multipart/form-data")
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .statusCode(200)
                .log().all();
    }
}
