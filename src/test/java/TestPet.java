import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utills.Category;
import utills.Pet;
import java.io.File;
import java.io.FileOutputStream;
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
    private String fileName = "TestData";
    Workbook workbook;
    Response res;

    @BeforeTest
    public void getValueFromExce() throws IOException {
        List<LinkedHashMap<String,String >> extractedValue = new ArrayList<>();

        workbook =  WorkbookFactory.create(new File("src/main/resources/" + fileName  +".xlsx"));
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


                    for (int j = 3 ;j < totalCols; j++) {

                        petObject.put("id", i);


                        JSONObject categoryObject = new JSONObject();
                        categoryObject.put("id", i);
                        categoryObject.put("name", sheet.getRow(i).getCell(j).getStringCellValue());
                        petObject.put("category", categoryObject);
                        j++;

                        petObject.put("name",sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;

                        JSONArray photoUrlsArray = new JSONArray();
                        photoUrlsArray.put(sheet.getRow(i).getCell(j).getStringCellValue());
                        petObject.put("photoUrls", photoUrlsArray);
                        j++;


                        JSONArray tagsArray = new JSONArray();
                        JSONObject tagObject = new JSONObject();

                        tagObject.put("id", i);
                        tagObject.put("name", sheet.getRow(i).getCell(j).getStringCellValue());
                        tagsArray.put(tagObject);
                        petObject.put("tags",tagsArray);
                        j++;

                        petObject.put("status",sheet.getRow(i).getCell(j).getStringCellValue());
                        j++;

                    }
                }

            }
        }

    }

    @Test(priority = 1)
    public void postPet() {

          ide = given()
                .contentType("application/json")
                .body(petObject.toString())
                .when()
                .post("https://petstore.swagger.io/v2/pet").jsonPath().getLong("id");

    }

    @Test(priority = 2)
    public void uploadImage() {

        File image = new File("/Users/ankitsharma/Downloads/pexels-pixabay-162173 (1).jpg");

        res = given()
                .baseUri("https://petstore.swagger.io/v2")
                .pathParam("petId", ide)
                .multiPart("additionalMetadata", "Updated image of the pet")
                .multiPart("file", image, "image/jpeg")
                .contentType("multipart/form-data")
                .when()
                .post("/pet/{petId}/uploadImage");

        if (res.statusCode() == 200) {
            updateResult("success");
        } else {
            updateResult("fail");
        }

    }

    public void updateResult(String result) {
        Sheet sheet = workbook.getSheet("Sheet1");
        Row row = sheet.getRow(1);
        Cell resultCell = row.getCell(10);
        if (resultCell == null) {
            resultCell = row.createCell(10);
        }
        resultCell.setCellValue(result);
    }


}
