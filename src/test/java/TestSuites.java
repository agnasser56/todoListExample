
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;


public class TestSuites {
    static ChromeDriver driver = new ChromeDriver();
    static ToDoItemPage todoItemPage = new ToDoItemPage(driver);
    static NgWebDriver ngWebDriver;

    @BeforeClass
    public void InitialzeTestEnv() {
        driver.get("http://todomvc.com/examples/angularjs/#/");
        driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
        ngWebDriver = new NgWebDriver(driver);
        ngWebDriver.waitForAngularRequestsToFinish();
    }


    @DataProvider
    public Object[][] getToDoItemList() {
        return new Object[][]{
                {"item", "item 1"},
                {"item", "item 2"}

        };
    }

    @Test
    public static void AddTodoItem(String item, String itemName) {
        try {
            todoItemPage.AddNewToDo(itemName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add multiple items using data provider
    @Test(dataProvider = "getToDoItemList")
    public static void AddMultipleTodoItems(String item, String itemName) {
        try {
            todoItemPage.AddNewToDo(itemName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(dataProvider = "getToDoItemList")
    public static void MarkAllTodoItemCompleted(String item, String itemName) throws InterruptedException {
        Thread.sleep(20000);
        todoItemPage.MarkAllItemsCompleted();
    }

    @Test(description = "This test suite will add an item, Check for updated count, mark it completed , then delete it and lastly check the count")
    public static void HappyScenario() {
        try {
            String itemName = "item 1";
            todoItemPage.AddNewToDo(itemName);
            todoItemPage.CheckItemsCount(1);
            todoItemPage.MarkTodoItemCompleted(itemName);
            todoItemPage.RemoveToDoItem(itemName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public void Teardown() {
        driver.quit();
    }
}
