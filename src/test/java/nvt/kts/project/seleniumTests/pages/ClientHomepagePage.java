package nvt.kts.project.seleniumTests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ClientHomepagePage {

    private final WebDriver driver;

    @FindBy(id = "addDestination")
    WebElement description;

    @FindBy(id = "start")
    WebElement startInput;

    @FindBy(id = "end")
    WebElement endInput;

    @FindBy(id = "findButton")
    WebElement findButton;

    @FindBy(id = "chooseButton")
    WebElement chooseButton;

    @FindBy(id = "car_1")
    WebElement type;

    @FindBy(id = "swal2-html-container")
    WebElement alert;

    @FindBy(id = "clickNext")
    WebElement clickNext;

    @FindBy(id = "secondNext")
    WebElement secondNext;

    @FindBy(id = "thirdNext")
    WebElement thirdNext;

    @FindBy(id = "toast-container")
    WebElement approveToast;

    @FindBy(id = "addPin")
    WebElement addPin;

    @FindBy(id = "place_0")
    WebElement placeInput;

    @FindBy(id = "userInput")
    WebElement passengerInput;


    @FindBy(id = "addUser")
    WebElement addUser;



    public ClientHomepagePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setInputInfo(String start, String end) {
        startInput.clear();
        startInput.sendKeys(start);
        endInput.clear();
        endInput.sendKeys(end);
        findButton.click();
    }

    public boolean isPageOpened(){
        return (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.textToBePresentInElement(description, "Add destination"));
    }


    public void waitToClickChoose(){
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(chooseButton)).isEnabled();
    }

    public void clickChoose(){
        chooseButton.click();
    }


    public void selectCarType(){
        type.click();
        clickNext.click();
    }


    public void finishScheduling(){
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(thirdNext)).click();
    }

    public void clickNextSecondWindow(){
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(secondNext)).click();
    }


    public void clickAlert(){
        (new WebDriverWait(driver, Duration.ofSeconds(8)))
                .until(ExpectedConditions.visibilityOf(approveToast));
        approveToast.click();
    }

    public void addStation(String s) {
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(addPin)).click();
        placeInput.sendKeys(s);
    }

    public void addPassenger(String s) {
        passengerInput.sendKeys(s);
        (new WebDriverWait(driver, Duration.ofSeconds(15)))
                .until(ExpectedConditions.elementToBeClickable(addUser)).click();
    }
}
