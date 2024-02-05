import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utills.Pet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.preemptive;


public class TestPet {

    Pet newpet = new Pet();
    private long ide;
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
                    for (int j = 3; j < totalCols-1; j++) {
                        newpet.setId(i);
                        newpet.setCategory(i,sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;
                        newpet.setName(sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;
                        newpet.setPhotoUrls(new String[]{sheet.getRow(i).getCell(j).getStringCellValue()});
                        j++;
                        newpet.setStatus(sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;
                        newpet.setTags(i,sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;
                        Gson gson = new Gson();
                        gson.toJson(newpet);
                    }
                }

            }
        }

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
