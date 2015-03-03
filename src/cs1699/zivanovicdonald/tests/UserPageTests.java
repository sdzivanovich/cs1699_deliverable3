package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class UserPageTests
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
    // Scenario: Viewing user page
    public void viewingUserPage() 
    {
        // Given Mansfieldatron is the name of a user on Wikipedia
        String name = "Mansfieldatron";
        // And the user is on the main page
        _driver.get("https://en.wikipedia.org/wiki/Main_Page");
        // When the user searches for the user name
        _driver.findElement(By.id("searchInput")).clear();
        _driver.findElement(By.id("searchInput")).sendKeys("user:" + name);
        _driver.findElement(By.id("searchButton")).click();
        // Then the user should see the user page for that user 
        assertEquals("https://en.wikipedia.org/wiki/User:" + name, _driver.getCurrentUrl());  
        assertEquals("User:" + name, _driver.findElement(By.id("firstHeading")).getText());     
    }

    @Test
    // Scenario: Editing user page
    public void editingUserPage()
    {
        // Given the user is logged in
        _driver.get("https://en.wikipedia.org/w/index.php?title=Special:UserLogin");
        _driver.findElement(By.id("wpName1")).click();
        _driver.findElement(By.id("wpName1")).clear();
        _driver.findElement(By.id("wpName1")).sendKeys("cs1699_test1");
        _driver.findElement(By.id("wpPassword1")).clear();
        _driver.findElement(By.id("wpPassword1")).sendKeys("cs1699_test1");
        _driver.findElement(By.id("wpLoginAttempt")).click();
        // And the user is on their user page
        _driver.findElement(By.linkText("Cs1699 test1")).click();
        // And their user page has some content
        String originalContent = _driver.findElement(By.cssSelector("p")).getText();
        // When the user edits their user page
        _driver.findElement(By.linkText("Edit")).click();
        _driver.findElement(By.id("wpTextbox1")).clear();
        _driver.findElement(By.id("wpTextbox1")).sendKeys("Hi everyone! This is my user page! Hahaha!");
        _driver.findElement(By.id("wpSave")).click();
        // Then the content on their user page should be updated
        String newContent = _driver.findElement(By.cssSelector("p")).getText();
        assertNotEquals(newContent, originalContent);
        assertEquals("Hi everyone! This is my user page! Hahaha!", _driver.findElement(By.cssSelector("p")).getText());

        // Clean-up
        _driver.findElement(By.linkText("Edit")).click();
        _driver.findElement(By.id("wpTextbox1")).clear();
        _driver.findElement(By.id("wpTextbox1")).sendKeys("x");
        _driver.findElement(By.id("wpSave")).click();
    }

    @Test
    // Scenario: Viewing user contributions
    public void viewingUserContributions()
    {
        // Given Mansfieldatron is the name of another user
        String username = "Mansfieldatron";
        // And the user is on the other user's user page
        _driver.get("https://en.wikipedia.org/wiki/User:" + username);
        // When the user clicks on the User contributions link
        _driver.findElement(By.linkText("User contributions")).click();
        // Then the user should see the contributions made by the other user
        assertEquals("https://en.wikipedia.org/wiki/Special:Contributions/" + username, _driver.getCurrentUrl());
        assertTrue(isElementPresent(By.cssSelector("ul.mw-contributions-list")));      
    }
    
    
    @Test
    // Scenario: Posting message to another user
    public void postingMessageToAnotherUser()
    {
        // Given the user is logged in
        _driver.get("https://en.wikipedia.org/w/index.php?title=Special:UserLogin");        
        _driver.findElement(By.id("wpName1")).clear();
        _driver.findElement(By.id("wpName1")).sendKeys("cs1699_test1");
        _driver.findElement(By.id("wpPassword1")).clear();
        _driver.findElement(By.id("wpPassword1")).sendKeys("cs1699_test1");
        _driver.findElement(By.id("wpLoginAttempt")).click();
        // And Mansfieldatron is the name of another user
        String username = "Mansfieldatron";
        // When a user navigates to the other user's page
        _driver.get("https://en.wikipedia.org/wiki/User:" + username);
        // And clicks on the Talk link
        _driver.findElement(By.cssSelector("a[title=\"Discussion about the content page [Alt+Shift+t]\"]")).click();
        // And clicks on the Edit link
        _driver.findElement(By.linkText("Edit")).click();
        // Then the user should be able to modify the talk page
        assertEquals("https://en.wikipedia.org/w/index.php?title=User_talk:Mansfieldatron&action=edit", _driver.getCurrentUrl());        
        assertTrue(isElementPresent(By.id("wpTextbox1")));
        _driver.findElement(By.id("wpSave")).isEnabled();
    }
    
    @Test
    // Scenario: Viewing user page history
    public void viewingUserPageHistory()
    {
        // Given Mansfieldatron the name of a user
        String name = "Mansfieldatron";
        // When the user navigates to this user's user page
        _driver.get("https://en.wikipedia.org/w/index.php?title=User:" + name);
        // And clicks on the View history link
        _driver.findElement(By.linkText("View history")).click();
        // Then the user should see this user's user page revisions
        String targetPage = "https://en.wikipedia.org/w/index.php?title=User:" + name + "&action=history";
        assertEquals(targetPage, _driver.getCurrentUrl());
        assertTrue(isElementPresent(By.id("pagehistory")));
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
