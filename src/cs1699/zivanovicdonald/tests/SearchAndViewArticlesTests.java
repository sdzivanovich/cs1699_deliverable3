package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

/* Feature: 
 * As a user (with or without an account)
 * I want to search for and/or view articles
 * So that I may increase my knowledge
 */ 
public class SearchAndViewArticlesTests
{
    private WebDriver _driver;    

    @Before
    public void setUp() throws Exception 
    {
        _driver = new FirefoxDriver();
        _driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    // Scenario: Searching for an existing article
    public void searchForExistingArticle() 
    {       
        // Given the user is on the main page
        // And the name of an article
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // When the user searches for the name of the article
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys("cat");
        _driver.findElement(By.id("searchButton")).click();
     // Then the user should be taken to the article's page
        assertEquals("https://en.wikipedia.org/wiki/Cat", _driver.getCurrentUrl());
        assertEquals("Cat", _driver.findElement(By.id("firstHeading")).getText());
    }
    
    @Test
    //Scenario: Searching non-existent article
    public void searchForNonExistentArticle()
    {
        // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And a phrase that is not the name of an article
        String target = "rent a cat";
        // When the user searches for the phrase
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(target);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should see that an article for the phrase does not exist
        assertThat(_driver.findElement(By.cssSelector("i")).getText(),containsString("The page \"Rent a cat\" does not exist."));
        //And the user should see search results for the phrase        
        assertEquals("Search results", _driver.findElement(By.id("firstHeading")).getText());
    }
    
    @Test
    //Scenario: Viewing categories of articles
    public void searchForCategory()
    {
        // Given the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // And the name of a category
        String category = "Category:Religion";
        // When the user searches for the category
        _driver.findElement(By.id("searchInput")).click();
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys(category);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should see the page for the category   
        assertEquals(category, _driver.findElement(By.id("firstHeading")).getText());
        // And the user should see links to pages in the category
        assertEquals("Atheism and religion", _driver.findElement(By.linkText("Atheism and religion")).getText());
    }
    
    

    @After
    public void tearDown() throws Exception 
    {
        _driver.quit();
    }
}
