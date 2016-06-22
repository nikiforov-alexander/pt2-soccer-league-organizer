package com.teamtreehouse.controller;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class MainMenuPrompter extends Prompter {

    // This players Set is the unique collection of players that is generated
    // from database: Players.load(). It is changing upon removal and addition
    // of players to team
    private Set<Player> mPlayersSet;
    // This team will point to team in mTeamsSet, will be sent to
    // TeamChangePrompter to add players and returned back
    private Team mPickedTeam;
    // protected because used in testing, not in the actual implementation
    protected Team getPickedTeam() {
        return mPickedTeam;
    }
    // why this number is separate: because it is defined at the very beginning
    // when database with players is loaded into mPlayersSet. It is used to
    // checkIfThereIsEnoughPlayersToCreateNewTeam()
    private int mNumberOfPlayersInDatabase;


    // Set of teams, will be implemented as TreeSet sorted by name, see
    // Team.compareTo(Team otherTeam)
    private Set<Team> mTeamsSet;
    // protected, because used in testing, not in the actual implementation
    protected Set<Team> getTeamsSet() {
        return mTeamsSet;
    }

    // fills players from database: Players.load() to mPlayersSet: our dynamic
    // database. Used in default constructor: MainMenuPrompter()
    private void fillPlayersSetWithPlayersDatabase() {
        for (Player player : Players.load()) {
            mPlayersSet.add(player);
        }
    }
    // constructor used in Testing with additional arguments, that let us
    // track proper messages and control user input:
    // BufferedReader and Logger will be mocked
    // BufferedReader is mocked to set user input
    // Logger - to mock output
    // for the description of types see parent class Prompter
    // mBufferedReader and mLogger are inherited from Prompter class
    protected MainMenuPrompter(BufferedReader bufferedReader, Logger logger) {
        mBufferedReader = bufferedReader;
        mLogger = logger;
        mMenu = new HashMap<>();
        fillMenuMapWithOptions();
        mTeamsSet = new TreeSet<>();
        mPlayersSet = new TreeSet<>();
        fillPlayersSetWithPlayersDatabase();
        mNumberOfPlayersInDatabase = mPlayersSet.size();
    }
    // constructor used in testing case when there are not enough players
    // in database to create a team, we can pass here our own database
    protected MainMenuPrompter(BufferedReader bufferedReader, Logger logger,
        Set<Player> playersSet) {
        mBufferedReader = bufferedReader;
        mLogger = logger;
        mMenu = new HashMap<>();
        fillMenuMapWithOptions();
        mTeamsSet = new TreeSet<>();
        mPlayersSet = playersSet;
        mNumberOfPlayersInDatabase = mPlayersSet.size();
    }
    // default constructor, used in Main LeagueManager Method
    public MainMenuPrompter() {
        this(new BufferedReader(new InputStreamReader(System.in)),
                new Logger());
    }

    // is executed upon  "show" choice in main menu, shows available teams.
    // toString() method is used to print Teams. If no mTeamsSet is empty,
    // simple message is returned
    private void showAvailableTeams() {
        if (mTeamsSet.size() > 0) {
            System.out.println("---- List of available teams:");
            for (Team team : mTeamsSet) {
                System.out.println(team);
                team.getPlayersSet().stream().forEach(System.out::println);
                System.out.println("--");
            }
            System.out.println("---- ");
        } else {
            mLogger.setSimpleMessage("No teams available");
        }
    }

    // this method checks inside createNewTeam() that possible number
    // of players is more than
    // max number of players in team, by removing
    // number of teams * max number of player from number of players
    // in database (the number sets in the beginning)
    // @return true - if it is ok, for new team to be created
    //         false - if it not. createNewTeam() doesn't go further
    private boolean checkIfThereIsEnoughPlayersToCreateNewTeam() {
        int numberOfPlayersInDatabaseMinusThreeFullTeams =
                mNumberOfPlayersInDatabase -
                        mTeamsSet.size() * Team.MAX_NUMBER_OF_PLAYERS;
        if (numberOfPlayersInDatabaseMinusThreeFullTeams >=
                Team.MAX_NUMBER_OF_PLAYERS) {
            return true;
        } else {
            mLogger.setErrorMessage("There are not enough players in" +
                    "database. There are "
                    + mTeamsSet.size() +
                    " teams now with possible "
                    + Team.MAX_NUMBER_OF_PLAYERS +
                    " players which leaves "
                    + numberOfPlayersInDatabaseMinusThreeFullTeams +
                    " players - not enough for a new team");
            return false;
        }
    }
    // is executed upon "create" choice in main menu,
    // shows available teams first,
    private void createNewTeam() throws IOException {
        showAvailableTeams();
        // prompt method is inherited from Parent Prompter class
        String teamName =
                promptForStringWithPatternUntilUserInputMatchingOne(
                        "^[a-zA-Z]+$",
                        "Please enter team name " +
                                "(One word, no digits, like 'Sharks')",
                        "Invalid team name");
        // here we create set of team names because we consider teams
        // to be compared by names, see compareTo(Team otherTeam) in Team class
        Set<String> teamNamesSet =
                new HashSet<>(mTeamsSet
                        .stream()
                        .map(Team::getName)
                        .collect(Collectors.toCollection(HashSet::new)));
        // and we check whether team is already in set, to avoid duplicates
        if (teamNamesSet.contains(teamName)) {
            mLogger.setErrorMessage("team " + teamName + " already exists");
        } else {
            // see implementation above
            if (checkIfThereIsEnoughPlayersToCreateNewTeam()) {
                String coachName =
                        promptForStringWithPatternUntilUserInputMatchingOne(
                            "^[a-zA-Z]+\\s+[a-zA-Z]+$",
                            "Please enter coach name " +
                                    "(Two words, no digits, like 'John Doe')",
                            "Invalid coach name");
                Team team = new Team(teamName,coachName);
                mTeamsSet.add(team);
                mLogger.setSuccessMessage("team is created and added to " +
                        "list of teams");
            }
        }
    }

    // get team at specific index from mTeamsSet converting Set to ArrayList
    // used in removeTeamIfExists() method
    private Team getTeamAtIndexOfSetConvertedToList(int index) {
        List<Team> teamList = new ArrayList<>(mTeamsSet);
        return teamList.get(index);
    }

    // is executed upon "remove" choice in main menu,
    // shows available teams first with ids, for user to choose
    private void removeTeamIfExists() throws IOException {
        // show available teams with ids
        showAvailableObjectsWithIdsInSet(mTeamsSet,"Teams","Team");
        // get id from user
        int parsedTeamId = promptUserForId();
        // if id is acceptable
        if (parsedIntIsInRangeOfGivenSet(parsedTeamId, mTeamsSet)) {
            // get team at id
            // id - 1, because we want user to pick a number from 1, not zero
            Team team = getTeamAtIndexOfSetConvertedToList(parsedTeamId - 1);
            // remove team from TeamsSet
            mTeamsSet.remove(team);
            mLogger.setSuccessMessage("Team " + team
                    + " is successfully removed");
        }
    }



    // is executed upon "choose" choice in main menu, redirects to team change
    // prompter. After its ready, returns changed Team object
    private void switchToTeamMenu() throws IOException {
        // show teams with ids
        showAvailableObjectsWithIdsInSet(mTeamsSet,"Teams","Team");
        // get team id from user
        int parsedTeamId = promptUserForId();
        // check if id is in right range
        if (parsedIntIsInRangeOfGivenSet(parsedTeamId,mTeamsSet)) {
            // set mPickedTeam
            // ids presented to user will be from 1 to teams.size(), which is natural
            // for user. That's why we pass int with minus one to
            // getTeamAtIndexOfSetConvertedToList
            mPickedTeam = getTeamAtIndexOfSetConvertedToList(parsedTeamId - 1);
            // run TeamChangePrompter with picked team, also passing
            // Buffered Reader, Logger - for testing purposes, and PlayersSet
            // because it is our dynamic database
            TeamChangePrompter teamChangePrompter =
                    new TeamChangePrompter(
                            mPickedTeam, mBufferedReader, mLogger,mPlayersSet);
            // do stuff with picked team, and return it afterwards
            mPickedTeam = teamChangePrompter.presentMenuWithPossibleOptions();
        } // else is implemented in Prompter.parsedIntIsInRangeOfGivenSet()
    }

    // is executed upon "experience" choice in main menu
    // shows experience distribution given a Map from
    // Team.getMapOfPlayersWithDifferentExperience()
    private void showExperienceCountOnTeams() {
        System.out.println("---- List of available teams:");
        System.out.println("-- Experience distribution " +
                "- Experience level: # of players with this experience");
        for (Team team: mTeamsSet) {
            System.out.println(team);
            team.getMapOfPlayersWithDifferentExperience().entrySet()
                    .stream()
                    .forEach(
                            object -> System.out.printf("%s: %s, ",
                                    object.getKey(),object.getValue())
                    );
            System.out.printf("%n--%n");
        }
        System.out.println("---- ");
    }

    // is executed upon "height" option in main menu
    // very similar to height distribution, but I don't know how
    // to makes them DRY. I guess more reading is needed
    private void showHeightDistributionOnTeams() {
        System.out.println("---- List of available teams:");
        System.out.println("-- Height distribution " +
                "- height: # of players this high");
        for (Team team: mTeamsSet) {
            System.out.println(team);
            team.getMapOfNumberOfPlayersWithSpecificHeight().entrySet()
                    .stream()
                    .forEach(
                            object -> System.out.printf("%s: %s, ",
                                    object.getKey(),object.getValue())
                    );
            System.out.printf("%n--%n");
        }
        System.out.println("----");
    }

    // fill menu map with options, used in constructor
    // printed here to be close to actual switch that has all options, see
    // processUserChoice()
    private void fillMenuMapWithOptions() {
        mMenu.put("height", "Show height distribution on teams");
        mMenu.put("experience", "Show experience distributions in teams");
        mMenu.put("choose", "choose team and go to team Menu");
        mMenu.put("show", "List available teams");
        mMenu.put("create", "Create new team");
        mMenu.put("quit", "Exit menu");
        mMenu.put("remove", "Remove team if exists");
    }
    // main menu, where teams can be created, picked and shown
    private void processUserChoice(String userChoice) throws IOException {
        // All options are available, when TeamsSet is more than zero
        if (mTeamsSet.size() > 0) {
            switch (userChoice) {
                case "height":
                    showHeightDistributionOnTeams();
                    break;
                case "experience":
                    showExperienceCountOnTeams();
                    break;
                case "create":
                    createNewTeam();
                    break;
                case "remove":
                    removeTeamIfExists();
                    break;
                case "show":
                    showAvailableTeams();
                    break;
                case "choose":
                    switchToTeamMenu();
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
            // I want to show nothing if there are no teams, so only
            // create option will be available. Now other options will be
            // seen as well, but I don't know how to simply fix that
            switch (userChoice) {
                case "create":
                    createNewTeam();
                    break;
                case "quit":
                    mLogger.setSuccessMessage("Exiting...");
                    break;
                default:
                    mLogger.setErrorMessage("Unknown choice or no teams: '" +
                            userChoice + "'. Try again.");
                    break;
            }
        }
    }


    // main method, that prompts for team menu, redirecting to
    // processUserChoice or presentTeamMenu functions
    public void presentMenuWithPossibleOptions() throws IOException {
        String userChoice = "";
        do {
            printMenuItems("Main Menu");
            // this method is implemented is inherited from parent Prompter
            // class, we deal with IOException there as well
            userChoice =
                    promptForStringAndReturnInput().trim().toLowerCase();
            // this method see above
            processUserChoice(userChoice);
            // see implementation in Prompter class
            throwNewRunTimeExceptionIfNumberOfWhileCallsIsBig();
        } while(!userChoice.equals("quit"));
    }
}
