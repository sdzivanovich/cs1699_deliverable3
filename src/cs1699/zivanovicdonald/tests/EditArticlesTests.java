package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.hamcrest.CoreMatchers.*;

/* Feature:
 * As a user ( not logged in)
 * I want to review and edit articles
 * so that I can update the contents of an article with new or more-correct information as well as preview such changes.
 */
public class EditArticlesTests
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
    //Scenario: Semi-Protected Page
    public void testSemiProtectedPage() 
    {
        //Given an existing Semi-Protected page
        //And a user that is not logged in
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("cat");
        driver.findElement(By.id("searchButton")).click();

        //When the user attempts to edit the page
        //Then the user should not be able to click "edit"
        assertFalse(isElementPresent(By.linkText("Edit")));

        driver.findElement(By.linkText("View source")).click();

        //And the user should see a prompt concerning it being semi-protected in the View Source link
        assertEquals("This page is currently semi-protected so that only established registered users can edit it.", driver.findElement(By.cssSelector("span.mbox-text-span")).getText());

    }//END semiProtectedPageTest()

    @Test
    //Scenario: Non-Protected Page
    public void testNonProtectedPage() 
    {
        //Given an existing Non-Protected page
        //And a user that is not logged in
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("almond butter");
        driver.findElement(By.id("searchButton")).click();

        //When the user attempts to edit the page
        driver.findElement(By.linkText("Edit")).click();

        //Then the user should arrive at the "editing" page
        assertEquals("Editing Almond butter", driver.findElement(By.id("firstHeading")).getText());

        //And the user should see a "Save page" button so that they may save their changes
        assertTrue(isElementPresent(By.id("wpSave")));
        assertEquals("Save page", driver.findElement(By.id("wpSave")).getAttribute("value"));

    }//END nonProtectedPage()

    @Test
    //Scenario: Preview Change
    public void testPreviewChange() 
    {
        //Given an existing page
        //And a user that is not logged in
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null pointer");
        driver.findElement(By.id("searchButton")).click();

        //When the user edits the page
        driver.findElement(By.linkText("Edit")).click();

        //Then the user should be able to click "Show Preview" in order to preview their change
        assertTrue(isElementPresent(By.id("wpPreview")));

        driver.findElement(By.id("wpPreview")).click();
        assertEquals("This is only a preview; your changes have not yet been saved!", driver.findElement(By.cssSelector("strong")).getText());

        String butts = "Null Pointers like big butts and they cannot lie.";

        String oldText = driver.findElement(By.id("wpTextbox1")).getText();
        assertThat(oldText, not(containsString(butts)));

        driver.findElement(By.id("wpTextbox1")).sendKeys(oldText + butts);

        driver.findElement(By.id("wpPreview")).click();

        String newText = driver.findElement(By.id("wpTextbox1")).getText();

        //And, after clicking, should be able to see the difference within the preview
        assertNotEquals(oldText, newText);
        assertThat(newText, containsString(butts));

    }//END previewChangeTest()

    @Test
    //Scenario: Show Changes
    public void testShowChanges()
    {
        //Given an existing page
        //And a user that is not logged in
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null pointer");
        driver.findElement(By.id("searchButton")).click();

        //When the user edits the page
        driver.findElement(By.linkText("Edit")).click();

        //Then the page should direct to its "Editing" page
        assertEquals("Editing Null pointer", driver.findElement(By.id("firstHeading")).getText());

        //And the user should be able to click "Show Changes" in order to compare their changes
        assertTrue(isElementPresent(By.id("wpDiff")));

        String butts = "Null Pointers like big butts and they cannot lie.";

        String beforeMod = driver.findElement(By.id("wpTextbox1")).getText();
        assertThat(beforeMod, not(containsString(butts)));

        driver.findElement(By.id("wpTextbox1")).clear();
        driver.findElement(By.id("wpTextbox1")).sendKeys(butts + beforeMod);

        driver.findElement(By.id("wpDiff")).click();

        String oldText = driver.findElement(By.cssSelector("td.diff-deletedline > div")).getText();
        String newText = driver.findElement(By.cssSelector("td.diff-addedline > div")).getText();

        //And, after clicking, should be able to see the differences within the comparison being shown
        assertNotEquals(oldText, newText);
        assertThat(oldText, not(containsString(butts)));
        assertThat(newText, containsString(butts));

    }//END testShowChanges()

    @Test
    //Scenario: View History
    public void testViewHistory()
    {
        //Given an existing page
        //And a user that is not logged in
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();

        //When the user navigates to the page
        driver.findElement(By.id("searchInput")).sendKeys("cats");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("View history")).click();

        //Then the user should arrive at the "Revision history" page
        assertEquals("Cat: Revision history", driver.findElement(By.id("firstHeading")).getText());

        //And have the ability to filter the history
        assertTrue(isElementPresent(By.id("mw-history-search")));
        driver.findElement(By.id("year")).clear();
        driver.findElement(By.id("year")).sendKeys("2014");
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.linkText("04:24, 24 October 2014")).click();

        //And the user should be able to see the edit history for that particular page
        assertThat(driver.findElement(By.cssSelector("b")).getText(), containsString("This is an old revision of this page"));

    }//END testViewHistory()

    @Test
    //Scenario: Disambiguation Edit
    public void testDisambiguationEdit()
    {
        //Given a generic item to search for
        //And a disambiguation page pertaining to the item
        //When the user arrives at the disambiguation page
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("Edit")).click();

        //Then the user should arrive at the "editing" page
        assertEquals("Editing Null", driver.findElement(By.id("firstHeading")).getText());

        //And the page should explicitly say that the user is editing the disambiguation page
        assertThat(driver.findElement(By.cssSelector("td.mbox-text")).getText(), containsString("This is not an article; this is a disambiguation page, for directing readers quickly to intended articles."));

        //And the user should be able to edit that page
        assertTrue(isElementPresent(By.id("wpSave")));

    }//END testDisambiguationEdit()


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


}//END EditArticlesTest CLASS////////////////////////////////////////////////////////////////
