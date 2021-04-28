package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DBConnector implements IO {
/*-----------------CHANGE PASSWORD IN LINE 21-----------------------------------------------------------*/
   final DateTimeFormatter dBDateTimeFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss");
   final DateTimeFormatter dBDateFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd");
   final DateTimeFormatter systemDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
   final DateTimeFormatter systemDateFormat = DateTimeFormatter.ofPattern("dd-MM-yy");

   private String driverName = "com.mysql.jdbc.Driver";
   private PreparedStatement pstmt = null;
   private Connection connection;

   public void DBBasicMethodSqlImplement() {
      try {
         this.connection = DriverManager.getConnection("jdbc:mysql://localhost/tournament_manager", "root", "Password");
      }
      catch (SQLException throwables) {
         throwables.printStackTrace();
      }
   }

   @Override
   public void setTournamentIdCounterData(String filePath){
      Connection conn = null;
      ResultSet rs = null;
      ResultSet rs2 = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql1 =
         "SET @@SESSION.information_schema_stats_expiry = 0";

         String sql2 =
         "SELECT AUTO_INCREMENT\n" +
         "FROM  INFORMATION_SCHEMA.TABLES\n" +
         "WHERE TABLE_SCHEMA = 'tournament_manager'\n" +
         "AND   TABLE_NAME   = 'tournaments'";

         PreparedStatement pstmt = conn.prepareStatement(sql1);
         PreparedStatement pstmt2 = conn.prepareStatement(sql2);

         rs = pstmt.executeQuery();
         rs2 = pstmt2.executeQuery();

         while(rs2.next()){
            int idCounter = rs2.getInt("AUTO_INCREMENT");
            Tournament.setIdCounter(rs2.getInt("AUTO_INCREMENT"));
         }

      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null || rs2 != null){
               rs.close();
               rs2.close();
            }
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   @Override
   public void setTeamIdCounterData(String filePath){
      Connection conn = null;
      ResultSet rs = null;
      ResultSet rs2 = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql1 =
         "SET @@SESSION.information_schema_stats_expiry = 0";

         String sql2 =
         "SELECT AUTO_INCREMENT\n" +
         "FROM  INFORMATION_SCHEMA.TABLES\n" +
         "WHERE TABLE_SCHEMA = 'tournament_manager'\n" +
         "AND   TABLE_NAME   = 'teams'";

         PreparedStatement pstmt = conn.prepareStatement(sql1);
         PreparedStatement pstmt2 = conn.prepareStatement(sql2);

         rs = pstmt.executeQuery();
         rs2 = pstmt2.executeQuery();

         while(rs2.next()){
            int idCounter = rs2.getInt("AUTO_INCREMENT");
            Team.setIdCounter(rs2.getInt("AUTO_INCREMENT"));
         }

      }catch (SQLException ex) {
         System.out.println(ex.getMessage());
      } finally {
         try {
            if(rs != null || rs2 != null){
               rs.close();
               rs2.close();
            }
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   @Override
   public void readTournamentData(String path) {
      try {
         String sql = "SELECT * FROM tournaments";

         DBBasicMethodSqlImplement();
         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("tournament_name");
            String sport = rs.getString("sport");
            String tournamentMode = rs.getString("tournament_mode");
            LocalDateTime signUpDeadline = rs.getTimestamp("sign_up_deadline").toLocalDateTime();
            String signUpDeadLineAsString = signUpDeadline.format(systemDateTimeFormat).toString();

            Main.tournaments.add(new Tournament(id, name, sport, tournamentMode, signUpDeadLineAsString));
         }

         readGameDateData(path, null);
         readTeamData(path, null);

         rs.close();
      }

      catch (SQLException throwables) {
         throwables.printStackTrace();
      }
   }

   @Override
   public void readGameDateData(String path, Tournament tournament) {
      try {
         DBBasicMethodSqlImplement();

         String sql =
         "SELECT tournaments.id, game_dates.game_date\n" +
         "FROM ((tournament_game_dates\n" +
         "INNER JOIN tournaments ON tournament_game_dates.fk_tournament_id = tournaments.id)\n" +
         "INNER JOIN game_dates ON tournament_game_dates.fk_game_date_id = game_dates.id)";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next()){
            int tournamentId = rs.getInt("id");
            LocalDate date = rs.getDate("game_date").toLocalDate();
            String dateAsString = date.format(systemDateFormat).toString();

            for(Tournament tournament2Add2 : Main.tournaments){
               if(tournamentId == tournament2Add2.getId()){
                  tournament2Add2.addGameDates(dateAsString);
               }
            }
         }

         rs.close();
      }

      catch (SQLException throwables) {
         throwables.printStackTrace();
      }
   }

   @Override
   public void readTeamData(String path, Tournament tournament) {
      try {
         DBBasicMethodSqlImplement();

         String sql =
         "SELECT teams.id, teams.team_name, teams.still_in_tournament, teams.points, " +
         "teams.goals_made, teams.goals_let_through, tournaments.id AS 'tournament_id'\n" +
         "FROM teams\n" +
         "INNER JOIN tournaments ON teams.fk_tournament_id = tournaments.id;";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("team_name");
            boolean stillInTournament = rs.getBoolean("still_in_tournament");
            int points = rs.getInt("points");
            int goalsMade = rs.getInt("goals_made");
            int opposingTeamsGoals = rs.getInt("goals_let_through");
            int tournamentId = rs.getInt("tournament_id");

            Team team = new Team(id, name, stillInTournament, points, goalsMade, opposingTeamsGoals);

            for(Tournament tournament2Add2 : Main.tournaments){
               if(tournamentId == tournament2Add2.getId()){
                  tournament2Add2.addTeam(team);
               }
            }
         }

         readPlayerData(path, null);

         rs.close();
      }
      catch (SQLException throwables) {
         throwables.printStackTrace();
      }
   }

   @Override
   public void readPlayerData(String path, Team team){
      try {
         DBBasicMethodSqlImplement();

         String sql =
         "SELECT players.player_name, teams.id AS 'team_id'\n" +
         "FROM players\n" +
         "INNER JOIN teams ON players.fk_team_id = teams.id;";

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next()) {
            String name = rs.getString("player_name");
            int teamId = rs.getInt("team_id");

            for(Tournament tournament : Main.tournaments){
               for(Team team2add2 : tournament.getTeams()){
                  if(teamId == team2add2.getId()){
                     team2add2.addPlayer(name);
                  }
               }
            }
         }

         rs.close();
      }
      catch (SQLException throwables) {
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
         String signUpDeadline = tournament.getSignUpDeadline().format(dBDateTimeFormat);
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
            String dateAsString = date.format(dBDateFormat);
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
   public void deleteTournamentData(String path, Tournament tournament)
   {
      Connection conn = null;
      ResultSet rs = null;

      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql = "DELETE FROM teams WHERE id=?";

         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         pstmt.setInt(1, tournament.getId());

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
      /*
         Connection conn = null;
      try
      {
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql = "DELETE FROM teams WHERE id=?";
         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         pstmt.setInt(2, team.getId());

         pstmt = this.connection.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();

         while (rs.next())
            rs.close();
      }

      catch (SQLException throwables)
      {
         throwables.printStackTrace();
      }*/
   }

   @Override
   public void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner) {
      Connection conn = null;
      ResultSet rs = null;
      boolean firstTeamDone = false;
      try{
         DBBasicMethodSqlImplement();
         conn = this.connection;

         String sql =
                 "INSERT INTO team_matches (fk_team_id, goals)\n" +
                         "VALUES (?, ?)";

         PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

         for (int i = 0; i < teams.length; i++) {
            pstmt.setInt(1, teams[i].getId());
            if(i == 0){
               pstmt.setInt(2, team1Goals);
            }else{
               pstmt.setInt(2, team2Goals);
            }
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
   public void saveMatches(Match data) {

   }

   @Override
   public void showMatchMenu(String matchType) {
      readTournamentData("path");
      for (int i = 0; i < Main.tournaments.size(); i++) {
         int tourId = Main.tournaments.get(i).getId();
         if(tourId == Integer.parseInt(matchType)){
            Main.tournaments.get(i).randomTeamsToMatch();
         }
      }
   }
}