package com.coveros.coverosmobileapp.website;

import org.junit.Test;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CustomWebClientTest {

    /*
     * Check on Getter/Setter for getConnection()
     */
    @Test
    public void checkConnection() throws Exception{
        CustomWebViewClient cwvc = new CustomWebViewClient();
        boolean connected = cwvc.getConnection();
        assertTrue(connected);
        cwvc.setConnection(false);
        assertFalse(cwvc.getConnection());
    }

    @Test
    public void checkMainActivity() throws Exception {
        MainActivity ma = new MainActivity();

    }
}
