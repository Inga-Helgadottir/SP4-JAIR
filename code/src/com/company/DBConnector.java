package com.company;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DBConnector implements IO
{

   final DateTimeFormatter myDateTimeFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
   final DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd");

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
         this.connection = DriverManager.getConnection("jdbc:mysql://localhost/tournament_manager", "root", "kisshu25");
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

         DBBasicMethodSqlImplement();
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
         DBBasicMethodSqlImplement();

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
         DBBasicMethodSqlImplement();

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
      Connection conn = null;
      ResultSet rs = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql =
         "INSERT INTO tournaments(tournament_name, sport, tournament_mode, sign_up_deadline)\n" +
         "VALUES(?, ?, ?, CAST(? AS DATETIME))";

         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         pstmt.setString(1, tournament.getName());
         pstmt.setString(2, tournament.getSport());
         pstmt.setString(3, tournament.getTournamentMode());
         String signUpDeadline = tournament.getSignUpDeadline().format(myDateTimeFormat);
         pstmt.setString(4, signUpDeadline);

         pstmt.addBatch();
         pstmt.executeBatch();
      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null)  rs.close();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   @Override
   public void saveGameDateData(String path, Tournament tournament) {
      Connection conn = null;
      ResultSet rs = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql1 =
         "INSERT INTO game_dates (game_date)\n" +
         "SELECT CAST(? AS DATE) WHERE NOT EXISTS (SELECT * FROM game_dates WHERE game_date = ?)";

         String sql2 =
         "INSERT INTO tournament_game_dates (fk_tournament_id, fk_game_date_id)\n" +
         "SELECT tournaments.id, game_dates.id\n" +
         "FROM tournaments, game_dates\n" +
         "WHERE tournaments.id = ? AND game_dates.game_date = ?";

         PreparedStatement pstmt1 = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
         PreparedStatement pstmt2 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);

         for(LocalDate date : tournament.getGameDates()){
            String dateAsString = date.format(myDateFormat);
            pstmt1.setString(1, dateAsString);
            pstmt1.setString(2, dateAsString);
            pstmt2.setInt(1, tournament.getId());
            pstmt2.setString(2, dateAsString);

            pstmt1.addBatch();
            pstmt2.addBatch();
         }

         pstmt1.executeBatch();
         pstmt2.executeBatch();
      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null)  rs.close();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   @Override
   public void saveTeamData(String path, Team team, Tournament tournament) {
      Connection conn = null;
      ResultSet rs = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql =
         "INSERT INTO teams(team_name, fk_tournament_id)\n" +
         "VALUES(?, ?)";

         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         pstmt.setString(1, team.getName());
         pstmt.setInt(2, tournament.getId());

         pstmt.addBatch();
         pstmt.executeBatch();
      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null)  rs.close();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   @Override
   public void savePlayerData(String path, Team team){
      Connection conn = null;
      ResultSet rs = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql =
         "INSERT INTO players (player_name, fk_team_id)\n" +
         "VALUES (?, ?)";

         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         for(String playerName : team.getPlayers()){
            pstmt.setString(1, playerName);
            pstmt.setInt(2, team.getId());
            pstmt.addBatch();
         }

         pstmt.executeBatch();
      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null)  rs.close();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
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