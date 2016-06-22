package com.teamtreehouse.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class TeamTest {
    private Team mTeam;
    private Player mJohnDoePlayer;

    @Before
    public void setUp() throws Exception {
        mTeam = new Team("team","coach name");
        mJohnDoePlayer = new Player("John","Doe",1,true);
    }
    // adding some decoration before each test
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.printf("%n -------- Starting test: %s %n",description.getMethodName());
        }
    };

    @Test
    public void addPlayerToNewTeamCreatesOnePlayer() throws Exception {
        // Given team with name "team"  and mMockedLogger

        // When player is added
        mTeam.addPlayer(mJohnDoePlayer);
        // Then size of team should be one
        assertEquals(1,mTeam.getTeamSize());
    }

    @Test
    public void addingMoreThanElevenIsImpossible() throws Exception {
        // Given team with name "team"  and mMockedLogger
        // and 11 players added
        Player[] players = Players.load();
        for (int i = 1; i <= 11; i++) {
           mTeam.addPlayer(players[i]);
        }
        // When 12-th player not the same player is added
        assertFalse(mTeam.addPlayer(mJohnDoePlayer));
        // Then addPlayer method should return false
    }

    @Test
    public void addingThePlayerWithTheSameLastNameInvokesIsAlreadyInTeamMessage()
            throws Exception {
        // Given team with name "team", and one player
        mTeam.addPlayer(mJohnDoePlayer);
        // When same player is added twice
        // Then addPlayer method should return false
        assertFalse(mTeam.addPlayer(mJohnDoePlayer));
    }

    @Test
    public void afterRemovingExistingPlayerHeCanNotBeFoundInPlayersMap()
            throws Exception {
        // Given team with name "team" and one player
        mTeam.addPlayer(mJohnDoePlayer);
        // When we remove same player
        mTeam.remove(mJohnDoePlayer);
        // Then value set of Team's PlayerMap should not contain that player
        assertFalse(mTeam.contains(mJohnDoePlayer));
    }

    @Test
    public void removingNonExistingPlayerInvokesErrorMessageThereIsNoPlayer() throws Exception {
        // Given empty team
        // with no players in it
        // When player is removed
        // Then remove should return false
        assertFalse(mTeam.remove(mJohnDoePlayer));
    }

    @Test
    public void teamsWithSameNameAreSame() throws Exception {
        // Given two team objects with name "team",
        // and mMockedLogger
        Team sameTeamAsmTeam = new Team("team", "coach");
        // When teams are compared
        assertEquals(0,sameTeamAsmTeam.compareTo(mTeam));
        // Then Zero should be returned
    }

    @Test
    public void teamsWithTheSameNameButDifferentCoachesAreSameByComparison()
            throws Exception {
        // Given two teams with coach names starting with
        // 'a' and 'b'
        Team aCoachTeam = new Team("team","aCoach");
        Team bCoachTeam = new Team("team","bCoach");
        // When they are compared by equals()
        assertEquals(0,aCoachTeam.compareTo(bCoachTeam));
        // Then true is returned
    }

    @Test
    public void aTeamAndBTeamUponComparisonReturnOne() throws Exception {
        // Given two teams with names starting with
        // 'a' and 'b'
        Team aTeam = new Team("aTeam","coach");
        Team bTeam = new Team("bTeam","coach");
        // When 'aTeam' is compared to 'bTeam'
        assertEquals(-1,aTeam.compareTo(bTeam));
        // Then -1 should be returned
    }

    @Test
    public void teamWithMaxNumberOfPlayersIsFull() throws Exception {
        // Given team with name "team", and mMockedLogger
        // and 11 players added
        Player[] players = Players.load();
        for (int i = 1; i <= 11; i++) {
            mTeam.addPlayer(players[i]);
        }
        // When is full method is executed
        // Then true should be returned after isFull() method
        assertTrue(mTeam.isFull());
    }

    @Test
    public void newlyCreatedTeamIsEmpty() throws Exception {
        // Given newly created team
        // When isFull() method is called
        // Then false should be returned
        assertFalse(mTeam.isFull());
    }

    @Test
    public void addingPlayerCreatesOneMapWithPlayerHeights() throws Exception {
        // Given team with two players with height 1, and one player with
        // height 2
        Player firstPlayerWithHeight1 = new Player("John","Doe",1,true);
        Player secondPlayerWithHeight1 = new Player("Alex","Doe",1,true);
        Player playerWithHeight2 = new Player("Craig","Doe",2,true);
        mTeam.addPlayer(firstPlayerWithHeight1);
        mTeam.addPlayer(secondPlayerWithHeight1);
        mTeam.addPlayer(playerWithHeight2);
        // When getMapOfNumberWithSpecificHeight() is executed
        Map<Integer,Integer> generatedHeightMap =
                mTeam.getMapOfNumberOfPlayersWithSpecificHeight();
        // Then right map with (2,1),(1,2) entries should be made
        Map<Integer,Integer> rightMapWithTwoEntries = new TreeMap<>();
        rightMapWithTwoEntries.put(1, 2);
        rightMapWithTwoEntries.put(2, 1);
        assertEquals(rightMapWithTwoEntries,generatedHeightMap);
    }

    @Test
    public void addingPlayerCreatesRightMapWithPlayerExperience() throws Exception {
        // Given team with two beginners , and one experienced player
        Player firstBeginnerPlayer = new Player("John","Doe",1,false);
        Player secondBeginnerPlayer = new Player("Alex","Doe",1,false);
        Player experiencedPlayer = new Player("Craig","Doe",2,true);
        mTeam.addPlayer(firstBeginnerPlayer);
        mTeam.addPlayer(secondBeginnerPlayer);
        mTeam.addPlayer(experiencedPlayer);
        // When getMapOfPlayersWithDifferentExperience() is executed
        Map<String,Integer> generatedExperienceMap =
                mTeam.getMapOfPlayersWithDifferentExperience();
        // Then right map with ("Beginner",1),("Experienced",2) entries should be made
        Map<String,Integer> rightMapWithTwoEntries = new TreeMap<>();
        rightMapWithTwoEntries.put("Beginner", 2);
        rightMapWithTwoEntries.put("Experienced", 1);
        assertEquals(rightMapWithTwoEntries,generatedExperienceMap);
    }
}