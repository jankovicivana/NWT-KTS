package nvt.kts.project.seleniumTests.tests;

import nvt.kts.project.seleniumTests.pages.ApprovePaymentPage;
import nvt.kts.project.seleniumTests.pages.ClientHomepagePage;
import nvt.kts.project.seleniumTests.pages.LoginPage;
import nvt.kts.project.seleniumTests.pages.NotificationsPage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuccessfulScheduleDriveTest extends TestBase{

    static final String EMAIL = "ivanaj0610@gmail.com";
    static final String PASS = "pass";
    static final String START = "Puskinova 16";
    static final String END = "Strazilovska 14";
    static final String ALERT = "Congratulations!";

    public static WebDriver driverDriver;
    public static WebDriver passengerDriver;

    @Test(priority = 4)
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

    @Test(priority = 3)
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

    @Test(priority = 2)
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

    @Test(priority = 1)
    public void testSuccessfulScheduleMoreClientsSplitFaire() throws InterruptedException {

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
        clientHomepagePage.clickSplitFaire();
        clientHomepagePage.finishScheduling();
        clientHomepagePage.clickAlert();

        ApprovePaymentPage approvePaymentPage = new ApprovePaymentPage(driver);
        approvePaymentPage.clickConfirm();


        passengerDriver = openPage("i@gmail.com");
        passengerDriver.manage().window().maximize();
        ClientHomepagePage clientHomepagePage2 = new ClientHomepagePage(passengerDriver);
        clientHomepagePage2.clickNotifications();
        NotificationsPage notificationsPage = new NotificationsPage(passengerDriver);
        notificationsPage.clickApproveTest();
        ApprovePaymentPage approvePaymentPage1 = new ApprovePaymentPage(passengerDriver);
        approvePaymentPage1.clickConfirm();
        assertTrue(approvePaymentPage.isSuccessful());
        driverDriver.quit();

    }

    private WebDriver openPage(String email) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver usedDriver = new ChromeDriver();
        usedDriver.manage().window().maximize();

        LoginPage loginPage = new LoginPage(usedDriver);
        loginPage.setUserEmail(email);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();
        usedDriver.manage().window().minimize();
        return usedDriver;
    }

    @Test(priority = 5)
    public void testReservation() {

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
        clientHomepagePage.reserve();

        assertTrue(clientHomepagePage.isAlertVisible(ALERT));

        driverDriver.quit();

    }

    @AfterClass
    public void close(){
        driver.quit();
    }


}
