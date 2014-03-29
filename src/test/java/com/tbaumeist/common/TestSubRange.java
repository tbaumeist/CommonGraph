package com.tbaumeist.common;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestSubRange {

    @Test
    public void testGetIntersection() {
        Node n = new Node(0.125, "Test");
        SubRange r1 = new SubRange(n, new Edge(0.1), new Edge(0.5));
        SubRange r2 = new SubRange(n, new Edge(0.2), new Edge(0.7));

        SubRange r3 = r1.getIntersection(r2);
        assertTrue(r3.getStartLocation() == 0.2);
        assertTrue(r3.getStopLocation() == 0.5);

        // Reversed
        // ===================================================
        r1 = new SubRange(n, new Edge(0.1), new Edge(0.5));
        r2 = new SubRange(n, new Edge(0.2), new Edge(0.7));

        r3 = r2.getIntersection(r1);
        assertTrue(r3.getStartLocation() == 0.2);
        assertTrue(r3.getStopLocation() == 0.5);

        // one overlap
        // ===================================================
        r1 = new SubRange(n, new Edge(0.4), new Edge(0.1));
        r2 = new SubRange(n, new Edge(0.3), new Edge(0.7));

        r3 = r1.getIntersection(r2);
        assertTrue(r3.getStartLocation() == 0.4);
        assertTrue(r3.getStopLocation() == 0.7);

        // one overlap
        // ===================================================
        r1 = new SubRange(n, new Edge(0.4), new Edge(0.1));
        r2 = new SubRange(n, new Edge(0.3), new Edge(0.7));

        r3 = r2.getIntersection(r1);
        assertTrue(r3.getStartLocation() == 0.4);
        assertTrue(r3.getStopLocation() == 0.7);

        // both overlap
        // ===================================================
        r1 = new SubRange(n, new Edge(0.4), new Edge(0.1));
        r2 = new SubRange(n, new Edge(0.3), new Edge(0.2));

        r3 = r2.getIntersection(r1);
        assertTrue(r3.getStartLocation() == 0.4);
        assertTrue(r3.getStopLocation() == 0.1);
    }

}
