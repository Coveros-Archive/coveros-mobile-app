package com.coveros.coverosmobileapp.website;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import io.appium.java_client.android.AndroidDriver;

import static junit.framework.Assert.assertEquals;

public class WebsiteAppium {

    private AndroidDriver driver;
    private String appPackage = "com.coveros.coverosmobileapp";

    @Before
    public void setUp() throws MalformedURLException{
        try{
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "7.1.1");
            capabilities.setCapability("deviceName", "Pixel");
            capabilities.setCapability("appPackage", "com.coveros.coverosmobileapp");
            capabilities.setCapability("appActivity", ".website.MainActivity");
            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException m){
            m.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    /*
     * WebView in MainActivity -> Click SecureCI (open secureCI)
     */
    @Test
    public void Click_Home_SecureCI() throws InterruptedException {
        String expectedUrl = "https://www3.dev.secureci.com/products/secure-ci/";
        driver.context("WEBVIEW_" + appPackage);
        driver.findElementByXPath("//*[@id=\"text-41\"]/div/center/a/h5").click();
        Thread.sleep(5000);
        String currentUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, currentUrl);
    }

    /*
     * WebView in MainActivity -> Click Menu -> Click Blog (Opens BlogList)
     */
    @Test
    public void Click_BlogList_Menu() throws InterruptedException {
        driver.context("WEBVIEW_" + appPackage);
        Thread.sleep(10000);     //Delay so the menu finishes loading
        driver.findElementByClassName("open-responsive-menu").click();
        Thread.sleep(5000);
        //Add some click to menu item here
        Thread.sleep(5000);
        //assertEquals(".blogpost.BlogPostListActivity", driver.currentActivity());
    }

    /*
     * WebView in MainActivity -> Click "Read More" (Opens Blog Post)
     */
    @Test
    public void Click_Blog_ReadMore() throws InterruptedException {
        driver.context("WEBVIEW_" + appPackage);
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//*[@id=\"latest-news\"]/div[2]/div/div/div[1]/div/div[3]/div/div/a")));
        driver.findElementByXPath("//*[@id=\"latest-news\"]/div[2]/div/div/div[1]/div/div[3]/div/div/a").click();
        Thread.sleep(5000);
        assertEquals(".blogpost.BlogPostReadActivity", driver.currentActivity());
    }

    /*
     * WebView in MainActivity -> Click Blog Title (Opens Blog Post)
     */
    @Test
    public void Click_Blog_Title() throws InterruptedException {
        driver.context("WEBVIEW_" + appPackage);
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//*[@id=\"latest-news\"]/div[2]/div/div/div[1]/div/div[3]/div/div/a")));
        driver.findElementByXPath("//*[@id=\"latest-news\"]/div[2]/div/div/div[1]/div/div[3]/div/div/h5/a").click();
        Thread.sleep(5000);
        assertEquals(".blogpost.BlogPostReadActivity", driver.currentActivity());
    }
}