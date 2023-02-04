package nvt.kts.project.seleniumTests.pages;

import nvt.kts.project.seleniumTests.tests.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ApprovePaymentPage extends TestBase {

    private final WebDriver driver;

    @FindBy(id = "confirmPayment")
    WebElement confirmButton;


    @FindBy(id = "toast-container")
    WebElement toast;

    public ApprovePaymentPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickConfirm(){
         (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    public boolean isSuccessful(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(toast,"Accepted drive!"));
    }

    public boolean noTokens(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(toast,"REJECTED payment!"));
    }

    public boolean noDrivers(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(toast,"REJECTED drive!"));
    }
}
