package nvt.kts.project.seleniumTests.tests;

import nvt.kts.project.seleniumTests.pages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class FinishDriveTest extends TestBase{

    static final String EMAIL = "ivanaj0610@gmail.com";
    static final String PASS = "pass";
    static final String START = "Puskinova 16";
    static final String END = "Strazilovska 14";

    public static WebDriver driverDriver;
    public static WebDriver passengerDriver;

    @Test
    public void shouldFinishDrive() {

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


        driverDriver.manage().window().maximize();
        DriverProfilePage driverProfilePage = new DriverProfilePage(driverDriver);
        driverProfilePage.clickCurrentDrives();

        DriverCurrentDrivesPage driverCurrentDrivesPage = new DriverCurrentDrivesPage(driverDriver);
        driverCurrentDrivesPage.clickGoToClient();
        driverCurrentDrivesPage.clickStart();
        driverCurrentDrivesPage.clickFinish();
        driverDriver.quit();
    }

    @Test
    public void shouldStopDrive() {

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


        driverDriver.manage().window().maximize();
        DriverProfilePage driverProfilePage = new DriverProfilePage(driverDriver);
        driverProfilePage.clickCurrentDrives();

        DriverCurrentDrivesPage driverCurrentDrivesPage = new DriverCurrentDrivesPage(driverDriver);
        driverCurrentDrivesPage.clickGoToClient();
        driverCurrentDrivesPage.clickStart();
        driverCurrentDrivesPage.clickStop();
        driverDriver.quit();
    }

    @Test
    public void shouldRejectDrive() throws InterruptedException {

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

        driverDriver.manage().window().maximize();
        DriverProfilePage driverProfilePage = new DriverProfilePage(driverDriver);
        driverProfilePage.clickCurrentDrives();

        DriverCurrentDrivesPage driverCurrentDrivesPage = new DriverCurrentDrivesPage(driverDriver);
        driverCurrentDrivesPage.clickReject();
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
        loginPage.clickOk();
        usedDriver.manage().window().minimize();
        return usedDriver;
    }
}
