/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2009 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.core.test.math;

import static org.junit.Assert.assertEquals;

import org.eclipse.elk.core.math.ElkMath;
import org.eclipse.elk.core.math.KVector;
import org.eclipse.elk.core.math.KVectorChain;
import org.junit.Test;

/**
 * Junit test-class for de.cau.cs.kieler.core.math: Mathematics utility class for the KIELER
 * projects.
 * 
 * @author wah
 */
public class MathTest {

    /**
     * Tests some valid combinations of Factl from ElkMath class.
     */
    @Test
    public void testFactl() {
        assertEquals(1, ElkMath.factl(0));
        assertEquals(1, ElkMath.factl(1));
        assertEquals(2432902008176640000L, ElkMath.factl(20));

    }

    /**
     * Tests the Little argument exception of Factl from ElkMath class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFactlLittleIllegalArgumentException() {
        ElkMath.factl(-50);
    }

    /**
     * Tests the Big argument exception of Factl from ElkMath class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFactlBigIllegalArgumentException() {
        ElkMath.factl(21);
    }

    /**
     * Tests some valid combinations of Factd from ElkMath class.
     */
    @Test
    public void testFactd() {
        assertEquals(1, ElkMath.factd(0), 0.0);
        assertEquals(1, ElkMath.factd(1), 0.0);
    }

    /**
     * Tests the Little argument exception of Factd from ElkMath class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFacdlLittleIllegalArgumentException() {
        ElkMath.factd(-1);
    }

    /**
     * Tests some valid combinations of binomiall from ElkMath class.
     */
    @Test
    public void testBinomiall() {
        assertEquals(1, ElkMath.binomiall(2, 0));
        assertEquals(1, ElkMath.binomiall(20, 20));
        assertEquals(2, ElkMath.binomiall(2, 1));
    }

    /**
     * Tests the Little argument exception of binomiall from ElkMath class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBinomialllLittleIllegalArgumentException() {
        ElkMath.binomiall(-1, 1);
    }

    /**
     * Tests some valid combinations of binomiald from ElkMath class.
     */
    @Test
    public void testBinomiald() {
        assertEquals(1, ElkMath.binomiald(2, 0), 0);
        assertEquals(1, ElkMath.binomiald(20, 20), 0);
        assertEquals(2, ElkMath.binomiald(2, 1), 0);
    }

    /**
     * Tests the Little argument exception of binomiald from ElkMath class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBinomialdLittleIllegalArgumentException() {
        ElkMath.binomiald(-1, 1);
    }

    /**
     * Tests some valid combinations of pow from ElkMath class.
     */
    @Test
    public void testPow() {
        double ad = 10;
        float af = 10;
        assertEquals(1, ElkMath.powd(ad, 0), 0);
        assertEquals(1, ElkMath.powf(af, 0), 0);
        assertEquals(100, ElkMath.powd(ad, 2), 0);
        assertEquals(100, ElkMath.powf(af, 2), 0);
    }

    /**
     * Tests some valid combinations of CalcBezierPoints from ElkMath class.
     */
    @Test
    public void testCalcBezierPoints() {
        /* some KVectors */
        KVector kvector1 = new KVector(10, 10);
        KVector kvector2 = new KVector(20, 20);
        KVector kvector3 = new KVector(30, 30);
        KVector kvector4 = new KVector(50, 50);
        /* test if the last KVector of the result similar to kvector4 */
        KVector[] result = ElkMath.approximateBezierSegment(20, kvector1, kvector2, kvector3, kvector4);
        assertEquals(kvector4.x, result[result.length - 1].x, 0.000000001);
        assertEquals(kvector4.y, result[result.length - 1].y, 0.000000001);

        /* some KVectors with y=10 */
        kvector1 = new KVector(50, 10);
        kvector2 = new KVector(70, 10);
        kvector3 = new KVector(80, 10);
        kvector4 = new KVector(100, 10);
        /* test if the all result-KVectors with y=10 */
        result = ElkMath.approximateBezierSegment(20, kvector1, kvector2, kvector3, kvector4);
        for (KVector k : result) {
            assertEquals(10, k.y, 0.000000001);
        }

    }

