package com.coveros.coverosmobileapp.website;

import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * @author Ethan Painter
 */
public class WebViewTest {

    /**
     * Check on Getter/Setter for WebName() for default constructor
     */
    @Test
    public void getWebsiteAddressName() {
        String savedUrl = "https://www3.dev.secureci.com";
        String expected = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        assertEquals(savedUrl, ma.getWebName());
        ma.setWebName("http://www.html5rocks.com/");
        assertEquals(expected, ma.getWebName());
    }

    /*
     * Check on MainActivity Constructors
     */
    @Test
    public void getWebAddressesForConstructors() {
        String savedUrl = "https://www3.dev.secureci.com";
        String newUrl = "https://github.com/";
        MainActivity mainActivity = new MainActivity();
        MainActivity mainActivity1 = new MainActivity(newUrl);
        assertEquals(savedUrl, mainActivity.getWebName());
        assertEquals(newUrl, mainActivity1.getWebName());
    }

    @Test
    public void getClientFromMainActivity() {
        MainActivity mainActivity = new MainActivity();
        CustomWebViewClient custom = new CustomWebViewClient();
        mainActivity.setCustomClient(custom);
        assertEquals(custom, mainActivity.getCustomClient());
    }

}