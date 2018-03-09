package com.byrneliam2.dvrcalc.impl;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

/**
 * Represents a routing table with a 2D array. Rows are added
 * and returned through operations.
 */
class RoutingTable {

    /*
     * Table is represented with neighbours across the top
     * and destinations down the side.
     */

    private int[][] table;
    private char[] neighbours;
    private char[] destinations;
    private int count = 0;

    RoutingTable(int numDestinations, int numNeighbours) {
        this.table = new int[numDestinations][numNeighbours];
        this.destinations = new char[numDestinations];
        this.neighbours = new char[numNeighbours];
    }

    /*
     * The following methods are used to translate character and integer
     * positions in the respective arrays.
     */

    int getDestinationIndex(char k) {
        for (int i = 0; i < destinations.length; i++) {
            if (destinations[i] == k)
                return i;
        }
        return -1;
    }

    int getNeighbourIndex(char k) {
        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] == k)
                return i;
        }
        return -1;
    }

    char getDestinationAt(int i) {
        return destinations[i];
    }

    char getNeighbourAt(int i) {
        return neighbours[i];
    }

    /* ========================== GETTERS and SETTERS ============================= */

    void setDestinations(char[] ds) {
        destinations = ds;
    }

    void setNeighbours(char[] neighbours) {
        this.neighbours = neighbours;
    }

    void addRow(int[] row) {
        table[count++] = row;
    }

    int[] getRow(int number) {
        return table[number];
    }

    int getValue(int row, int col) {
        return table[row][col];
    }

    /*
     * The following getters and setters are specialised in the way that they accept character
     * indexes. These are translated using the appropriate methods from before. The methods
     * hold implicit and explicit safeguards against index-out-of-bounds exceptions; getValueAt
     * returns a negative value if an error is found and setValueAt checks the incoming value (which
     * is most likely to be the direct result of getValueAt) for negatives. setValueAt simply exits if
     * there is an error, meaning the cell represented by dest and via is not overwritten.
     */

    int getValueAt(char dest, char via) {
        int d = getDestinationIndex(dest);
        int n = getNeighbourIndex(via);
        if (d == -1) return d;
        if (n == -1) return n;
        return table[d][n];
    }

    void setValueAt(char dest, char via, int num) {
        // safeguard
        if (num == -1) return;
        // carry on like in getValueAt
        int d = getDestinationIndex(dest);
        int n = getNeighbourIndex(via);
        if (d == -1) return;
        if (n == -1) return;
        table[d][n] = num;
    }

    /* ========================== ATTRIBUTE GETTERS ============================== */

    int destinationSize() {
        return table.length;
    }

    int neighbourSize() {
        return table[0].length;
    }
}
