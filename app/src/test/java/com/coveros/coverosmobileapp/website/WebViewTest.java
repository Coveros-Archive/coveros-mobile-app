package com.coveros.coverosmobileapp.website;

import com.coveros.coverosmobileapp.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ethan Painter
 */
public class WebViewTest {

    /**
     * Check on Getter for WebName()
     */
    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl;
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com", savedUrl);
    }

    /**
     * Check on Setter for WebName()
     */
    @Test
    public void check_SetWebsiteAddressName() throws Exception {
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

}