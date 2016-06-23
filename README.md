# Techdegree project 2 
## Soccer League Organizer
This project present Organizer of Teams (Team.java), with players
(Player.java) in the League. Method with `public static void main` 
function is in the LeagueManager.java file. 

1.  As an organizer, I should be presented with a menu item that allows 
    me to create a new team, so that I can build the season.
    Required fields are team name and coach. 
    <hr>
    This is solved in `MainMenuPrompter.createNewTeam()` method. When
    a LeagueManager is running, user is presented with menu items, 
    one of which is "create". After typing create, user is asked to
    give a name of new Team, then coach name. Here are steps to create
    a team:
    1. "create" - to create some team
    2. "Sharks" - as team name
    3. "John Doe" - as coach name
    <hr>
2.  As an organizer, I should be presented with a menu item that allows 
    me to add players to a team, so that I can build fair teams.
    <hr>
    This is solved in `TeamChangePrompter.addPlayersToTeam()` method.
    When a LeagueManager is running, user is presented with menu items.
    Here are steps to add player from main menu.
    1. "create" - to create some team
    2. "Sharks" - as team name
    3. "John Doe" - as coach name
    4. "choose" - to choose team from list of teams with ids
        - Here we enter Team menu, i.e. `TeamChangePrompter`
    5. "add" - to add players from database
    6. "1" - to choose first player from database
    <hr>
3.  As an organizer, I should be presented with a menu item that allows 
    me to remove players from a team, so that I can attempt to produce 
    more fair teams.
    <hr>
    Continuing from step *2.*, type following to remove player:
    1. "remove"
        - User will be presented with players with ids, to pick from
    2. "1" - to remove first player from team
    <hr>
4.  As an organizer adding or removing a player to a yet to be chosen 
    team, I should be prompted with an alphabetically ordered list of 
    teams to choose from, so that I can quickly locate the team and 
    avoid typos.
    <hr>
    User is presented with list of ordered Teams in main menu to 
    choose, when he types "choose". corresponding method, showing list 
    is `showAvailableObjectsWithIdsInSet(mTeamsSet, "Teams", "Team")`,
    it is executed in `MainMenuPrompter.switchToTeamMenu()`
    <hr>
5.  As an organizer adding or removing a player to a chosen team, I 
    should be prompted with an alphabetically ordered list of players 
    along with their stats, so that I can quickly locate the player 
    and take action.
    <hr>
    User is presented with ordered list of players after following
    steps *2.1 to 2.4* (see above) to `TeamChangePrompter` - Team
    change menu, and then typing:
    1. "show team" - he will be presented with list of team players. 
        - The method responsible for showing team players to remove is 
        `showAvailableObjectsWithIdsInSet(
        mTeam.getPlayersSet, "Team players", "Player")` in 
        TeamChangePrompter.
    2. "show all"  - he will be presented with list of players
        in database.
        - The method responsible for showing players in database to add is 
        `showAvailableObjectsWithIdsInSet(
        mPlayersSet, "Players database", "Player")` in TeamChangePrompter.
    3. "add" - he will be presented with list of database players with 
        ids, to choose from.
    4. "remove" - he will be presented with list of team players with 
        ids, to choose from.
    <hr>
6.  As an organizer planning teams, I should be able to view a report 
    of a chosen team grouped by height, so that I can determine if 
    teams are fair.
    <hr>
    User is presented with map of players by height after following
    steps *2.1 to 2.4* (see above) to `TeamChangePrompter` - Team
    change menu and then typing:
    - "height"
        - Responsible method is 
        `TeamChangePrompter.showHeightDistributionOfTeam()`.

    Also in main menu, after creating team, and adding players to it,
    following steps *2.1 to 2.6*, then typing:
    - "quit" - to exit Team menu, back to Main Menu
    - "height" - shows height distribution of players for all
        teams.
        - Responsible method is 
        `MainMenuPrompter.showHeightDistributionOnTeams()`.
    <hr>
7.  As an organizer who is planning teams, I should be able to see a 
    League Balance Report for all teams in the league showing a total 
    count of experienced players vs. inexperienced players, so I can 
    determine from a high level if the teams are fairly balanced 
    regarding previous experience.
    <hr>
    User is presented with map of players by experience after following
    steps *2.1 to 2.4* (see above) to `TeamChangePrompter` - Team
    change menu and then typing:
    - "experience"
        - Responsible method is 
        `TeamChangePrompter.showExperienceDistribution()`.
    
    Also in main menu, after creating team, and adding players to it,
    following steps *2.1 to 2.6*, then typing:
    - "quit" - to exit Team menu, back to Main Menu
    - "experience" - shows experience distribution of players for all
    teams.
        - Responsible method is `MainMenuPrompter.showExperienceCountOnTeams()`.
8.  As a coach of a team, I should be able to print out a roster of 
    all the players on my team, so that I can plan appropriately. 
    <hr>
    User is presented with ordered list of players after following
    steps *2.1 to 2.4* (see above) to `TeamChangePrompter` - Team
    change menu, and then typing:
    1. "show team" - he will be presented with list of team players. 
        - The method responsible is 
        `showAvailableObjectsWithIdsInSet(
        mTeam.getPlayersSet, "Team players", "Player")` in 
        TeamChangePrompter.
### Extra credit 
9.  As an administrator, I shouldn't be able to add more teams than 
    there are available players so that teams can be arranged 
    appropriately.
    <hr>
    Given set of 33 players with maximum number of players in team
    as 11, and 3 created teams, after following steps in *1.*
    When user will try to create 4-th team, then error message should be 
    given even if 3 teams are empty.
    Responsible method is 
    `checkIfThereIsEnoughPlayersToCreateNewTeam()`. Returns true if it
    is acceptable to create new team, and false otherwise.
    <hr>
10. As an administrator, I should only have the option of adding 
    players that are not on existing teams, so that I don't 
    accidentally put the player on two teams.
    <hr>
    This is solved by using `mPlayersSet` member variable of 
    MainMenuPrompter class. Upon creation of MainMenuPrompter class, 
    `mPlayersSet` is filled with `Players.load()` database. Later on 
    this set is passed to TeamChangePrompter, so that when player 
    is added or removed, `mPlayersSet` is changed accordingly, thus
    being a dynamic database. This may be not the best solution to 
    have this kind of big data class, swinging around, but the only I 
    could come up with.
    <hr>
11. As an administrator, my team report should show an average 
    experience level so that we can try to manually balance fairness.
    <hr>
    Not calculating any average unfortunately. Only have map solution,
    see *7.*
    <hr>
12. As an administrator who is planning teams, the League Balance 
    Report should also include a count of how many players of each 
    height are on each team.
    <hr>
    Again using map solution of height distribution, see *6.*
    
