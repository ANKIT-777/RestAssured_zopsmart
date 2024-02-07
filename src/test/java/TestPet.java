import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utills.Category;
import utills.Pet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import org.json.JSONObject;
import utills.Tag;
import org.json.JSONArray;
import org.json.JSONObject;


public class TestPet {

    Pet newpet = new Pet();
    private long ide;
    JSONObject petObject;
    private String fileName = "Book1";

    @BeforeTest
    public void getValueFromExce() throws IOException {
        List<LinkedHashMap<String,String >> extractedValue = new ArrayList<>();

        Workbook workbook =  WorkbookFactory.create(new File("src/main/resources/" + fileName  +".xlsx"));
        Sheet sheet =  workbook.getSheet("Sheet1");


        int rowNumber = sheet.getPhysicalNumberOfRows();
        List<String> allKeys = new ArrayList<>();

        for (int i =0;i<rowNumber;i++){
            int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
            if(i==0) {
                for (int j = 0; j < totalCols; j++) {
                    allKeys.add(sheet.getRow(0).getCell(j).getStringCellValue());
                }
            }
            else{
                if(sheet.getRow(i).getCell(1).getStringCellValue().equals("Y")){
                    Category category = new Category();
                    petObject = new JSONObject();
                    Tag tag = new Tag();
                    Gson gson = new Gson();

                    for (int j = 3; j < totalCols; j++) {
                        newpet.setId(i);
                        petObject.put("id", i);

                        JSONObject categoryObject = new JSONObject();
                        categoryObject.put("id", i);
                        categoryObject.put("name", sheet.getRow(i).getCell(j).getStringCellValue());
                        petObject.put("category", categoryObject);

                        newpet.setCategory(new String[]{String.valueOf(category)});
                        j++;

                        petObject.put("name",sheet.getRow(i).getCell(j).getStringCellValue());
                        newpet.setName(sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;

                        JSONArray photoUrlsArray = new JSONArray();
                        photoUrlsArray.put(sheet.getRow(i).getCell(j).getStringCellValue());
                        petObject.put("photoUrls", photoUrlsArray);

                        newpet.setPhotoUrls(new String[]{sheet.getRow(i).getCell(j).getStringCellValue()});
                        j++;

                        JSONArray tagsArray = new JSONArray();
                        JSONObject tagObject = new JSONObject();

                        tagObject.put("id", i);
                        tagObject.put("name", sheet.getRow(i).getCell(j).getStringCellValue());
                        tagsArray.put(tagObject);
                        petObject.put("tags", tagsArray);
                        j++;

                        tag.setId(i);
                        tag.setName(sheet.getRow(i).getCell(j).getStringCellValue());
                        System.out.println(String.valueOf(tag));
                        newpet.setTags(new String[]{String.valueOf(tag)});

                        petObject.put("status",sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;

                        newpet.setStatus(sheet.getRow(i).getCell(j).getStringCellValue());

                        gson.toJson(newpet);
                    }
                }

            }
        }

    }

    @Test
    public void postPet() {

        System.out.println(petObject.toString());

         ide = given()
                .contentType("application/json")
                .body(petObject)
                .when()
                .post("https://petstore.swagger.io/v2/pet")
                 .jsonPath().get("id");
    }

//    @Test(priority = 2)
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
