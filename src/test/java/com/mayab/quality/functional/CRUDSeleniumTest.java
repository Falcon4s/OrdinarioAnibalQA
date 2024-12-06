package com.mayab.quality.functional;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


public class CRUDSeleniumTest {

    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;

    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        baseUrl = "http://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void createNewRecord() throws Exception {
    	driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Anibal Falcon");
        
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("AnibalFalcon@usuario.com");
        
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("25");
        
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click(); 
        driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
        pause(1000);
        takeScreenshot("CreateNewRecord");
        
        String isUserCreated = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[1]")).getText();
        assertEquals(isUserCreated, "Anibal Falcon");     
    }
    
    @Test
    public void createNewRecord2() throws Exception {
    	driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Angel Falcon");
        
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("AngelFalcon@usuario.com");
        
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("25");
        
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click(); 
        driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
        pause(1000);
        takeScreenshot("CreateNewRecord2");
        

    }

    @Test
    public void tryCreateWithExistingEmail() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        pause(1000);

        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Angel S Falcon");
        pause(1000);

        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("AngelFalcon@usuario.com");
        pause(1000);

        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("45");
        pause(1000);

        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
        pause(1000);

        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(1000);
        takeScreenshot("tryCreateWithExistingEmail");

        String errorMessage = driver.findElement(By.xpath("//p[text()='That email is already taken.']")).getText();
        assertEquals(errorMessage, "That email is already taken.");
    }

//
    @Test
    public void modifyRecord() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr/td[5]/button")).click();
        pause(1000);

        driver.findElement(By.name("age")).click();
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("25");
        pause(1000);

        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
        pause(1000);
        takeScreenshot("modifyRecord");

        String ageCheck = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[3]")).getText();
        assertEquals(ageCheck, "25");
    }


    @Test
    public void deleteRecord() throws Exception {
	    driver.get(baseUrl + "chrome://newtab/");
	    driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click(); 
        driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
        pause(1000);
        takeScreenshot("deleteRecord");

        
        String userErased = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[2]")).getText();
        String userExpected = "AnibalFalcon@usuario.com";
        assertNotEquals(userErased, userExpected);

    }
    
    @Test
    public void createNewRecord3() throws Exception {
    	driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Gabriel Torres");
        
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("GabrielTorres@usuario.com");
        
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("45");
        
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click(); 
        driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
        pause(1000);
        takeScreenshot("CreateNewRecord3");


    }
    
    @Test
    public void createNewRecord4() throws Exception {
    	driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Angel Castro");
        
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("AngelCastro@usuario.com");
        
        driver.findElement(By.name("age")).clear();
        driver.findElement(By.name("age")).sendKeys("19");
        
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/i")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click(); 
        driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
        pause(1000);
        takeScreenshot("CreateNewRecord4");


    }
    
    @Test
    public void searchForSpecificRecordByName() throws Exception {
    	driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[2]/table/tbody"))
        );
        
        boolean isRecordFound = false;
        
        java.util.List<WebElement> rows = driver.findElements(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr"));
        
        for (WebElement row : rows) {
            String name = row.findElement(By.xpath("td[1]")).getText();
            
            if (name.equalsIgnoreCase("Angel Falcon")) {
                isRecordFound = true;
                break;
            }
        }
        
        assertThat(isRecordFound, is(true));
        pause(1000);
        takeScreenshot("findone");

        
    }
    
    @Test
    public void searchForAllRecords() throws Exception {
        Set<String> namesToSearch = new HashSet<>(Arrays.asList("Angel Falcon", "Gabriel Torres", "Angel Castro"));
        boolean allFound = areAllRecordsFound(namesToSearch);
        assertThat(allFound, is(true));
        pause(1000);
    }

    public boolean areAllRecordsFound(Set<String> namesToFind) {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[2]/table/tbody"))
        );
        
        Set<String> usersToFind = new HashSet<>(namesToFind);
        
        java.util.List<WebElement> rows = driver.findElements(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr"));
        
        for (WebElement row : rows) {
            String name = row.findElement(By.xpath("td[1]")).getText();
            
            usersToFind.remove(name);
            
            if (usersToFind.isEmpty()) {
                break;
            }
        }
        
        return usersToFind.isEmpty();
        takeScreenshot("findall");

    }
    public void takeScreenshot(String fileName) throws IOException {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("src/screenshot/" + fileName + ".jpg"));
    }

    private void pause(long mils) {
  	  try {
  		  Thread.sleep(mils);  
  	  }catch(Exception e) {
  		  e.printStackTrace();
  	  }
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            org.junit.jupiter.api.Assertions.fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } finally {
        }
    }
}
