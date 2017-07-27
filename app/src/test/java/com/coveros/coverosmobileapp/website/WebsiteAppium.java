package com.coveros.coverosmobileapp.website;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.coveros.coverosmobileapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import io.appium.java_client.android.AndroidDriver;

public class WebsiteAppium {

    private AndroidDriver driver;

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
    public void Click_Home_SecureCI(){
        driver.context("WEBVIEW");
        driver.findElementByXPath("//*[@id=\"text-41\"]/div/center/a/h5").click();
        //Assert SecureCI web page is loaded
    }

    /*
     * WebView in MainActivity -> Click Twitter  (browser opens Twitter)
     */
    @Test
    public void Click_External_Content(){
        driver.context("WEBVIEW");
        driver.findElementByXPath("//*[@id=\"text-11\"]/div/p/a[2]/img");
        //Assert Twitter / Browser is open instead of WebView
    }

    /*
     * WebView in MainActivity -> Click Menu (Drawer is open)
     */
    @Test
    public void Click_Menu() throws InterruptedException {
        Thread.sleep(5000);
        driver.context("WEBVIEW");
        driver.findElementByClassName("open-responsive-menu").click();
        //Assert drawer is open
        driver.findElementByClassName("open-responsive-menu").click();
        //Assert drawer is closed
    }

    /*
     * WebView in MainActivity -> Click Menu -> Click Blog (Opens BlogList)
     */
    @Test
    public void Click_BlogList_Menu(){
        driver.context("WEBVIEW");
        driver.findElementByClassName("open-responsive-menu").click();


        //Assert BlogList Activity is running and BlogList is open
    }


    /*
     * WebView in MainActivity -> Click "Read More" (Opens Blog Post)
     */
    @Test
    public void Click_Blog_ReadMore(){
        driver.context("WEBVIEW");


        //Assert Blog Post is open
    }

    /*
     * WebView in MainActivity -> Click Blog Title (Opens Blog Post)
     */
    @Test
    public void Click_Blog_Title(){
        driver.context("WEBVIEW");


        //Assert Blog Post is open
    }
}