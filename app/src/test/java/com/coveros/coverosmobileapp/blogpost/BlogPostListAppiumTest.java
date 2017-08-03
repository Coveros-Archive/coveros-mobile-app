package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.AndroidDriver;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests that the List title is the same as the blog title
 * Created by SRynestad on 7/31/2017.
 */

public class BlogPostListAppiumTest {
    public AndroidDriver driver;
    private static final String PROPERTIES_FILE = "appium.properties";

    @Before
    public void setUp() throws Exception {

        Properties appiumProperties = new Properties();
        InputStream in = BlogPostReadActivityAppiumTest.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        appiumProperties.load(in);
        in.close();

        final String deviceName = appiumProperties.getProperty("deviceName");
        final String platformVersion = appiumProperties.getProperty("platformVersion");

        System.out.println("deviceName:" + deviceName);
        System.out.println("platformVersion:" + platformVersion);


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("appPackage", "com.coveros.coverosmobileapp");
        capabilities.setCapability("appActivity", ".blogpost.BlogPostsListActivity");

        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @After
    public void tearDown() throws Exception{
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    public void titlesMatch_listTitleToBlogTitle(){
        WebElement listItem = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]");
        String listTitle = listItem.findElement(By.id("com.coveros.coverosmobileapp:id/title")).getText();
        listItem.click();
        WebElement blogItem = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView");
        String blogTitle = blogItem.getText();
        assertThat(listTitle, equalTo(blogTitle));

    }
}
