package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;

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
    }

    @After
    public void tearDown() throws Exception 
    {
        _driver.quit();
    }
}
