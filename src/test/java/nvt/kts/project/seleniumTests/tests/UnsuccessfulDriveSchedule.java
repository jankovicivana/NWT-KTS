package nvt.kts.project.seleniumTests.tests;

import nvt.kts.project.seleniumTests.pages.ApprovePaymentPage;
import nvt.kts.project.seleniumTests.pages.ClientHomepagePage;
import nvt.kts.project.seleniumTests.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class UnsuccessfulDriveSchedule extends TestBase{


    static final String EMAIL = "ivana@gmail.com";
    static final String PASS = "pass";
    static final String START = "Puskinova 16";
    static final String END = "Strazilovska 14";
    static final String ALERT = "User not found";

    public static WebDriver driverDriver;
    public static WebDriver passengerDriver;


    @Test
    public void testSuccessfulScheduleNoEnoughTokens() {

        driverDriver = openPage("driver@gmail.com");

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail(EMAIL);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        ClientHomepagePage clientHomepagePage = new ClientHomepagePage(driver);
        assertTrue(clientHomepagePage.isPageOpened());

        clientHomepagePage.setInputInfo(START,END);
        clientHomepagePage.addStation("Puskinova 6");
        clientHomepagePage.waitToClickChoose();
        clientHomepagePage.clickChoose();

        clientHomepagePage.selectCarType();
        clientHomepagePage.clickNextSecondWindow();
        clientHomepagePage.finishScheduling();
        clientHomepagePage.clickAlert();

        ApprovePaymentPage approvePaymentPage = new ApprovePaymentPage(driver);
        approvePaymentPage.clickConfirm();
        assertTrue(approvePaymentPage.noTokens());
        driverDriver.quit();
    }

    @Test
    public void testSuccessfulScheduleNoDrivers() {

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail("i@gmail.com");
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        ClientHomepagePage clientHomepagePage = new ClientHomepagePage(driver);
        assertTrue(clientHomepagePage.isPageOpened());

        clientHomepagePage.setInputInfo(START,END);
        clientHomepagePage.addStation("Puskinova 6");
        clientHomepagePage.waitToClickChoose();
        clientHomepagePage.clickChoose();

        clientHomepagePage.selectCarType();
        clientHomepagePage.clickNextSecondWindow();
        clientHomepagePage.finishScheduling();
        clientHomepagePage.clickAlert();

        ApprovePaymentPage approvePaymentPage = new ApprovePaymentPage(driver);
        approvePaymentPage.clickConfirm();
        assertTrue(approvePaymentPage.noDrivers());

    }
    @Test
    public void testUserNotFound() {

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());
        loginPage.setUserEmail("i@gmail.com");
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();
        ClientHomepagePage clientHomepagePage = new ClientHomepagePage(driver);
        assertTrue(clientHomepagePage.isPageOpened());

        clientHomepagePage.setInputInfo(START,END);
        clientHomepagePage.waitToClickChoose();
        clientHomepagePage.clickChoose();

        clientHomepagePage.selectCarType();
        clientHomepagePage.addPassenger("tretsfsf@gmail.com");

        assertTrue(clientHomepagePage.isAlertVisible(ALERT));


    }

    private WebDriver openPage(String emailDriver) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver usedDriver = new ChromeDriver();
        usedDriver.manage().window().maximize();

        LoginPage loginPage = new LoginPage(usedDriver);
        loginPage.setUserEmail(emailDriver);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();
        usedDriver.manage().window().minimize();
        return usedDriver;
    }

    @AfterSuite
    public void close(){
        driver.quit();
    }

}
