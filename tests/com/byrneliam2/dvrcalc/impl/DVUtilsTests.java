package com.byrneliam2.dvrcalc.impl;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DVUtilsTests {

    @Test
    public void test01_GetBestRouteWithRows() {
        int[] row = {4, 7, 12, 3, 5};
        int[] output = new DistanceVectorRouter.DVUtils(new ArrayList<>()).getNextRoute(row);
        assertEquals(3, output[0]);
        assertEquals(3, output[1]);
    }

    @Test
    public void test02_GetBestRouteWithRows() {
        int[] row = {2, 3, 4};
        int[] output = new DistanceVectorRouter.DVUtils(new ArrayList<>()).getNextRoute(row);
        assertEquals(2, output[0]);
        assertEquals(0, output[1]);
    }

    @Test
    public void test03_GetBestRouteWithRows() {
        int[] row = {14};
        int[] output = new DistanceVectorRouter.DVUtils(new ArrayList<>()).getNextRoute(row);
        assertEquals(14, output[0]);
        assertEquals(0, output[1]);
    }

    @Test
    public void test04_GetBestRouteWithNodes() {
        Node node = new Node('q', 0, 0);
        node.addNeighbour('A', 5);
        node.addNeighbour('B', 8);
        node.addNeighbour('C', 2);
        node.addNeighbour('E', 12);
        assertEquals('C',
                new DistanceVectorRouter.DVUtils(new ArrayList<>()).getShortestRoute(node));
    }

    @Test
    public void test05_GetBestRouteWithNodes() {
        Node node = new Node('q', 0, 0);
        node.addNeighbour('A', 2);
        node.addNeighbour('B', 6);
        node.addNeighbour('C', 14);
        node.addNeighbour('E', 12);
        assertEquals('A',
                new DistanceVectorRouter.DVUtils(new ArrayList<>()).getShortestRoute(node));
    }


    @Test
    public void test06_GetBestRouteWithNodes() {
        Node node = new Node('q', 0, 0);
        node.addNeighbour('G', 5);
        node.addNeighbour('H', 5);
        node.addNeighbour('I', 5);
        assertEquals('G',
                new DistanceVectorRouter.DVUtils(new ArrayList<>()).getShortestRoute(node));
    }
}
