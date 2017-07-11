package com.coveros.coverosmobileapp.website;

import android.webkit.WebView;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Ethan Painter
 */
public class WebViewTest {

    /**
     * Check on Getter/Setter for WebName()
     */
    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl;
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com/", savedUrl);
        ma.setWebName("http://www.html5rocks.com/");
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }
}