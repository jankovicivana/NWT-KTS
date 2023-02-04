package nvt.kts.project.seleniumTests.pages;

import nvt.kts.project.seleniumTests.tests.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NotificationsPage extends TestBase {


    private final WebDriver driver;

    @FindBy(id = "approvePayment_0")
    WebElement approvePayment;


    @FindBy(id = "toast-container")
    WebElement approveToast;

    public NotificationsPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickApproveTest(){
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(approvePayment)).click();
    }


}
