package com.company;

import java.util.ArrayList;

public class Team {
   final private static UI ui = new UI();
   static private int idCounter;

   private int id;
   private String name;
   private ArrayList<String> players = new ArrayList<String>();
   private boolean stillInTournament = true;
   private int point = 0;
   private int goalsMade = 0;
   private int opposingTeamsGoals = 0;

   // For team created by user
   public Team(String name) {
      this.id = idCounter;
      this.name = name;
      idCounter++;
   }

   // For team created from data
   public Team(int id, String name, boolean stillInTournament, int point, int goalsMade, int opposingTeamsGoals) {
      this.id = id;
      this.name = name;
      this.stillInTournament = stillInTournament;
      this.point = point;
      this.goalsMade = goalsMade;
      this.opposingTeamsGoals = opposingTeamsGoals;
   }

   public int getId(){
      return id;
   }

   public static int getIdCounter(){
      return idCounter;
   }

   public static void setIdCounter(int idCounterFromData){
      idCounter = idCounterFromData;
   }

   public ArrayList<String> getPlayers(){
      return players;
   }

   public int getPoint(){
      return this.point;
   }

   public String getName() {
      return name;
   }

   public int getGoalsMade() {
      return goalsMade;
   }

   public int getOpposingTeamsGoals() {
      return opposingTeamsGoals;
   }

   public void setGoalsMade(int goals) {
      this.goalsMade += goals;
   }

   public void setOpposingTeamsGoals(int goals) {
      this.opposingTeamsGoals += goals;
   }

   public void addPlayer(String playerName){
      players.add(playerName);
   }

   public static void registerNewTeam(){
      ui.displayMsg("\n(REGISTER NEW TEAM)");

      if(Main.tournaments.size() != 0){
         ui.displayMsg("\nTournaments currently in the system: ");
         Tournament.displayAllTournaments();

         String userInput = ui.getUserInput("\nType the ID of the tournament the team would like to register " +
         "into \nType -1 to cancel: ");

         if(!userInput.equals("-1")){
            int tournamentId = Integer.parseInt(userInput);

            Tournament tournamentToRegisterInto = Tournament.findTournament(tournamentId);

            if(tournamentToRegisterInto != null){
               String teamName = ui.getUserInput("\nTeam name:");

               ui.displayMsg("\nA team must have at least 2 players to get registered");
               ui.displayMsg("Please enter 2 or more players to complete registration");
               ui.displayMsg("Type -1 to end");

               ArrayList<String> playerNames = new ArrayList<String>();
               boolean stillAdding = true;

               while(stillAdding){
                  String playerName = ui.getUserInput("\nPlayer name: ");

                  if(!playerName.equals("-1")){
                     playerNames.add(playerName);
                  }else{
                     stillAdding = false;
                  }
               }

               if(playerNames.size() == 0){
                  ui.displayMsg("You need at least 2 players to register into the tournament");
                  ui.displayMsg("\nThe team was not registered...");
               }else{
                  Team team = new Team(teamName);
                  tournamentToRegisterInto.addTeam(team);

                  for(String playerName : playerNames){
                     team.addPlayer(playerName);
                  }

                  Controller.saveTeamData(team, tournamentToRegisterInto);
                  Controller.savePlayerData(team);

                  ui.displayMsg("\nThe team was successfully registered!");
               }
            }else{
               ui.displayMsg("\nNo tournament matching the provided id could be found in the system...");
            }
         }
      }else{
         ui.displayMsg("There are currently no tournaments registered in the system.");
      }
   }

   public void joinTournament(Tournament tournamentToJoin){
      tournamentToJoin.addTeam(this);
   }

   public static void displayAllTeams(){
      for(Tournament tournament : Main.tournaments){
         System.out.println(tournament.getTeams());
      }
   }

   @Override
   public String toString() {
      return
      "ID," + this.id + "," +
      "Name," + this.name + "," +
      "Still in tournament," + stillInTournament + "," +
      "Point," + point + "," +
      "Goals made," + goalsMade + "," +
      "Opposing team's goals," + opposingTeamsGoals + "\n";
   }
}
