package nvt.kts.project.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;

    private static final String PAGE_URL="http://localhost:4200/login";

    @FindBy(tagName = "h2")
    WebElement heading;

    @FindBy(id = "email")
    WebElement emailInput;

    @FindBy(id = "pass")
    WebElement passwordInput;

    @FindBy(id = "loginButton")
    WebElement loginButton;

    @FindBy(id = "swal2-html-container")
    WebElement alert;

    @FindBy(id = "profile-nav-link")
    WebElement navbarProfileTitle;


    public LoginPage(WebDriver driver){
        this.driver = driver;
        driver.get(PAGE_URL);

        PageFactory.initElements(driver, this);
    }

    public void setUserEmail(String email){
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void setUserPassword(String password){
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickOnLogin(){
        loginButton.click();
    }

    public boolean isPageOpened(){
        return (new WebDriverWait(driver, Duration.ofSeconds(5)))
                .until(ExpectedConditions.textToBePresentInElement(heading, "LOGIN"));
    }

    public boolean isAlertVisible(String alertText){
        return (new WebDriverWait(driver, Duration.ofSeconds(8)))
                .until(ExpectedConditions.textToBePresentInElement(alert, alertText));
    }

    public boolean isHomepageOpened(){
        return (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(navbarProfileTitle, "Profile"));
    }

}
