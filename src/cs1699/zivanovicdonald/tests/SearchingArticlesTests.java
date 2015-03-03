package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

/* Feature: 
 * As a user (with or without an account)
 * I want to search for and find articles
 * So that I may increase my knowledge
 */ 
public class SearchingArticlesTests
{
    private WebDriver _driver;    

    @Before
    public void setUp()
    {
        _driver = new FirefoxDriver();
        _driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown()
    {
        _driver.quit();
    }

    @Test
    // Scenario: Searching for an existing article
    public void searchingForExistingArticle() 
    {       
        // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And "Cat" is the name of an article
        String articleName = "Cat";
        // When the user searches for the name of the article
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(articleName);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should be taken to the article's page
        assertEquals("https://en.wikipedia.org/wiki/" + articleName, _driver.getCurrentUrl());
        assertEquals(articleName, _driver.findElement(By.id("firstHeading")).getText());
    }

    @Test
    //Scenario: Searching non-existent article
    public void searchingForNonExistentArticle()
    {
        // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And "Rent a cat" is not the name of an article
        String target = "Rent a cat";
        // When the user searches for the phrase
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(target);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should see that an article for the phrase does not exist
        String targetMessage = "The page \"" + target + "\" does not exist.";
        assertThat(_driver.findElement(By.cssSelector("i")).getText(), containsString(targetMessage));
        //And the user should see search results for the phrase        
        assertEquals("Search results", _driver.findElement(By.id("firstHeading")).getText());
    }
    
    @Test
    //Scenario: Linking to other articles within article
    public void linkingToOtherArticlesWithinArticle()
    {
        // Given the user is on the article page for "Cat"
        _driver.get("https://en.wikipedia.org/wiki/Cat");
        // And the article page contains links to other Wikipedia articles
        List<WebElement> links = _driver.findElements(By.cssSelector("#mw-content-text > p > a"));
        assertFalse(links.isEmpty());
        // When the user clicks on a link within the article
        WebElement link = links.get(0);
        String name = link.getAttribute("title");
        link.click();
                
        // Then the user should be taken to the page for the clicked-on article
        assertEquals("https://en.wikipedia.org/wiki/" + name, _driver.getCurrentUrl());
    }
    
    @Test
    //Scenario: Searching for portal
    public void searchingForPortal()
    {
     // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And "Portal:Mathematics" is the name of a portal
        String portal = "Portal:Mathematics";
        // When the user searches for the portal
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(portal);
        _driver.findElement(By.id("searchButton")).click();;
        // Then the user should be taken to the page for the clicked-on article
        assertEquals("https://en.wikipedia.org/wiki/" + portal, _driver.getCurrentUrl());  
        // And the portal page contains a selected article
        assertTrue(isElementPresent(By.id("Selected_article")));
    }

    @Test
    //Scenario: Searching for category
    public void searchingForCategory()
    {
        // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And "Category:Religion" is the name of a category
        String category = "Category:Religion";
        // When the user searches for the category
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(category);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should see the page for the category   
        assertEquals(category, _driver.findElement(By.id("firstHeading")).getText());
        // And the user should see links to pages in the category
        assertTrue(isElementPresent(By.id("mw-pages")));        
        List<WebElement> links = _driver.findElements(By.cssSelector("#mw-pages .mw-content-ltr a"));
        assertFalse(links.isEmpty());
        
        // (make sure linked page is actually in category)
        links.get(0).click();
        assertTrue(isElementPresent(By.cssSelector("a[title=\"" + category + "\"]")));
    }

    private boolean isElementPresent(By by) 
    {
        try 
        {
            _driver.findElement(by);
            return true;
        } 
        catch (NoSuchElementException e) 
        {
            return false;
        }
    }
}
