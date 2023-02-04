package nvt.kts.project.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverCurrentDrivesPage {

    private final WebDriver driver;

    public DriverCurrentDrivesPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "goTo0")
    WebElement goTo;

    @FindBy(id = "start0")
    WebElement start;

    @FindBy(id = "finish0")
    WebElement finish;

    @FindBy(id = "stop0")
    WebElement stop;

    @FindBy(id = "reject0")
    WebElement reject;

    @FindBy(id = "toast-container")
    WebElement stopToast;

    @FindBy(id = "toast-container")
    WebElement finishToast;

    public void clickGoToClient(){
        goTo.click();
    }

    public void clickStart(){
        start.click();
    }

    public void clickStop(){
        stop.click();
    }

    public void clickFinish(){
        finish.click();
    }

    public void clickReject(){
        reject.click();
    }


    public boolean driveStopped(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(stopToast,"Drive stopped!"));
    }

    public boolean driveFinished(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(finishToast,"Drive finished!"));
    }
}
