package com.teamtreehouse.controller;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

public class TeamChangePrompterTest {
    private BufferedReader mMockedBufferedReader;
    private Logger mMockedLogger;
    private Team mTeam;
    private TeamChangePrompter mTeamChangePrompter;
    // adding some decoration before each test
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.printf("%n -------- Starting test: %s %n",description.getMethodName());
        }
    };
    private Player mFirstPlayerInPlayersDatabase;

    @Before
    public void setUp() throws Exception {
        mMockedBufferedReader = mock(BufferedReader.class);
        mMockedLogger = mock(Logger.class);
        mTeam = new Team("team","coach name");
        mTeamChangePrompter =
                new TeamChangePrompter(mTeam,
                        mMockedBufferedReader,
                        mMockedLogger);
        List<Player> playerList =
                new ArrayList<>(mTeamChangePrompter.getPlayersSet());
        mFirstPlayerInPlayersDatabase = playerList.get(0);
    }

    @Test
    public void someChoiceNotInMenuMapInvokesUnknownChoiceMessage()
            throws Exception {
        // When user types some choice
        when(mMockedBufferedReader.readLine())
                .thenReturn("someChoice")
                .thenReturn("quit");
        // Given prompter with menu
        mTeamChangePrompter.presentMenuWithPossibleOptions();
        // Then error message should contain "Unknown choice"
        verify(mMockedLogger).setErrorMessage(contains("Unknown choice"));
    }

    @Test
    public void addingRightPlayerUsingIdAddsRightPlayer()
            throws Exception {
        // Given prompter with menu and empty team
        when(mMockedBufferedReader.readLine())
                .thenReturn("add")
                .thenReturn("1")
                .thenReturn("quit");
        mTeamChangePrompter.presentMenuWithPossibleOptions();
        assertTrue(mTeam.contains(mFirstPlayerInPlayersDatabase));
    }

    @Test
    public void typingNumbersNotInARangeResultsInError()
            throws Exception {
        // When user is trying to type numbers 0 and higher than players
        // database size
        when(mMockedBufferedReader.readLine())
                .thenReturn("add")
                .thenReturn(String.valueOf(0))
                .thenReturn("add")
                .thenReturn(String.
                        valueOf(mTeamChangePrompter.getPlayersSet().size() + 10))
                .thenReturn("quit");
        // Given prompter with menu and empty team
        mTeamChangePrompter.presentMenuWithPossibleOptions();
        // Then error message "no such player" should be passed to Logger
        verify(mMockedLogger, times(2))
                .setErrorMessage(contains("Number is not in the right range"));
    }


    @Test
    public void removingPlayerFromTeamActuallyRemovesPlayer() throws Exception {
        // When user is trying to add and remove some player from Team
        when(mMockedBufferedReader.readLine())
                .thenReturn("add")
                .thenReturn(String.valueOf(1))
                .thenReturn("remove")
                .thenReturn(String.valueOf(1))
                .thenReturn("quit");
        // Given prompter with menu and empty team
        mTeamChangePrompter.presentMenuWithPossibleOptions();
        // Then this player will not be found in mTeam
        assertFalse(mTeam.contains(mFirstPlayerInPlayersDatabase));
    }
    @Test
    public void removingPlayerFromTeamPutsHimBackToPlayersSet() throws Exception {
        // When user is trying to add and remove some player from Team
        when(mMockedBufferedReader.readLine())
                .thenReturn("add")
                .thenReturn(String.valueOf(1))
                .thenReturn("remove")
                .thenReturn(String.valueOf(1))
                .thenReturn("quit");
        // Given prompter with menu and empty team
        mTeamChangePrompter.presentMenuWithPossibleOptions();
        // Then player should be found in players set database
        assertTrue(mTeamChangePrompter
                .getPlayersSet()
                .contains(mFirstPlayerInPlayersDatabase));

    }
}