package com.coveros.coverosmobileapp.blogpost;

import android.widget.ListView;

import com.coveros.coverosmobileapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by SRynestad on 7/27/2017.
 */

public class MenuDrawerAppiumTest {
    private AndroidDriver driver;
    public void swipe()
    {
        JavascriptExecutor js = driver;
        HashMap<String, Double> swipeObject = new HashMap<String, Double>();
        swipeObject.put("startX", 0.95);
        swipeObject.put("startY", 0.5);
        swipeObject.put("endX", 0.05);
        swipeObject.put("endY", 0.5);
        swipeObject.put("duration", 1.8);
        js.executeScript("mobile: swipe", swipeObject);
    }
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Pixel");
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
    public void menuDrawer_slidesOpenInWebview(){
        driver.context("WEBVIEW");
        swipe();
        ListView expectedView;
        ListView actualView;
        //make an assertion
    }
    @Test
    public void menuDrawer_slidesOpenInListOfBlogPosts(){
        driver.context("WEBVIEW");
        swipe();
        driver.findElementByName("Blog").click();
        swipe();
        ListView expectedView;
        ListView actualView;
        //make an assertion
    }
}
