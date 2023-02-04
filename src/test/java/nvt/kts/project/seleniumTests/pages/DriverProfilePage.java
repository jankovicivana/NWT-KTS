package nvt.kts.project.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DriverProfilePage {

    private final WebDriver driver;

    public DriverProfilePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "currentDrives")
    WebElement currentDrives;

    public void clickCurrentDrives(){
        currentDrives.click();
    }

}
