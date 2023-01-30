package nvt.kts.project.seleniumTests.tests;

import nvt.kts.project.seleniumTests.pages.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LoginWrongCredentialsTest extends TestBase{

    static final String EMAIL = "ivanak@gmail.com";
    static final String PASS = "pas";
    static final String ALERT = "Wrong credentials!";


    @Test
    public void testLogin() throws InterruptedException {

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail(EMAIL);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        assertTrue(loginPage.isAlertVisible(ALERT));

        Thread.sleep(2000);
    }
}
