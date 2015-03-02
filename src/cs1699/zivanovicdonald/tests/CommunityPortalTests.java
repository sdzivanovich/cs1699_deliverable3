package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/* Feature:
 * As a user (with or without an account)
 * I want to interact with others by asking questions and viewing helpful content
 * So that I can have a much easier time navigating Wikipedia
 */
public class CommunityPortalTests
{
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp()
    {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        baseUrl = "http://www.wikipedia.org/";

    }//END setUp()

    @After
    public void tearDown()
    {
        driver.quit();

    }//END tearDown()

    @Test
    //Scenario: Community Help Desk
    public void testHelpDesk()
    {
        //Given a user
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.cssSelector("a[title=\"About the project, what you can do, where to find things\"]")).click();

        //When the user needs help regarding how to use the site
        driver.findElement(By.linkText("Help desk")).click();
        driver.findElement(By.cssSelector("div.center")).click();

        //Then the user should be able to navigate to the Help Desk
        assertEquals("Wikipedia:Help desk", driver.findElement(By.id("firstHeading")).getText());

        //And be able to click to post a new question
        assertEquals("Click here to ask a new question about how to use or edit Wikipedia »", driver.findElement(By.cssSelector("span > b")).getText());
        driver.findElement(By.cssSelector("span > b")).click();

        //And then be able to click "Save page" after typing their question to post it
        assertEquals("Editing Wikipedia:Help desk (new section)", driver.findElement(By.id("firstHeading")).getText());
        assertTrue(isElementPresent(By.id("wpSave")));
        assertEquals("", driver.findElement(By.id("wpSave")).getText());

    }//END testHelpDesk()

    @Test
    //Scenario: Community Reference Desk
    public void testReferenceDesk() 
    {
        //Given a user at the front page
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).click();
        driver.findElement(By.cssSelector("a[title=\"About the project, what you can do, where to find things\"]")).click();

        //When the user needs help regarding a particular subject matter
        driver.findElement(By.linkText("Reference desk")).click();

        //Then the user should be able to navigate to the Reference Desk
        assertEquals("Wikipedia:Reference desk", driver.findElement(By.id("firstHeading")).getText());

        //And then be able to click a category for their question
        assertEquals("Computers and IT", driver.findElement(By.linkText("Computers and IT")).getText());
        driver.findElement(By.linkText("Computers and IT")).click();

        //And the user should be able to click to post a new question
        assertTrue(isElementPresent(By.name("create")));
        driver.findElement(By.name("create")).click();

        //And be able to click "Save page" after typing their qeustion to post it
        assertTrue(isElementPresent(By.id("wpSave")));

    }//END testReferenceDesk()

    @Test
    //Scenario: Reference Look-Up
    public void testReferenceDeskLookUp()
    {
        //Given a user at the front page
        driver.get(baseUrl + "/");

        //When the user needs help regarding a particular subject matter
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.cssSelector("a[title=\"About the project, what you can do, where to find things\"]")).click();

        //Then the user should be able to navigate to the Reference Desk
        assertTrue(isElementPresent(By.linkText("Reference desk")));
        driver.findElement(By.linkText("Reference desk")).click();
        assertEquals("Wikipedia:Reference desk", driver.findElement(By.id("firstHeading")).getText());
        driver.findElement(By.id("bodySearchInputMP")).click();

        //And the user should be able to search Wikipedia as a whole from the Reference Desk
        assertTrue(isElementPresent(By.id("bodySearchInputMP")));
        assertTrue(isElementPresent(By.name("go")));

        //And the user should be able to search the reference desk archives specifically
        assertTrue(isElementPresent(By.cssSelector("p > input[name=\"search\"]")));
        assertTrue(isElementPresent(By.name("fulltext")));

        driver.findElement(By.cssSelector("p > input[name=\"search\"]")).clear();
        driver.findElement(By.cssSelector("p > input[name=\"search\"]")).sendKeys("cats");
        driver.findElement(By.name("fulltext")).click();

        //And searching the archives should return a "Search Results" page
        assertEquals("Search results", driver.findElement(By.id("firstHeading")).getText());

    }//END testReferenceDeskLookUp()


    @Test
    //Scenario: Community Teahouse
    public void testTeaHouse() 
    {
        //Given a user at the front page
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.cssSelector("a[title=\"About the project, what you can do, where to find things\"]")).click();
        //When the user needs help regarding how to edit pages
        driver.findElement(By.linkText("Peer editing help")).click();

        //Then the user should be able to navigate to the Teahouse page
        assertEquals("Wikipedia:Teahouse/Questions", driver.findElement(By.id("firstHeading")).getText());

        //And the user should be able to post a question at the Teahouse
        assertTrue(isElementPresent(By.cssSelector("span.mw-ui-button.mw-ui-progressive")));
        driver.findElement(By.cssSelector("span.mw-ui-button.mw-ui-progressive")).click();
        driver.findElement(By.id("wp-th-question-title")).clear();
        driver.findElement(By.id("wp-th-question-title")).sendKeys("I like cats");
        driver.findElement(By.id("wp-th-question-text")).clear();
        driver.findElement(By.id("wp-th-question-text")).sendKeys("And I like dogs\n\n~~~~");

        //And, once they type their question, they can click "Ask my question" to post it
        assertTrue(isElementPresent(By.cssSelector("span.ui-button-text")));

    }//END testTeaHouse()

    @Test
    //Scenario: Community Village Pump
    public void testVillagePump() 
    {
        //Given a user at the front page
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();

        //When the user wishes to discuss issues concerning Wikipedia
        driver.findElement(By.cssSelector("a[title=\"About the project, what you can do, where to find things\"]")).click();

        //Then the user should be able to navigate to the Village pump page
        assertTrue(isElementPresent(By.linkText("Village pump")));
        driver.findElement(By.linkText("Village pump")).click();

        //And the user should be able to select a particular category
        assertTrue(isElementPresent(By.linkText("Miscellaneous")));
        driver.findElement(By.linkText("Miscellaneous")).click();

        //And the user should be able to create a new post
        assertTrue(isElementPresent(By.linkText("New post")));
        driver.findElement(By.linkText("New post")).click();

        //And then the user should be able to click "Save page" to post it
        assertTrue(isElementPresent(By.id("wpSave")));

    }//END testVillagePump()

    private boolean isElementPresent(By by) 
    {
        try 
        {
            driver.findElement(by);
            return true;
        } 
        catch (NoSuchElementException e) 
        {
            return false;
        }

    }//END isElementPresent()


}
