import com.paulhammant.ngwebdriver.ByAngularModel;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class ToDoItemPage {
    //in the first two elements, i have used an extension of selenium web driver that is suited to be used with Angular applications
    @ByAngularModel.FindBy(model = "newTodo")
    private WebElement txtNewTodo;

    @ByAngularModel.FindBy(model = "todo.completed")
    private WebElement txtEditItem;


    @FindBy(xpath = "//ul[@class='todo-list']")
    private WebElement divAllItems;


    @FindBy(xpath = "//label[@ng-dblclick='editTodo(todo)']")
    private WebElement lblEditItem;



    ChromeDriver myDriver = null;
    NgWebDriver ngWebDriver;
    WebDriverWait myDriverWait;

    public ToDoItemPage(WebDriver driver) {
        myDriver = (ChromeDriver) driver;
        PageFactory.initElements(driver, this);
        ngWebDriver = new NgWebDriver(myDriver);
        ngWebDriver.waitForAngularRequestsToFinish();
        myDriverWait = new WebDriverWait(driver, 20);
    }

    public void AddNewToDo(String itemName) throws Exception {
        WebElement chkSuccess = null;
        try {
            txtNewTodo.sendKeys(itemName);
            txtNewTodo.sendKeys(Keys.ENTER);
            ngWebDriver.waitForAngularRequestsToFinish();
            chkSuccess = myDriver.findElement(By.xpath("//label[text()='" + itemName + "']"));

            chkSuccess = myDriverWait.until(ExpectedConditions.visibilityOf(chkSuccess));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Assert.assertTrue(chkSuccess.isDisplayed());
    }

    public void MarkAllItemsCompleted() {
        try {
            List<WebElement> itemsLst = divAllItems.findElements(By.xpath("//li/descendant::input[@type='checkbox']"));
            for (WebElement item : itemsLst) {
                item.click();
                ngWebDriver.waitForAngularRequestsToFinish();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void MarkTodoItemCompleted(String itemName) throws Exception {
        WebElement chSuccess = null;
        try {
            WebElement chkItemName = myDriver.findElement(By.xpath("//label[text()='" + itemName + "']/preceding-sibling::input"));
            chkItemName.click();

            chSuccess = myDriver.findElement(By.xpath("//label[text()='" + itemName + "']/parent::div/parent::li[@class='ng-scope completed']"));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Assert.assertTrue(chSuccess.isDisplayed());

    }

    public void RemoveToDoItem(String itemName) throws Exception {
        WebElement chkSuccess=null;
        try {

            WebElement lblItem = myDriver.findElement(By.xpath("//label[text()='" + itemName + "']"));

            Actions mouseActions = new Actions(myDriver);
            mouseActions.moveToElement(lblItem).moveToElement(lblItem).click().build().perform();

            WebElement btnRemoveItem = myDriver.findElement(By.xpath("//label[text()='" + itemName + "']/following-sibling::button"));

            btnRemoveItem.click();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //Check item is no longer exists
        Assert.assertTrue(!exists(By.xpath("//label[text()='"+itemName+"']")));

    }

    public void CheckItemsCount(int itemCount) throws Exception {
        int actualCount = 0;
        WebElement lblCount;
        try {

            lblCount = myDriver.findElement(By.xpath("//span[@class='todo-count']/strong"));
            if(exists(By.xpath("//span[@class='todo-count']/strong")))
            {
                actualCount = Integer.parseInt(lblCount.getText());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //Check item is no longer exists
        Assert.assertEquals(itemCount,actualCount);

    }

    public boolean exists(By locatorKey) {
        try {
            myDriver.findElement(locatorKey);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

}
