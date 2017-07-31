package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BlogPostReadActivityAppiumTest {

    private static final String PROPERTIES_FILE = "appium.properties";
    public AndroidDriver driver;

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

    @Test
    public void viewMultipleBlogPosts(){
        // click on the first three blog posts in the list
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.pressKeyCode(AndroidKeyCode.BACK);
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[2]").click();
        driver.navigate().back();
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[3]").click();
    }

    @Test
    public void viewComments() {
        // click on the first blog post in the list
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();

        String expectedCommentLabel = "Comments";
        String actualCommentLabel = driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.TextView").getText();

        assertThat(actualCommentLabel, equalTo(expectedCommentLabel));
    }

    @After
    public void tearDown() throws Exception{
        if (driver != null) {
            driver.quit();
        }
    }



}

