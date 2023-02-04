package nvt.kts.project.seleniumTests.tests;

import nvt.kts.project.seleniumTests.pages.ApprovePaymentPage;
import nvt.kts.project.seleniumTests.pages.ClientHomepagePage;
import nvt.kts.project.seleniumTests.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SuccessfulScheduleDriveTest extends TestBase{

    static final String EMAIL = "ivanaj0610@gmail.com";
    static final String PASS = "pass";
    static final String START = "Puskinova 16";
    static final String END = "Strazilovska 14";

    public static WebDriver driverDriver;
    public static WebDriver passengerDriver;

    @Test
    public void testSuccessfulSchedule() {

        driverDriver = openPage("driver@gmail.com");

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail(EMAIL);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        ClientHomepagePage clientHomepagePage = new ClientHomepagePage(driver);
        assertTrue(clientHomepagePage.isPageOpened());

        clientHomepagePage.setInputInfo(START,END);
        clientHomepagePage.waitToClickChoose();
        clientHomepagePage.clickChoose();

        clientHomepagePage.selectCarType();
        clientHomepagePage.clickNextSecondWindow();
        clientHomepagePage.finishScheduling();
        clientHomepagePage.clickAlert();

        ApprovePaymentPage approvePaymentPage = new ApprovePaymentPage(driver);
        approvePaymentPage.clickConfirm();
        assertTrue(approvePaymentPage.isSuccessful());
        driverDriver.quit();

    }

    @Test
    public void testSuccessfulScheduleMoreRoutes() {

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
        assertTrue(approvePaymentPage.isSuccessful());
        driverDriver.quit();

    }

    @Test
    public void testSuccessfulScheduleMoreClients() {

        driverDriver = openPage("driver@gmail.com");

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail(EMAIL);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        ClientHomepagePage clientHomepagePage = new ClientHomepagePage(driver);
        assertTrue(clientHomepagePage.isPageOpened());

        clientHomepagePage.setInputInfo(START,END);
        clientHomepagePage.waitToClickChoose();
        clientHomepagePage.clickChoose();

        clientHomepagePage.selectCarType();
        clientHomepagePage.addPassenger("i@gmail.com");
        clientHomepagePage.clickNextSecondWindow();
        clientHomepagePage.finishScheduling();
        clientHomepagePage.clickAlert();

        ApprovePaymentPage approvePaymentPage = new ApprovePaymentPage(driver);
        approvePaymentPage.clickConfirm();

        assertTrue(approvePaymentPage.isSuccessful());
        driverDriver.quit();

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

    @AfterClass
    public void close(){
        driver.quit();
    }


}
