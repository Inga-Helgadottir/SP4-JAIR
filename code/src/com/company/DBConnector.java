package com.company;
import java.sql.*;

public class DBConnector implements IO
{

   // JDBC driver name and database URL
   //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   //static final String DB_URL = "jdbc:mysql://localhost/tournament_manager";

   //  Database credentials
   //static final String USER = "root";
   //static final String PASS = "Password";

   private String driverName = "com.mysql.jdbc.Driver";
   private PreparedStatement pstmt = null;
   private Connection connection;

   public void DBBasicMethodSqlImplement()
   {
      try
      //connection info
      {
         this.connection = DriverManager.getConnection("jdbc:mysql://localhost/tournament_manager", "root", "Password");
      }
      catch (SQLException throwables)
      {
         throwables.printStackTrace();
      }
   }


   @Override
   public void readTournamentData(String path)
   {
      try
      {
         String sql = "SELECT * FROM tournaments";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         //prints all tournaments data
         {
            System.out.println("ID: " + rs.getInt("id") + ", tournament: " + rs.getString("tournament_name") + ", sport: " + rs.getString("sport") + ", mode: " + rs.getString("tournament_mode") + ", deadline: " + rs.getDate("sign_up_deadline"));
         }
         rs.close();
      }

      catch (SQLException throwables)
      {
         throwables.printStackTrace();
      }
   }
   @Override
   public void readGameDateData(String path, Tournament tournament)
   {
      try
      {
         String sql = "SELECT * FROM game_dates";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         //prints all game_dates data
         {
            System.out.println("ID: " + rs.getInt("id") + ", game date: "+ rs.getDate("game_date"));
         }
         rs.close();
      }

      catch (SQLException throwables)
      {
         throwables.printStackTrace();
      }
   }

   @Override
   public void readTeamData(String path, Tournament tournament)
   {
      try
      {
         String sql = "SELECT * FROM teams";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
         //prints all teams data
         {
            System.out.println("ID: " + rs.getInt("id") + ", team name: " + rs.getString("team_name") + ", still in tournament?: " + rs.getInt("still_in_tournament") + ", point: " + rs.getInt("points") + ", goals made: " + rs.getInt("goals_made") + ", goals let int" + rs.getInt("goals_let_through") + "tournament id: " + rs.getInt("fk_tournament_id"));
         }
         rs.close();
      }

      catch (SQLException throwables)
      {
         throwables.printStackTrace();
      }
   }

   @Override
   public void saveTournamentData(String path, Tournament tournament) {

   }

   @Override
   public void saveGameDateData(String path, Tournament tournament, String date) {

   }

   @Override
   public void saveTeamData(String path, Team team, Tournament tournament) {

   }

   @Override
   public void deleteTournamentData(String path, Tournament tournament) {

   }

   @Override
   public void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner) {

   }

   @Override
   public void saveMatches(Match data) {

   }

   @Override
   public void showMatchMenu(String matchType) {

   }

}