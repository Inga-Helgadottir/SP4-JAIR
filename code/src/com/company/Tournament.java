package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Tournament {
   final static UI ui = new UI();
   static final DateTimeFormatter myDateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
   static final DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yy");
   static private int idCounter;

   private int id;
   private String name;
   private String sport;
   private String tournamentMode;
   private LocalDateTime signUpDeadline;
   private ArrayList<LocalDate> gameDates = new ArrayList<LocalDate>();
   private ArrayList<Team> teams = new ArrayList<Team>();
   private ArrayList<Match> matches = new ArrayList<Match>();


   // For tournaments created by user
   public Tournament(String name, String sport, String tournamentMode, String signUpDeadline) {
      this.id = idCounter;
      this.name = name;
      this.sport = sport;
      this.tournamentMode = tournamentMode;
      this.signUpDeadline = LocalDateTime.parse(signUpDeadline, myDateTimeFormat);
      idCounter++;
   }

   // For tournaments created from data
   public Tournament(int id, String name, String sport, String tournamentMode, String signUpDeadline) {
      this.id = id;
      this.name = name;
      this.sport = sport;
      this.tournamentMode = tournamentMode;
      this.signUpDeadline = LocalDateTime.parse(signUpDeadline, myDateTimeFormat);
   }


// ******************** Getter and Setter-ish *******************

   public static int getIdCounter(){
      return idCounter;
   }

   public static void setIdCounter(int idCounterFromData){
      idCounter = idCounterFromData;
   }

   public int getId(){
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getSport() {
      return sport;
   }

   public void setSport(String sport) {
      this.sport = sport;
   }

   public String getTournamentMode() {
      return tournamentMode;
   }

   public void setTournamentMode(String tournamentMode) {
      this.tournamentMode = tournamentMode;
   }

   public LocalDateTime getSignUpDeadline() {
      return signUpDeadline;
   }

   public void setSignUpDeadline(LocalDateTime signUpDeadline) {
      this.signUpDeadline = signUpDeadline;
   }

   public ArrayList<LocalDate> getGameDates(){
      return gameDates;
   }

   public void addGameDates(String dateToAdd){
      gameDates.add(LocalDate.parse(dateToAdd, myDateFormat));
   }

   public ArrayList<Team> getTeams() {
      return teams;
   }

   public void addTeam(Team team){
      teams.add(team);
   }

// ***************** Getter and Setter-ish END *******************


// *********************** Other methods *************************

   public void displayTeamRankings(){
      ArrayList<Team> rankings = new ArrayList<Team>();

      for(Team team : teams){
         rankings.add(team);
      }

//      Team teamWithHighestPoint = rankings.get(0);
//
//      for(int i = 0; i < rankings.size(); i++){
//         for(int j = 1; j < (rankings.size() - i); j++){
//            if(rankings.get(j-1).getPoint() > rankings.get(j).getPoint()){
//               //swap elements
//               teamWithHighestPoint = rankings.get(j-1);
//               rankings.add(j-1, rankings.get(j));
//               rankings.add(j, teamWithHighestPoint);
//            }
//
//         }
//      }

      int rankCounter = 1;

      for(Team team : rankings){
         System.out.println(rankCounter + ") " + team.getName() + ": " + team.getPoint() + " points");
         rankCounter++;
      }
   }

// ********************* Other methods END ***********************


// ********************** Static methods *************************

   public static void readTournamentData(String filePath){
      try{
         File tournamentsDir = new File(filePath);
         String tournamentsDirContent[] = tournamentsDir.list();

         for(int i = 0; i<tournamentsDirContent.length; i++) {
            File tournamentData = new File(filePath + "/" + tournamentsDirContent[i] + "/tournamentData.txt");
            Scanner scanner = new Scanner(tournamentData);
            String[] tournamentLine;

            while(scanner.hasNextLine()) {
               String line = scanner.nextLine();
               tournamentLine = line.split(",");

               int id = Integer.parseInt(tournamentLine[1]);
               String name = tournamentLine[3];
               String sport = tournamentLine[5];
               String tournamentMode = tournamentLine[7];
               String signUpDeadline = tournamentLine[9];

               Main.tournaments.add(new Tournament(id, name, sport, tournamentMode, signUpDeadline));

               // Adds dateAndTimes data to the corresponding tournament
               readTournamentGameDateData(filePath, Main.tournaments.get(i));
               readTournamentTeamData(filePath, Main.tournaments.get(i));
            }
            scanner.close();
         }
      }catch(IOException e){
         System.out.println(e);
      }
   }

   public static void readTournamentGameDateData(String filePath, Tournament tournament){
      try{
         File tournamentsDir = new File(filePath);

         File gameDateData = new File(filePath + "/" + tournament.getName() + "/gameDateData.txt");
         Scanner scanner = new Scanner(gameDateData);
         String[] gameDateLines;

         while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            gameDateLines = line.split(",");

            for(String gameDateLine : gameDateLines){
               tournament.addGameDates(gameDateLine);
            }
         }
         scanner.close();
      }catch(IOException e){
         System.out.println(e);
      }
   }

   public static void readTournamentTeamData(String filePath, Tournament tournament){
      try{
         File tournamentsDir = new File(filePath);

         File teamData = new File(filePath + "/" + tournament.getName() + "/teamData.txt");
         Scanner scanner = new Scanner(teamData);
         String[] teamLines;

         while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            teamLines = line.split(",");

            int id = Integer.parseInt(teamLines[1]);
            String name = teamLines[3];
            boolean stillInTournament = Boolean.parseBoolean(teamLines[5]);
            int point = Integer.parseInt(teamLines[7]);
            int goalsMade = Integer.parseInt(teamLines[9]);
            int opposingTeamsGoals = Integer.parseInt(teamLines[11]);

            tournament.teams.add(new Team(id, name, stillInTournament, point, goalsMade, opposingTeamsGoals));
         }
         scanner.close();
      }catch(IOException e){
         System.out.println(e);
      }
   }

   public static void saveTournamentData(String filePath, String fileName, String data){
      String fileData = data;

      try{
         Main.createNewDir(filePath, fileName);

         File file = new File(filePath + "/" + fileName + "/tournamentData.txt");
         FileWriter fileWriter = new FileWriter(file, false);
         fileWriter.write(data);
         fileWriter.close();
      }catch (IOException e){
         System.out.println(e.getCause());
      }
   }

   public static void registerNewTournament(){
      ui.displayMsg("\n(REGISTER NEW TOURNAMENT)");

      String name = ui.getUserInput("\nTournament name:");
      String sport = ui.getUserInput("Sport:");
      String mode = ui.getUserInput("Tournament mode (knock-out or group):");
      String signUpDeadline = ui.getUserInput("Signup deadline (dd-MM-yy HH:mm):");

      Tournament tournament = new Tournament(name, sport, mode, signUpDeadline);
      Main.tournaments.add(tournament);
      ui.displayMsg("\nNew tournament has been registered!");

      Main.saveIdCounterData("src/data/idCounters/idCounter_Tournament.txt", "ID:" + Tournament.getIdCounter());
      saveTournamentData("src/data/tournaments", name, tournament.toString());
      Main.saveData("src/data/tournaments/" + name + "/gameDateData.txt", "");
      Main.saveData("src/data/tournaments/" + name + "/teamData.txt", "");

      String willAddDateAndTimesNow = ui.getUserInput("\nWould you like to add game dates now? (y/n):").toLowerCase();

      if(willAddDateAndTimesNow.equals("y")){
         ui.displayMsg("\n(ADD GAME DATE)");
         ui.displayMsg("\nWrite the game date you would like to add to the tournament");
         ui.displayMsg("Type -1 to end\n");
         boolean stillAdding = true;

         while(stillAdding){
            String date = ui.getUserInput("Date (dd-MM-yy): ");

            if(date.equals("-1")){
               ui.displayMsg("\nAll game dates has now been saved");
               stillAdding = false;
               return;
            }else{
               tournament.addGameDates(date);
               Main.saveData("src/data/tournaments/" + name + "/gameDateData.txt", date + "\n");
            }
         }
      }
   }

   public static void displayAllTournaments(){
      for(Tournament tournament : Main.tournaments){
         System.out.println("- " + tournament.getName() + " (ID: " + tournament.getId() + ")");
      }
   }

   public static Tournament findTournament(int id){
      Tournament tournamentMatch = null;

      for(Tournament tournament : Main.tournaments){
         if(tournament.getId() == id){
            tournamentMatch = tournament;
            break;
         }
      }

      return tournamentMatch;
   }

   public static void displayTeamRankings(Tournament tournament){
      ArrayList<Team> rankings = new ArrayList<Team>();

      for(Team team : tournament.getTeams()){
         rankings.add(team);
      }

      int n = rankings.size();
      for (int i = 0; i < n-1; i++)
         for (int j = 0; j < n-i-1; j++)
            if (rankings.get(j).getPoint() < rankings.get(j+1).getPoint())
            {
               // swap arr[j+1] and arr[j]
               Team temp = rankings.get(j);
               rankings.set(j, rankings.get(j+1));
               rankings.set(j+1, temp);
            }

      int rankCounter = 1;

      for(Team team : rankings){
         System.out.println(rankCounter + ") " + team.getName() + ": " + team.getPoint() + " points");
         rankCounter++;
      }
   }

   public static ArrayList<LocalDate> showGameDates(Tournament tournament){
      ArrayList<LocalDate> gameDates = tournament.gameDates;
      return gameDates;
   }

   public static void deleteTournament(){
      ui.displayMsg("\n(DELETE TOURNAMENT)");
      ui.displayMsg("\nTournaments currently in the system: ");

      if(Main.tournaments.size() != 0){
         displayAllTournaments();

         String userInput = ui.getUserInput("\nType the ID of the tournament you " +
                 "would like to delete\nType -1 to cancel: ");

         if(!userInput.equals("-1")){
            int tournamentId = Integer.parseInt(userInput);

            Tournament tournamentToBeDeleted = findTournament(tournamentId);

            if(tournamentToBeDeleted != null){
               File fileToBeDeleted = new File("src/data/tournaments/" + tournamentToBeDeleted.getName());
               Main.deleteFolder(fileToBeDeleted);

               Main.tournaments.remove(findTournament(tournamentId));

               ui.displayMsg("\nThe tournament was successfully deleted!");
            }else{
               ui.displayMsg("\nNo tournament matching the provided id could be found in the system...");
            }
         }
      }else{
         ui.displayMsg("There are currently no tournaments registered in the system...");
      }
   }

   // ******************** Static methods END ***********************
   public void randomTeamsToMatch(){
      int amountOfTeams = teams.size();
      ArrayList<Integer> randDone = new ArrayList<>();
      if(amountOfTeams < 2) {
         System.out.println("You need at least 2 teams to start a match");
      }else if(amountOfTeams % 2 != 0){
         System.out.println("You have an uneven number of teams");
      }else if(amountOfTeams == 2){
         Team[] matchTeams = {teams.get(0), teams.get(1)};
         Match m = new Match(matchTeams);
         saveMatchesToFile(m);
      }else{
         Random rand = new Random();
         while(randDone.size() < amountOfTeams){
            int r1 = rand.nextInt(amountOfTeams - 0) + 0;
            int r2 = rand.nextInt(amountOfTeams - 0) + 0;
            boolean result = randDone.contains(r1);
            boolean result2 = randDone.contains(r2);
            if(result == false && result2 == false && r2 != r1){
               randDone.add(r1);
               randDone.add(r2);
               Team[] matchTeams = {teams.get(r1), teams.get(r2)};
               Match m = new Match(matchTeams);
               saveMatchesToFile(m);
            }
         }
      }
   }

   public void saveMatchesToFile(Match data){
      try{
         File file = new File("src/data/matches/matchesBetween.txt");
         FileWriter fr = new FileWriter(file, true);
         String myData = data.toString() + "\n";
         fr.write(myData);
         fr.close();
         Main.printTournamentData(file);
      }catch (IOException e){
         System.out.println(e.getCause());
      }
   }

   @Override
   public String toString(){
      return "ID," + this.id + "," +
             "Name," + this.name + "," +
             "Sport," + this.sport + "," +
             "Tournament mode," + this.tournamentMode + "," +
             "SignUp deadline," + this.signUpDeadline.format(myDateTimeFormat);
   }

}
