package cs1699.zivanovicdonald.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.hamcrest.CoreMatchers.*;

public class EditArticlesTests
{

    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception 
    {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        baseUrl = "http://www.wikipedia.org/";

    }//END setUp()

    @Test
    public void semiProtectedPageTest() throws Exception 
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("cat");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("View source")).click();
        assertEquals("This page is currently semi-protected so that only established registered users can edit it.", driver.findElement(By.cssSelector("span.mbox-text-span")).getText());

    }//END semiProtectedPageTest()

    @Test
    public void nonProtectedPage() throws Exception 
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("almond butter");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("Edit")).click();
        assertTrue(isElementPresent(By.id("wpSave")));
        assertEquals("Save page", driver.findElement(By.id("wpSave")).getAttribute("value"));

    }//END nonProtectedPage()

    @Test
    public void previewChangeTest() throws Exception 
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null pointer");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("Edit")).click();

        assertTrue(isElementPresent(By.id("wpPreview")));

        driver.findElement(By.id("wpPreview")).click();
        assertEquals("This is only a preview; your changes have not yet been saved!", driver.findElement(By.cssSelector("strong")).getText());

        String butts = "Null Pointers like big butts and they cannot lie.";

        String oldText = driver.findElement(By.id("wpTextbox1")).getText();
        assertThat(oldText, not(containsString(butts)));

        driver.findElement(By.id("wpTextbox1")).sendKeys(oldText + butts);

        driver.findElement(By.id("wpPreview")).click();

        String newText = driver.findElement(By.id("wpTextbox1")).getText();

        assertNotEquals(oldText, newText);
        assertThat(newText, containsString(butts));

    }//END previewChangeTest()

    @Test
    public void testShowChanges()
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null pointer");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("Edit")).click();

        assertEquals("Editing Null pointer", driver.findElement(By.id("firstHeading")).getText());

        assertTrue(isElementPresent(By.id("wpDiff")));

        String butts = "Null Pointers like big butts and they cannot lie.";

        String beforeMod = driver.findElement(By.id("wpTextbox1")).getText();
        assertThat(beforeMod, not(containsString(butts)));

        driver.findElement(By.id("wpTextbox1")).clear();
        driver.findElement(By.id("wpTextbox1")).sendKeys(butts + beforeMod);

        driver.findElement(By.id("wpDiff")).click();

        String oldText = driver.findElement(By.cssSelector("td.diff-deletedline > div")).getText();
        String newText = driver.findElement(By.cssSelector("td.diff-addedline > div")).getText();

        assertNotEquals(oldText, newText);
        assertThat(oldText, not(containsString(butts)));
        assertThat(newText, containsString(butts));

    }//END testShowChanges()

    @Test
    public void testViewHistory()
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("cats");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("View history")).click();
        assertEquals("Cat: Revision history", driver.findElement(By.id("firstHeading")).getText());
        assertTrue(isElementPresent(By.id("mw-history-search")));
        driver.findElement(By.id("year")).clear();
        driver.findElement(By.id("year")).sendKeys("2014");
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.linkText("04:24, 24 October 2014")).click();
        assertThat(driver.findElement(By.cssSelector("b")).getText(), containsString("This is an old revision of this page"));

    }//END testViewHistory()

    @Test
    public void testDisambiguationEdit()
    {
        driver.get(baseUrl + "/");
        driver.findElement(By.cssSelector("strong")).click();
        driver.findElement(By.id("searchInput")).click();
        driver.findElement(By.id("searchInput")).clear();
        driver.findElement(By.id("searchInput")).sendKeys("null");
        driver.findElement(By.id("searchButton")).click();
        driver.findElement(By.linkText("Edit")).click();
        assertEquals("Editing Null", driver.findElement(By.id("firstHeading")).getText());
        assertThat(driver.findElement(By.cssSelector("td.mbox-text")).getText(), containsString("This is not an article; this is a disambiguation page, for directing readers quickly to intended articles."));
        assertTrue(isElementPresent(By.id("wpSave")));

    }//END testDisambiguationEdit()


    @After
    public void tearDown() throws Exception 
    {
        driver.quit();

    }//END tearDown()

    
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
