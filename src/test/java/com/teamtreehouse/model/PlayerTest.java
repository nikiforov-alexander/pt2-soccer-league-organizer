package com.teamtreehouse.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    @Before
    public void setUp() throws Exception {
    }

    // other tests I trust because we used implemented String compare
    // method
    @Test
    public void playerWithLastNameAbrahamIsMinusTwoFromCraig() throws Exception {
        Player alexAbraham = new Player("Alex","Abraham",1,true);
        Player alexCraig = new Player("Alex","Craig",1,true);
        assertEquals(-2,alexAbraham.compareTo(alexCraig));
    }

    @Test
    public void equalPlayersReturnZeroUponComparison() throws Exception {
        Player johnDoe = new Player("John","Doe",1,true);
        Player johnDoeClone = new Player("John","Doe",1,true);
        assertEquals(0,johnDoe.compareTo(johnDoeClone));
    }

    @Test
    public void playersWithSameLastNamesAreCorrectlyComparedByFirstNames() throws Exception {
        Player abrahamDoe = new Player("Abraham","Doe",1,true);
        Player CraigDoe = new Player("Craig","Doe",1,true);
        assertEquals(-2, abrahamDoe.compareTo(CraigDoe));
    }

}