package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Scanner;

public class FileHandler implements IO{

   public static int readIdCounterData(String filePath){
      String[] idCounterLine;
      int idCounter = 0;

      try{
         File file = new File(filePath);
         Scanner scanner = new Scanner(file);
         String line = scanner.nextLine();
         idCounterLine = line.split(":");
         idCounter = Integer.parseInt(idCounterLine[1]);
         scanner.close();
      }catch(FileNotFoundException e){
         System.out.println(e);
      }

      return idCounter;
   }

   public static void saveData(String filePath, String data, boolean willAppend){
      String fileData = data;

      try{
         File file = new File(filePath);
         FileWriter fileWriter = new FileWriter(file, willAppend);
         fileWriter.write(data);
         fileWriter.close();
      }catch (IOException e){
         System.out.println(e.getCause());
      }
   }

   public static void createNewDir(String filePath){
      File newDir = new File(filePath);
      newDir.mkdir();
   }

   public static void deleteFolder(File file){
      try{
         File[] allContents = file.listFiles();
         if (allContents != null) {
            for (File content : allContents) {
               deleteFolder(content);
            }
         }
         Files.delete(file.toPath());
      }catch(IOException e){
         System.out.println(e);
      }
   }

   public static void printTournamentData(File file){
      Scanner input = null;
      try {
         input = new Scanner(file);

         while (input.hasNextLine())
         {
            System.out.println(input.nextLine());
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void setTournamentIdCounterData(String filePath){
      int currentIdCounter = readIdCounterData(filePath + "/idCounters/idCounter_Tournament.txt");
      Tournament.setIdCounter(currentIdCounter);
   }

   @Override
   public void setTeamIdCounterData(String filePath){
      int currentIdCounter = readIdCounterData(filePath + "/idCounters/idCounter_Team.txt");
      Team.setIdCounter(currentIdCounter);
   }

   @Override
   public void readTournamentData(String filePath){
      try{
         File tournamentsDir = new File(filePath + "/tournaments");
         String tournamentsDirContent[] = tournamentsDir.list();

         for(int i = 0; i<tournamentsDirContent.length; i++) {
            File tournamentData = new File(filePath + "/tournaments/" + tournamentsDirContent[i] + "/tournamentData.txt");
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
               readGameDateData(filePath, Main.tournaments.get(i));
               readTeamData(filePath, Main.tournaments.get(i));
            }
            scanner.close();
         }
      }catch(IOException e){
         System.out.println(e);
      }
   }

   @Override
   public void readGameDateData(String filePath, Tournament tournament){
      try{
         File tournamentsDir = new File(filePath);

         File gameDateData = new File(filePath + "/tournaments/" + tournament.getName() + "/gameDateData.txt");
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

   @Override
   public void readTeamData(String filePath, Tournament tournament){
      try{
         File tournamentsDir = new File(filePath);

         File teamData = new File(filePath + "/tournaments/" + tournament.getName() + "/teamData.txt");
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

            tournament.addTeam(new Team(id, name, stillInTournament, point, goalsMade, opposingTeamsGoals));
         }
         scanner.close();
      }catch(IOException e){
         System.out.println(e);
      }
   }

   @Override
   public void readPlayerData(String path, Team team){

   }

   @Override
   public void saveTournamentData(String filePath, Tournament tournament){
      saveData(filePath + "/idCounters/idCounter_Tournament.txt", "ID:" + Tournament.getIdCounter(), false);
      createNewDir(filePath + "/tournaments/" + tournament.getName());
      saveData(filePath + "/tournaments/" + tournament.getName() + "/tournamentData.txt", tournament.toString(),
      true);

      //createDirAndTournamentDataFile(filePath + "/tournaments/", tournament.getName(), tournament.toString());
      saveData(filePath + "/tournaments/" + tournament.getName() + "/gameDateData.txt", "", true);
      saveData(filePath + "/tournaments/" + tournament.getName() + "/gameDateData.txt", "", true);
      saveData(filePath + "/tournaments/" + tournament.getName() + "/teamData.txt", "", true);
   }

   @Override
   public void saveGameDateData(String filePath, Tournament tournament){
      for(LocalDate date : tournament.getGameDates()){
         String dateAsString = date.format(Tournament.myDateFormat);

         FileHandler.saveData(filePath + "/tournaments/" + tournament.getName() + "/gameDateData.txt",
         dateAsString + "\n", true);
      }
   }

   @Override
   public void saveTeamData(String path, Team team, Tournament tournament){
      saveData(path + "/idCounters/idCounter_Team.txt", "ID:" + Team.getIdCounter(), false);
      saveData(path + "/tournaments/" + tournament.getName() + "/teamData.txt",
      team.toString(), true);
   }

   @Override
   public void savePlayerData(String path, Team team){

   }

   @Override
   public void deleteTournamentData(String filePath, Tournament tournament){
      File fileToBeDeleted = new File(filePath + "/tournaments/" + tournament.getName());
      deleteFolder(fileToBeDeleted);
   }

   @Override
   public void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner) {
      System.out.println();
      try{
         File file = new File("src/data/matches/matchWinnerData.txt");
         FileWriter fr = new FileWriter(file, true);
         String data = "team1, " + teams[0].getName() + ", team1goals, " + team1Goals + ", team2, " + teams[1].getName() + ", team2goals, " + team2Goals + ", Winner, " + winner + ",\n";
         fr.write(data);
         fr.close();
         printTournamentData(file);
      }catch (IOException e){
         System.out.println(e.getCause());
      }
   }

   @Override
   public void saveMatches(Match data){
      try{
         File file = new File("src/data/matches/matchesBetween.txt");
         FileWriter fr = new FileWriter(file, true);
         String myData = data.toString() + "\n";
         fr.write(myData);
         fr.close();
         printTournamentData(file);
      }catch (IOException e){
         System.out.println(e.getCause());
      }
   }

   @Override
   public void showMatchMenu(String matchType) {
      Tournament t = new Tournament("to", "sd", "sfd", "11-11-21 13:30");

      int tournamentId = Integer.parseInt(matchType);
      Tournament matchTournament = Tournament.findTournament(tournamentId);
      if(matchTournament != null){
         File setTournamentToMatches = new File("src/data/tournaments/" + matchTournament.getName() + "/teamData.txt");
         Scanner scanner = null;
         try {
            scanner = new Scanner(setTournamentToMatches);
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
         if (scanner != null) {
            while (scanner.hasNextLine()) {
               String[] lines = scanner.nextLine().split(",");
               String name = lines[3];
               Team team1 = new Team(name);
               t.addTeam(team1);
            }
            t.randomTeamsToMatch();
            System.out.println("The teams in your tournament have been sent to matches");
         }
      }else{
         System.out.println("There is no tournament with that id");
      }
   }

}
