package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
   public void saveGameDateData(String filePath, Tournament tournament, String date){
      FileHandler.saveData(filePath + "/tournaments/" + tournament.getName() + "/gameDateData.txt", date + "\n",
      true);
   }

}