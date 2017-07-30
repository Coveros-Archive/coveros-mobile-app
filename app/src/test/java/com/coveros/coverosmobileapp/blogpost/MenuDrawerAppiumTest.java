package com.coveros.coverosmobileapp.blogpost;

import android.widget.ListView;

import com.coveros.coverosmobileapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by SRynestad on 7/27/2017.
 */

public class MenuDrawerAppiumTest {
    private AndroidDriver driver;
    public void swipe() throws InterruptedException
    {
            //Get the size of screen.
            Dimension size = driver.manage().window().getSize();
            System.out.println(size);

            //Find swipe start and end point from screen's with and height.
            //Find startx point which is at right side of screen.
            int startx = (int) (size.width * 0.70);
            //Find endx point which is at left side of screen.
            int endx = (int) (size.width * 0.30);
            //Find vertical point where you wants to swipe. It is in middle of screen height.
            int starty = size.height / 2;
            System.out.println("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);
            //Swipe from Left to Right.
            driver.swipe(endx, starty, startx, starty, 3000);
            Thread.sleep(2000);
    }
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Nexus 6 API 25");
        capabilities.setCapability("platformVersion", "7.1.1");
        capabilities.setCapability("appPackage", "com.coveros.coverosmobileapp");
        capabilities.setCapability("appActivity", ".website.MainActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @After
    public void tearDown(){
        driver.quit();
    }
    @Test
    public void menuDrawer_slidesOpenInWebview() throws InterruptedException{
        Set<String> contextNames = driver.getContextHandles();
        System.out.println(contextNames.toString());
     for (String contextName : contextNames) {
           if (contextName.contains("WEBVIEW")) {
                driver.context(contextName);
                System.out.println(driver.getContext());
            }
        }
        swipe();

    }
    @Test
    public void menuDrawer_slidesOpenInListOfBlogPosts() throws InterruptedException{
        driver.context("WEBVIEW");
        swipe();
        driver.findElementByName("Blog").click();
        swipe();
        WebElement drawer = driver.findElement(By.id("left_drawer"));
        assertThat(drawer.isDisplayed(), is(true));
    }
}
