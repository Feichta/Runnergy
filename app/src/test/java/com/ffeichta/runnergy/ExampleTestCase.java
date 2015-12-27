package com.ffeichta.runnergy;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Fabian on 27.12.2015.
 */
public class ExampleTestCase extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {

    }

    @Test
    public void testSum() {
        assertEquals(10, 5 + 5);
    }
}