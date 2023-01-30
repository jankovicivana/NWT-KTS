package nvt.kts.project.seleniumTests.tests;


import nvt.kts.project.seleniumTests.pages.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LoginTest extends TestBase{

    static final String EMAIL = "ivanaj0610@gmail.com";
    static final String PASS = "pass";
    static final String ALERT = "Congratulations!";


    @Test
    public void testLogin() throws InterruptedException {

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageOpened());

        loginPage.setUserEmail(EMAIL);
        loginPage.setUserPassword(PASS);
        loginPage.clickOnLogin();

        assertTrue(loginPage.isAlertVisible(ALERT));
        assertTrue(loginPage.isHomepageOpened());

        Thread.sleep(5000);
    }
}
