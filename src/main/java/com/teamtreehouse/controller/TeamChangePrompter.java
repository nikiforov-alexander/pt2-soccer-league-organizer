package com.teamtreehouse.controller;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TeamChangePrompter extends Prompter{
    // This team is passed in constructor, we'll add and remove players to it
    private Team mTeam;

    // our database - is passed in constructor, so that we keep track of
    // players leaving and coming. Upon addition to team, player is removed
    // from here, upon removal from team, player is added her
    private Set<Player> mPlayersSet;
    // protected, used in testing
    protected Set<Player> getPlayersSet() {
        return mPlayersSet;
    }

    // Simply fills mPlayersSet - our dynamic database with Players.load()
    // used for testing, so that we can independently from MainMenuPrompter
    // test this class
    private void fillPlayersSetWithPlayersDatabase() {
        for (Player player : Players.load()) {
            mPlayersSet.add(player);
        }
    }
    // used in testing, with mocked BufferedReader to control user input
    // and mocked Logger to control output. Database mPlayerSet is created
    // from Players.load()
    protected TeamChangePrompter(Team team, BufferedReader bufferedReader,
                              Logger logger) {
        mBufferedReader = bufferedReader;
        mLogger = logger;
        fillMenuWithOptions();
        mPlayersSet = new TreeSet<>();
        fillPlayersSetWithPlayersDatabase();
        mTeam = team;
    }
    // constructor used in actual implementation in
    // MainMenuPrompter.switchToTeamMenu() method
    // I have to pass Logger and Buffered Reader for testing this class called
    // through MainMenuPrompter. PlayersSet is passed from MainMenuPrompter
    protected TeamChangePrompter(Team team, BufferedReader bufferedReader,
                                 Logger logger,
                                 Set<Player> playersSet) {
        mBufferedReader = bufferedReader;
        mLogger = logger;
        fillMenuWithOptions();
        mPlayersSet = playersSet;
        mTeam = team;
    }

    // can be used in testing TeamChangePrompter without
    // MainMenuPrompter. Not used anywhere right now...
    public TeamChangePrompter(Team team,
                              Set<Player> databasePlayersMap) {
        this(team,
                new BufferedReader(new InputStreamReader(System.in)),
                new Logger(),
                databasePlayersMap);
    }

    // helpful function used in addPlayersToTeam and removePlayersFromTeam,
    // returns Player at specific index, if you convert given Set to ArrayList
    private Player getPlayerAtIndexOfSetConvertedToList(
            int index, Set<Player> set) {
        List<Player> playerList = new ArrayList<>(set);
        return playerList.get(index);
    }

    // is executed upon "remove" choice
    private void removePlayersFromTeam() throws IOException {
        showAvailableObjectsWithIdsInSet(
                mTeam.getPlayersSet(),"Team players", "Player");
        // get number from user
        int parsedPlayerId = promptUserForId();
        // if number is in range
        if (parsedIntIsInRangeOfGivenSet(
                parsedPlayerId, mTeam.getPlayersSet())) {
            // I make sure that no IndexOutOfBoundsIsThrown by if above
            // get player at index from Team. id given to user starts from one,
            // that's why we pass parsedPlayerId - 1 here
            Player playerToBeRemoved =
                    getPlayerAtIndexOfSetConvertedToList(
                            parsedPlayerId - 1, mTeam.getPlayersSet());
            // finally remove. No ifs here - left for testing
            mTeam.remove(playerToBeRemoved);
            // put player back to database - again no ifs
            mPlayersSet.add(playerToBeRemoved);
            mLogger.setSuccessMessage("Player "
                    + playerToBeRemoved +
                    "is back in database");
        } // error message is thrown in parsedIntIsInRangeOfGivenSet() method
    }

    // is executed upon "add" choice
    private void addPlayersToTeam() throws IOException {
        // show player with ids from database
        showAvailableObjectsWithIdsInSet(
                mPlayersSet, "Players database", "Player");
        // prompt user for a number
        int parsedPlayerId = promptUserForId();
        // if number is in the right range
        if (parsedIntIsInRangeOfGivenSet(parsedPlayerId, mPlayersSet)) {
            // player is taken at the number, asked by player
            Player playerToAdd =
                    getPlayerAtIndexOfSetConvertedToList(
                            parsedPlayerId - 1, mPlayersSet);
            // no ifs - left for testing
            mTeam.addPlayer(playerToAdd);
            // remove player from database
            mPlayersSet.remove(playerToAdd);
            mLogger.setSuccessMessage("Player " + playerToAdd + " is on team");
        } // error is coded in parsedIntIsRangeOfGivenSet
    }

    // is executed upon "height" in main menu, shows height distribution as
    // map of height to number of players this high
    // untested, because it is a view function
    private void showHeightDistributionOfTeam() {
        printMapOfObjectsWithMessage(
                mTeam.getMapOfNumberOfPlayersWithSpecificHeight(),
                "Height distribution: 'height' : '# of players this high'");
    }
    // is executed upon "experience" in main menu, shows experience as
    // map of experience level by # of players with this level
    // untested, because it is a view function
    private void showExperienceDistribution() {
        printMapOfObjectsWithMessage(
                mTeam.getMapOfPlayersWithDifferentExperience(),
                "Experience distribution: 'experience level' : " +
                        "'# of players with this experience level'");
    }


    // used in constructor to fill menu map with possible options
    private void fillMenuWithOptions() {
        mMenu.put("experience", "Show experience distribution");
        mMenu.put("height", "Show height distribution");
        mMenu.put("show all", "List available players");
        mMenu.put("show team", "Show players in team");
        mMenu.put("add", "Add new players");
        mMenu.put("remove", "Remove players");
        mMenu.put("quit", "Exit menu");
    }
    // main menu, where teams can be created, picked and shown
    private void processUserChoice(String userChoice) throws IOException {
        // all options available when team size > 0
        if (mTeam.size() > 0) {
            switch (userChoice) {
                case "experience":
                    showExperienceDistribution();
                    break;
                case "height":
                    showHeightDistributionOfTeam();
                    break;
                case "show all":
                    showAvailableObjectsWithIdsInSet(
                            mPlayersSet, "Players database", "Player");
                    break;
                case "show team":
                    showAvailableObjectsWithIdsInSet(
                            mTeam.getPlayersSet(), "Team players", "Player");
                    break;
                case "add":
                    addPlayersToTeam();
                    break;
                case "remove":
                    removePlayersFromTeam();
                    break;
                case "quit":
                    mLogger.setSuccessMessage("Exiting...");
                    break;
                default:
                    mLogger.setErrorMessage("Unknown choice: '" +
                            userChoice + "'. Try again.");
                    break;
            }
        } else {
            // when no players are in team, one is limited to "add", "show all"
            // and "quit"
            switch (userChoice) {
                case "show all":
                    showAvailableObjectsWithIdsInSet(
                            mPlayersSet, "Players database", "Player");
                    break;
                case "add":
                    addPlayersToTeam();
                    break;
                case "quit":
                    mLogger.setSuccessMessage("Exiting...");
                    break;
                default:
                    mLogger.setErrorMessage("Unknown choice or empty Team: '" +
                            userChoice + "'. Try again.");
                    break;
            }

        }

    }

    // main team menu method that redirects to different options and
    // methods behind, prints menu options
    public Team presentMenuWithPossibleOptions() throws IOException {
        String userChoice = "";
        do {
            printMenuItems("Team Menu");
            // user choice is trimmed and lower cased for convenience
            userChoice = promptForStringAndReturnInput().trim().toLowerCase();
            // main function with switch
            processUserChoice(userChoice);
            // if something is wrong, while will not be infinite
            throwNewRunTimeExceptionIfNumberOfWhileCallsIsBig();
        } while (!userChoice.equals("quit"));
        return mTeam;
    }
}
