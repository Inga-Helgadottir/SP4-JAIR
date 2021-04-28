package com.company;

public class Controller {
   public static IO io;
   public static UI ui = new UI();

   enum Datasource{
      DATABASE,
      TXTFILE
   }

   private static Datasource src;
   private static String path;

   public static void setDataSrc(String dataSrcType){
      if(dataSrcType.equals("1")){
         src = Datasource.DATABASE;
         ui.displayMsg("\nThe system will run through database");
      }else if(dataSrcType.equals("2")){
         src = Datasource.TXTFILE;
         ui.displayMsg("\nThe system will run locally");
      }
   }

   public static IO getIO() {
      IO io = null;

      if(src == Datasource.DATABASE){
         path = null;
         io = new DBConnector();
      }else if (src == Datasource.TXTFILE){
         path = "src/data";
         io = new FileHandler();
      }
      return io;
   }

   public static void loadData(){
      io = getIO();
      io.setTournamentIdCounterData(path);
      io.setTeamIdCounterData(path);
      io.readTournamentData(path);
   }

   public static void saveTournamentData(Tournament tournament){
      io = getIO();
      io.saveTournamentData(path, tournament);
   }

   public static void saveGameDateData(Tournament tournament){
      io = getIO();
      io.saveGameDateData(path, tournament);
   }

   public static void saveTeamData(Team team, Tournament tournament){
      io = getIO();
      io.saveTeamData(path, team, tournament);
   }

   public static void savePlayerData(Team team){
      io = getIO();
      io.savePlayerData(path, team);
   }

   public static void deleteTournamentData(Tournament tournament){
      io = getIO();
      io.deleteTournamentData(path, tournament);
   }

   public static void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner) {
      io = getIO();
      io.updateGoals(teams, team1Goals, team2Goals, winner);
   }

   public static void saveMatches(Match data){
      io = getIO();
      io.saveMatches(data);
   }

   public static void showMatchMenu(String matchType){
      io = getIO();
      io.showMatchMenu(matchType);
   }

}
