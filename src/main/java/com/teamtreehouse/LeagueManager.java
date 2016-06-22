package com.teamtreehouse;
import com.teamtreehouse.controller.MainMenuPrompter;
import com.teamtreehouse.controller.TeamChangePrompter;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.io.IOException;

public class LeagueManager {
    public static void main(String[] args) throws IOException {
        Player[] players = Players.load();
        System.out.printf("There are currently %d registered players.%n", players.length);
        // Creating new MainMenuPrompter controller class where all our menus
        MainMenuPrompter prompter = new MainMenuPrompter();
        // main menu call
        prompter.presentMenuWithPossibleOptions();
    }
}