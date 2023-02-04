package nvt.kts.project.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

}
