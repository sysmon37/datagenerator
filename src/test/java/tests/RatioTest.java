package tests;

import config.Ratio;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author swilk
 */
public class RatioTest {
    
    public RatioTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testToFractions() {
        System.out.println("toFractions");
        Ratio r1 = new Ratio(Arrays.asList(0.0, 0.0, 0.0));
        Ratio r2 = new Ratio(Arrays.asList(1.0, 2.0, 1.0));
        Ratio r3 = new Ratio(Arrays.asList(1.0, 1.0, 1.0));
        
        assertEquals(r1.toFractions(), new Ratio(Arrays.asList(0.0, 0.0, 0.0)));
        assertEquals(r2.toFractions(), new Ratio(Arrays.asList(0.25, 0.50, 0.25)));
        assertEquals(r3.toFractions(), new Ratio(Arrays.asList(1/3.0, 1/3.0, 1/3.0)));
    }

    @Test
    public void testSubRatio() {
        System.out.println("subRatio");
        Ratio r1 = new Ratio(Arrays.asList(40.0, 30.0, 20.0, 10.0));
        assertEquals(r1.subRatio(0, 2), new Ratio(Arrays.asList(40.0, 30.0)));
        assertEquals(r1.subRatio(2, 2), new Ratio(Arrays.asList(20.0, 10.0)));
    }

    @Test
    public void testSortIndexes_0args() {
        System.out.println("sortIndexes");
        Ratio t1 = new Ratio(Arrays.asList(40.0, 30.0, 20.0, 10.0));
        assertEquals(t1.sortIndexes(), Arrays.asList(3, 2, 1, 0));
    }

    @Test
    public void testSortIndexes_boolean() {
        Ratio t1 = new Ratio(Arrays.asList(40.0, 30.0, 20.0, 10.0));
        assertEquals(t1.sortIndexes(true), Arrays.asList(3, 2, 1, 0));
        assertEquals(t1.sortIndexes(false), Arrays.asList(0, 1, 2, 3));
    }

    @Test
    public void testGetTotal() {
        System.out.println("getTotal");
        Ratio r1 = new Ratio(Arrays.asList(40.0, 30.0, 20.0, 10.0));
        Ratio r2 = new Ratio(Arrays.asList(0.0, 0.0, 0.0, 0.0));

        assertEquals(100.0, r1.getTotal(), 0.0);
        assertEquals(0.0, r2.getTotal(), 0.0);
    }
    
}
