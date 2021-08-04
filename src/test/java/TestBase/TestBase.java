package TestBase;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.logging.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;



public class TestBase extends Utilis{
    static Logger logger;

    @BeforeTest
    public void setup() {
    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//browserDriver//chromedriver");

    }
        @Test
        public void testSingleInsert() {
            WebDriver driver = new ChromeDriver();
            RequestSpecification request = RestAssured.given();

            driver.manage().window().maximize();
            driver.get("http://localhost:8080/");
            logger = Logger.getLogger(TestBase.class.getName());

            try {
                logger.info("------------------ Starting tests on Singel Insert Data ------------------");
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(Utilis.jsonFile));
                JSONArray jsonAr = (JSONArray) obj;
                logger.info("Data Insert");
                request.request("Accept", "application/json");
                request.body(jsonAr.toJSONString());
                Response response = request.post(Utilis.baseURL+"/calculator/insert/");
                int statusCode = response.getStatusCode();
                

                if(statusCode == 200){
                    logger = Logger.getLogger("The status code recieved: " + statusCode);
                    Assert.assertTrue(Boolean.parseBoolean("The status code recieved: " + statusCode));
                }
                else{
//                    System.out.println("The status code recieved: " + statusCode);
//                    System.out.println("Response body: " + response.body().asString());
                    logger = Logger.getLogger("The status code recieved: " + statusCode);
                    Assert.assertFalse(Boolean.parseBoolean("The status code recieved: " + statusCode));
                }
                System.out.println("Response body: " + response.body().asString());


            }catch (FileNotFoundException e){
                logger.info("File not found: " + e.getMessage());
            }catch (ParseException e){
                logger.info("Parse not found: " + e.getMessage());
            } catch (IOException e) {
            logger.info("Exception occurred: " + e.getMessage());
            }
            driver.close();
        }

    @Test
    public void testFileUpload() throws UnirestException {
        logger = Logger.getLogger(TestBase.class.getName());

        logger.info("------------------ Starting tests on File Upload  ------------------");
        RequestSpecification request = RestAssured.given();
        WebDriver driver = new ChromeDriver();
        Response response = request.post(Utilis.baseURL+"calculator/uploadLargeFileForInsertionToDatabase");
        int statusCode = response.getStatusCode();

        driver.manage().window().maximize();
        driver.get(Utilis.baseURL);

        logger = Logger.getLogger(TestBase.class.getName());

        WebElement UploadFile = driver.findElement(By.className("custom-file-input"));
        UploadFile.sendKeys(Utilis.filePath);
        UploadFile.clear();
          

        if(statusCode != 200) {
            logger = Logger.getLogger("The status code recieved: " + statusCode);
            Assert.assertFalse(false,"The status code recieved: " + statusCode);
        }
        else {
            logger = Logger.getLogger("The status code recieved: " + statusCode);
            Assert.assertTrue(true,"The status code recieved: " + statusCode);
        }
        System.out.println("Response body: " + response.body().asString());
        driver.close();

        }

    }

