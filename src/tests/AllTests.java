package tests;

/*
 * NWEN 243 Lab 4
 * Liam Byrne (byrneliam2)
 * 300338518
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({RoutingTableTests.class, DVUtilsTests.class})
public class AllTests {}
