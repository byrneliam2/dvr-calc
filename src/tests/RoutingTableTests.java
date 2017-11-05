package tests;

/*
 * NWEN 243 Lab 4
 * Liam Byrne (byrneliam2)
 * 300338518
 */

import impl.RoutingTable;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoutingTableTests {
    
    private static final char[] DESTS = {'A', 'B', 'C', 'D', 'E'};
    private static final char[] NEIGHS = {'B', 'D', 'E'};

    @Test
    public void test01_BuildTable() {
        RoutingTable table = buildTable();
        assertTrue(table.destinationSize() == DESTS.length);
        assertTrue(table.neighbourSize() == NEIGHS.length);
        int test = 1;
        for (int i = 0; i < DESTS.length; i++)
            for (int j = 0; j < NEIGHS.length; j++)
                assertTrue(table.getValue(i, j) == test++);
    }
    
    @Test
    public void test02_Accessing() {
        RoutingTable table = buildTable();
        int count = 1;
        for (int i = 0; i < DESTS.length; i++) {
            int[] row = new int[NEIGHS.length];
            for (int j = 0; j < NEIGHS.length; j++) row[j] = count++;
            int[] actual = table.getRow(i);
            for (int j = 0; j < NEIGHS.length; j++)
                assertTrue(actual[j] == row[j]);
        }
    }

    @Test
    public void test03_DestinationIndexing() {
        RoutingTable table = buildTable();
        for (int i = 0; i < DESTS.length; i++)
            assertTrue(table.getDestinationAt(i) == DESTS[i]);
    }
    
    @Test
    public void test04_DestinationIndexing2() {
        RoutingTable table = buildTable();
        for (int i = 0; i < DESTS.length; i++)
            assertTrue(table.getDestinationIndex(DESTS[i]) == i);
        assertTrue(table.getDestinationIndex('X') == -1);
        
    }

    @Test
    public void test05_NeighbourIndexing() {
        RoutingTable table = buildTable();
        for (int i = 0; i < NEIGHS.length; i++)
            assertTrue(table.getNeighbourAt(i) == NEIGHS[i]);
    }

    @Test
    public void test06_NeighbourIndexing2() {
        RoutingTable table = buildTable();
        for (int i = 0; i < NEIGHS.length; i++)
            assertTrue(table.getNeighbourIndex(NEIGHS[i]) == i);
        assertTrue(table.getNeighbourIndex('X') == -1);
    }

    @Test
    public void test07_GettingValues() {
        RoutingTable table = buildTable();
        int count = 1;
        for (int i = 0; i < DESTS.length; i++)
            for (int j = 0; j < NEIGHS.length; j++)
                assertEquals(count++, table.getValueAt(DESTS[i], NEIGHS[j]));
    }

    @Test
    public void test08_SettingValues() {
        RoutingTable table = buildTable();
        for (int i = 0; i < DESTS.length; i++)
            for (int j = 0; j < NEIGHS.length; j++)
                table.setValueAt(DESTS[i], NEIGHS[j], table.getValue(i, j) + 10);
        int count = 11;
        for (int i = 0; i < DESTS.length; i++)
            for (int j = 0; j < NEIGHS.length; j++)
                assertTrue(table.getValue(i, j) == count++);
    }

    /**
     * Build a table filled with SIZE*SIZE integers from 1 to SIZE*SIZE.
     */
    private RoutingTable buildTable() {
        RoutingTable table = new RoutingTable(DESTS.length, NEIGHS.length);
        int count = 1;
        table.setDestinations(DESTS);
        table.setNeighbours(NEIGHS);
        for (int i = 0; i < DESTS.length; i++) {
            int[] row = new int[NEIGHS.length];
            for (int j = 0; j < NEIGHS.length; j++) row[j] = count++;
            table.addRow(row);
        }
        return table;
    }
}
