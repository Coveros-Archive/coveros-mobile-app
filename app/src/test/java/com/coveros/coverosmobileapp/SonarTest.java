package com.coveros.coverosmobileapp;

        import org.junit.Test;

        import static org.junit.Assert.assertFalse;
        import static org.junit.Assert.assertTrue;

        /**
  * Created by SRynestad on 6/12/2017.
  */

        public class SonarTest {
     @Test
     public void aIsLessThanB(){
                assertFalse(Sonar.num(6,7));
            }
     @Test
    public void aIsGreaterThanB(){
                assertTrue(Sonar.num(7,6));
            }
     @Test
     public void aIsEqualtoB(){
                assertTrue(Sonar.num(6,6));
            }
 }