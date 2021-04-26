package com.company;

public class DBConnector implements IO {

   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/tournament_manager";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "kisshu25";

   @Override
   public void readTournamentData(String path) {

   }

   @Override
   public void readGameDateData(String path, Tournament tournament) {

   }

   @Override
   public void readTeamData(String path, Tournament tournament) {

   }

   @Override
   public void saveTournamentData(String path, Tournament tournament){

   }

   @Override
   public void saveGameDateData(String path, Tournament tournament, String date){

   }

}