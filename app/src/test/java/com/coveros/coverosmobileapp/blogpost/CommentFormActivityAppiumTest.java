package com.coveros.coverosmobileapp.blogpost;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommentFormActivityAppiumTest {
    public AndroidDriver driver;

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Genymotion 'Phone' version - 7.0");
        capabilities.setCapability("platformVersion", "7.0");
        capabilities.setCapability("appPackage", "com.coveros.coverosmobileapp");
        capabilities.setCapability("appActivity", ".blogpost.BlogPostsListActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void submitComment_withValidInput() {
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/leave_comment").click();

        driver.findElementById("com.coveros.coverosmobileapp:id/enter_name").sendKeys("Ryan Kenney");
        driver.findElementById("com.coveros.coverosmobileapp:id/enter_email").sendKeys("rtyklheartsdogs@gmail.com");
        driver.findElementById("com.coveros.coverosmobileapp:id/enter_message").sendKeys("Wonderful post!");

        driver.hideKeyboard();
        driver.findElementById("com.coveros.coverosmobileapp:id/send_button").click();

        // Just checking to see if a request is sent--result of request is dependent on network, so either a success or error response is acceptable
        String expectedSuccessAlertDialogMessage = "Your comment is now pending approval.";
        String expectedErrorAlertDialogMessage = "Oops! Something went wrong, and your comment was not sent. Please try again later.";
        String actualAlertDialogMessage = driver.findElementById("android:id/message").getText();

        assertThat(actualAlertDialogMessage, anyOf(equalTo(expectedSuccessAlertDialogMessage), equalTo(expectedErrorAlertDialogMessage)));
    }

    @Test
    public void submitComment_withInvalidEmail() {
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/leave_comment").click();

        driver.findElementById("com.coveros.coverosmobileapp:id/enter_name").sendKeys("Ryan Kenney");
        driver.findElementById("com.coveros.coverosmobileapp:id/enter_email").sendKeys("notAnEmail");
        driver.findElementById("com.coveros.coverosmobileapp:id/enter_message").sendKeys("Wonderful post!");

        driver.hideKeyboard();
        driver.findElementById("com.coveros.coverosmobileapp:id/send_button").click();

        String expectedInvalidEmailAlertDialogMessage = "Please provide a valid email.";
        String actualInvalidEmailAlertDialogMessage = driver.findElementById("android:id/message").getText();

        assertThat(actualInvalidEmailAlertDialogMessage, equalTo(expectedInvalidEmailAlertDialogMessage));
    }

    @Test
    public void submitComment_withMissingName() {
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/leave_comment").click();

        driver.findElementById("com.coveros.coverosmobileapp:id/enter_email").sendKeys("rtyklheartsdogs@gmail.com");
        driver.findElementById("com.coveros.coverosmobileapp:id/enter_message").sendKeys("Wonderful post!");

        driver.hideKeyboard();
        driver.findElementById("com.coveros.coverosmobileapp:id/send_button").click();

        String expectedAlertDialogMessage = "All fields are required. Please provide your name.";
        String actualAlertDialogMessage = driver.findElementById("android:id/message").getText();

        assertThat(actualAlertDialogMessage, equalTo(expectedAlertDialogMessage));
    }

    @Test
    public void submitComment_withMissingNameAndEmail() {
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/leave_comment").click();

        driver.findElementById("com.coveros.coverosmobileapp:id/enter_message").sendKeys("Wonderful post!");

        driver.hideKeyboard();
        driver.findElementById("com.coveros.coverosmobileapp:id/send_button").click();

        String expectedAlertDialogMessage = "All fields are required. Please provide your name and email.";
        String actualAlertDialogMessage = driver.findElementById("android:id/message").getText();

        assertThat(actualAlertDialogMessage, equalTo(expectedAlertDialogMessage));
    }

    @Test
    public void submitComment_withMissingAllFields() {
        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.RelativeLayout[1]").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/view_comments").click();
        driver.findElementById("com.coveros.coverosmobileapp:id/leave_comment").click();

        driver.findElementById("com.coveros.coverosmobileapp:id/send_button").click();

        String expectedAlertDialogMessage = "All fields are required. Please provide your name, email, and message.";
        String actualAlertDialogMessage = driver.findElementById("android:id/message").getText();

        assertThat(actualAlertDialogMessage, equalTo(expectedAlertDialogMessage));
    }

    @After
    public void tearDown() throws Exception{
        if (driver != null) {
            driver.quit();
        }
    }


}

