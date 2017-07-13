package com.coveros.coverosmobileapp.website;

import org.junit.Test;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CustomWebClientTest {

    /*
     * Check on Getter/Setter for Connection
     */
    @Test
    public void checkConnection() throws Exception{
        CustomWebViewClient cwvc = new CustomWebViewClient();
        boolean connected = cwvc.getConnection();
        assertTrue(connected);
        cwvc.setConnection(false);
        assertFalse(cwvc.getConnection());
    }

    /*
     * Check on Getter/Setter for MainActivity
     */
    @Test
    public void checkMainActivitySet() throws Exception {
        MainActivity first = new MainActivity();
        MainActivity substitute = new MainActivity();
        CustomWebViewClient testClient = first.getCustomClient();
        assertTrue(testClient.getMainActivity() == first);
        substitute.setCustomClient(testClient);
        testClient.setMainActivity(substitute);
        assertTrue(testClient.getMainActivity() == substitute);
    }
}
