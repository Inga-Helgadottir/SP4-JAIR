package com.company;

import java.util.ArrayList;

public class Team {
   static private int idCounter;

   private int id;
   private String name;
   private ArrayList<String> players = new ArrayList<String>();
   private boolean stillInTournament = true;
   private int point = 0;
   private int goalsMade = 0;
   private int opposingTeamsGoals = 0;

   // For tournaments created by user
   public Team(String name) {
      this.id = idCounter;
      this.name = name;
      idCounter++;
   }

   // For tournaments created from data
   public Team(int id, String name, boolean stillInTournament, int point, int goalsMade, int opposingTeamsGoals) {
      this.id = id;
      this.name = name;
      this.stillInTournament = stillInTournament;
      this.point = point;
      this.goalsMade = goalsMade;
      this.opposingTeamsGoals = opposingTeamsGoals;
   }


   public static int getIdCounter(){
      return idCounter;
   }

   public static void setIdCounter(int idCounterFromData){
      idCounter = idCounterFromData;
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
//      "Players," + players + "," +
      "Still in tournament," + stillInTournament + "," +
      "Point," + point + "," +
      "Goals made," + goalsMade + "," +
      "Opposing team's goals," + opposingTeamsGoals + "\n";
   }
}
