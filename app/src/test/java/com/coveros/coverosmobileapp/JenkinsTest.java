package com.coveros.coverosmobileapp;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by SRynestad on 6/12/2017.
 */

public class JenkinsTest {
    @Test
    public void aIsLessThanB(){
        assertEquals(false, Jenkins.num(6,7));
    }
    @Test
    public void aIsGreaterThanB(){
        assertEquals(true, Jenkins.num(7,6));
    }
    @Test
    public void aIsEqualtoB(){
        assertEquals(true, Jenkins.num(6,6));
    }

}
