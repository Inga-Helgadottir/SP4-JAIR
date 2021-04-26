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

   public static void loadData(){
      io = getIO();
      io.readTournamentData(path);

      if(io instanceof FileHandler){
         Tournament.setIdCounter(FileHandler.readIdCounterData(path + "/idCounters/idCounter_Tournament.txt"));
         Team.setIdCounter(FileHandler.readIdCounterData(path + "/idCounters/idCounter_Team.txt"));
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

   public static void saveTournamentData(Tournament tournament){
      io = getIO();
      io.saveTournamentData(path, tournament);
   }

   public static void saveGameDateData(Tournament tournament, String date){
      io = getIO();
      io.saveGameDateData(path, tournament, date);
   }

}