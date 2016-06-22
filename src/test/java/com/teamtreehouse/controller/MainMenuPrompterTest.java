package com.teamtreehouse.controller;

import com.teamtreehouse.model.Team;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MainMenuPrompterTest {
    private BufferedReader mMockedBufferedReader;
    private Logger mMockedLogger;
    private MainMenuPrompter mMainMenuPrompter;

    // adding some decoration before each test
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.printf("%n -------- Starting test: %s %n",description.getMethodName());
        }
    };

    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mMockedBufferedReader = mock(BufferedReader.class);
        mMockedLogger = mock(Logger.class);
        mMainMenuPrompter = new MainMenuPrompter(mMockedBufferedReader, mMockedLogger);
    }

    @Test(expected = IOException.class)
    public void exceptionIsCaughtWithExceptionMessageWhenBufferedReaderReadlineIsCalled()
            throws IOException {
        // When BufferedReader.readLine() throws IOException
        doThrow(new IOException()).when(mMockedBufferedReader).readLine();
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then exception should be caught, and message is set to exception type
    }

    @Test
    public void quitIsAcceptedAndMenuPrompterIsOver() throws Exception{
        // When user types "quit"
        when(mMockedBufferedReader.readLine()).thenReturn("quit");
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then success message should be set to "Exiting"
        verify(mMockedLogger).setSuccessMessage("Exiting...");
    }

    @Test
    public void someChoiceNotInMenuMapInvokesUnknownChoiceMessage()
            throws Exception {
        // When user types some choice
        when(mMockedBufferedReader.readLine())
                .thenReturn("someChoice")
                .thenReturn("quit");
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then error message should contain "Unknown choice"
        verify(mMockedLogger).setErrorMessage(contains("Unknown choice"));
    }

    @Test
    public void creatingNewTeamWithRightTeamNameAndCoachPutTeamInTeamsSet()
            throws Exception {
        // When user inputs "create", right team Name "team" - one string,
        // no spaces, right coach name
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("Coach Name")
                .thenReturn("quit");
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        Team team = new Team("team","Coach Name");
        // Then right team should be found in teams set
        assertTrue(mMainMenuPrompter.getTeamsSet().contains(team));
    }

    @Test
    public void threeErrorsAreGivenWhenTeamNameIsSpaceNumberOrTwoWords()
        throws Exception {
        // When user inputs "create" in main menu,
        // for team name: space, two words and number
        // then right team name "team", and right coach name
        // then "quit" in main menu
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn(" ")
                .thenReturn(" Trojans USC ")
                .thenReturn(" 123 ")
                .thenReturn("team")
                .thenReturn("Coach Name")
                .thenReturn("quit");
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then error message should be passed to Logger each time wrong input
        // is given
        verify(mMockedLogger,times(3)).setErrorMessage(
                contains("Invalid team name"));
    }
    @Test
    public void threeErrorsAreGivenWhenCoachNameIsSpaceNumberOrOneWord()
            throws Exception {
        // When user inputs "create" in main menu,
        // then right team name "team",
        // for coach name: space, one word and number
        // then "quit" in main menu
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn(" ")
                .thenReturn(" Trojans ")
                .thenReturn(" 123 ")
                .thenReturn("Coach Name")
                .thenReturn("quit");
        // Given prompter with menu
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then error message should be passed to Logger each time wrong input
        // is given
        verify(mMockedLogger,times(3))
                .setErrorMessage(contains("Invalid coach name"));
    }

    @Test
    public void addingSameNameTeamToTeamSetSetsErrorMessage() throws Exception {
        // When user creates team with the same name
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("Coach Name")
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("Other Name")
                .thenReturn("quit");
        // Given prompter with created team
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then Error message should be passed to Logger
        verify(mMockedLogger).setErrorMessage(contains("already exists"));
    }

    @Test
    public void pickingUpTeamByIdPicksRightTeamFromTeamsSet()
            throws Exception {
        // When user picks an existing team
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("coach name")
                .thenReturn("choose")
                .thenReturn("1")
                .thenReturn("quit")
                .thenReturn("quit");
        // Given prompter with one team
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        // Then picked Team should be equal to existing team
        Team team = new Team("team","coach name");
        assertEquals(team,mMainMenuPrompter.getPickedTeam());
    }

    @Test
    public void removingExistingTeamFromTeamsSetActuallyRemovesTeamFromSet()
            throws Exception {
        // When user tries to remove existing team
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("Coach Name")
                .thenReturn("remove")
                .thenReturn("1")
                .thenReturn("team")
                .thenReturn("quit");
        // Given prompter with one team
        mMainMenuPrompter.presentMenuWithPossibleOptions();
        Team team = new Team("team","Coach Name");
        // Then team set doesn't contain this team
        assertFalse(mMainMenuPrompter.getTeamsSet().contains(team));
    }

    @Test
    public void creatingNewTeamFromEmptyDatabaseGivesNotEnoughPlayersError()
            throws Exception {
        // Given prompter with empty PlayersMap
        MainMenuPrompter prompterWithEmptyDatabase =
                new MainMenuPrompter(mMockedBufferedReader,mMockedLogger,
                        new TreeSet<>());
        // When create new team is asked
        when(mMockedBufferedReader.readLine())
                .thenReturn("create")
                .thenReturn("team")
                .thenReturn("John Coach")
                .thenReturn("quit");
        prompterWithEmptyDatabase.presentMenuWithPossibleOptions();
        // Then error message will be returned, that there are not enough
        // players
        verify(mMockedLogger).setErrorMessage(contains("There are not enough"));
    }
}