    /**
     * Tests some valid combinations of appoximateSpline from ElkMath class.
     */
    @Test
    public void testAppoximateSpline() {
        /* some KVectors */
        KVector kvector1 = new KVector(10, 10);
        KVector kvector2 = new KVector(20, 20);
        KVector kvector3 = new KVector(30, 30);
        KVector kvector4 = new KVector(50, 50);
        /* test if the last KVector of the result similar to kvector4 */
        KVector[] vectors = ElkMath.approximateBezierSegment(20, kvector1, kvector2, kvector3, kvector4);
        KVectorChain controlPoints = new KVectorChain(vectors);
        KVectorChain result = ElkMath.approximateBezierSpline(controlPoints);
        KVector k = result.get(result.size() - 1);
        assertEquals(kvector4.x, k.x, 0.000000001);
        assertEquals(kvector4.y, k.y, 0.000000001);

        /* some KVectors with y=10 */
        kvector1 = new KVector(50, 10);
        kvector2 = new KVector(70, 10);
        kvector3 = new KVector(80, 10);
        kvector4 = new KVector(100, 10);
        /* test if the all result-KVectors with y=10 */
        vectors = ElkMath.approximateBezierSegment(20, kvector1, kvector2, kvector3, kvector4);
        controlPoints = new KVectorChain(vectors);
        result = ElkMath.approximateBezierSpline(controlPoints);

        for (KVector kv : result) {
            assertEquals(10, kv.y, 0.000000001);
        }

    }

    /**
     * Tests some valid combinations of distanceFromSpline from ElkMath class.
     */
    @Test
    public void testDistanceFromSpline() {
        /* some KVectors */
        KVector kvector1 = new KVector(10, 10);
        KVector kvector2 = new KVector(20, 20);
        KVector kvector3 = new KVector(30, 30);
        KVector kvector4 = new KVector(50, 50);
        /* test if the result is 0 when kvector4 = needle */
        KVector needle = kvector4;
        double result = ElkMath.distanceFromBezierSegment(kvector1, kvector2, kvector3, kvector4,
                needle);
        assertEquals(0, result, 0.01);
        /* test if the result is 0 when kvector3 = needle */
        needle = kvector3;
        result = ElkMath.distanceFromBezierSegment(kvector1, kvector2, kvector3, kvector4, needle);
        assertEquals(0, result, 0.01);
        /* test if the result is 0 when kvector2 = needle */
        needle = kvector2;
        result = ElkMath.distanceFromBezierSegment(kvector1, kvector2, kvector3, kvector4, needle);
        assertEquals(0, result, 0.01);
        /* test if the result is 0 when kvector1 = needle */
        needle = kvector1;
        result = ElkMath.distanceFromBezierSegment(kvector1, kvector2, kvector3, kvector4, needle);
        assertEquals(0, result, 0.01);

    }

    /**
     * Tests some valid combinations of maxi from ElkMath class.
     */
    @Test
    public void testMax() {
        /* test if the max is 7 */
        assertEquals(7, ElkMath.maxi(1, 7, 5, 6));
        assertEquals(7, ElkMath.maxf(1, 7, 5, 6), 0);
        assertEquals(7, ElkMath.maxd(1, 7, 5, 6), 0);

    }

    /**
     * Tests some valid combinations of mini,minf,mind from ElkMath class.
     */
    @Test
    public void testMin() {
        /* test if the mini is 1 */
        assertEquals(1, ElkMath.mini(1, 7, 5, 6));
        /* test if the mini is 0 */
        assertEquals(0, ElkMath.mini(8, 1, 9, 0));
        /* test if the mini is 8 */
        assertEquals(8, ElkMath.mini(8, 8, 8, 8));
        /* test if the minf is 1 */
        assertEquals(1, ElkMath.minf(1, 7, 5, 6), 0);
        /* test if the minf is 0 */
        assertEquals(0, ElkMath.minf(8, 1, 9, 0), 0);
        /* test if the minf is 8 */
        assertEquals(8, ElkMath.minf(8, 8, 8, 8), 0);
        /* test if the mind is 1 */
        assertEquals(1, ElkMath.mind(1, 7, 5, 6), 0);
        /* test if the mind is 0 */
        assertEquals(0, ElkMath.mind(8, 1, 9, 0), 0);
        /* test if the mind is 8 */
        assertEquals(8, ElkMath.mind(8, 8, 8, 8), 0);
    }

    /**
     * Tests some valid combinations of averagei from ElkMath class.
     */
    @Test
    public void testAverage() {
        /* test if the averagei is 4 */
        assertEquals(4, ElkMath.averagel((long) 5, (long) 8, (long) 2, (long) 1));
        /* test if the averagei is 2 */
        assertEquals(2, ElkMath.averagel((long) 5, (long) 0, (long) 2, (long) 1));
        /* test if the averagef is 4 */
        assertEquals(4, ElkMath.averagef(5, 8, 2, 1), 0);
        /* test if the averagef is 2 */
        assertEquals(2, ElkMath.averagef(5, 0, 2, 1), 0);
        /* test if the averaged is 4 */
        assertEquals(4, ElkMath.averaged(5, 8, 2, 1), 0);
        /* test if the averaged is 2 */
        assertEquals(2, ElkMath.averaged(5, 0, 2, 1), 0);
    }

}
