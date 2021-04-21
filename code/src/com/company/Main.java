package com.company;

import java.io.File;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {
    final static UI ui = new UI();

    static ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
    static boolean systemOn = true;
    static Tournament t = new Tournament("to", "sd", "sfd", "11-11-21 13:30");

    public static void main(String[] args) throws IOException{
        //Read all id counters and set to corresponding classes
        Tournament.setIdCounter(readIdCounterData("src/data/idCounters/idCounter_Tournament.txt"));
        Team.setIdCounter(readIdCounterData("src/data/idCounters/idCounter_Team.txt"));

        //Read all tournament data
        Tournament.readTournamentData("src/data/tournaments");

        ui.displayMsg("~ Tournament Manager ~");

        showStartMenu();
        String taskType = ui.getUserInput("\nUser input:");
        handleStartMenuChoice(taskType);

        while(systemOn){
            showStartMenu();
            taskType = ui.getUserInput("\nUser input:");
            handleStartMenuChoice(taskType);
        }
    }

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

    public static void saveIdCounterData(String filePath, String data){
        String fileData = data;

        try{
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(data);
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getCause());
        }
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

    public static void saveData(String filePath, String data){
        String fileData = data;

        try{
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(data);
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    public static void createNewDir(String filePath, String dirName){
        File newDir = new File(filePath + "/" + dirName);
        newDir.mkdir();
    }

    public static void showStartMenu(){
        ui.displayMsg("\n(START-MENU)");

        ui.displayMsg("\nWhat would you like to do?");
        ui.displayMsg("\n- Manage tournaments (Type: 1)");
        ui.displayMsg("- Register a new team to a tournament (Type: 2)");
        ui.displayMsg("- See data (Type: 3)");
        ui.displayMsg("- Make matches (Type: 4)");
        ui.displayMsg("- Close system (Type: 5)");
    }

    public static void handleStartMenuChoice(String taskType){
        if(taskType.equals("1")){
            showTournamentMenu();
        }else if(taskType.equals("2")){
            registerNewTeam();
        }else if(taskType.equals("3")){
            System.out.println("See data");
            showDataMenu();
        }else if(taskType.equals("4")){
            Tournament.displayAllTournaments();
            String matchType = ui.getUserInput("\nWhich tournament would you like to send to matches:");
            showMatchMenu(matchType);
        }else if(taskType.equals("5")){
            System.out.println("\nThe system has been turned off");
            systemOn = false;
        }else{
            System.out.println("Invalid input");
        }
    }

    public static void showMatchMenu(String matchType){
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

    public static void showTournamentMenu(){
        ui.displayMsg("\n(TOURNAMENT-MENU)");

        ui.displayMsg("\nWhat would you like to do?");
        ui.displayMsg("\n- Register new tournament (Type: 1)");
        ui.displayMsg("- Edit a tournament (Type: 2)");
        ui.displayMsg("- Delete a tournament (Type: 3)");
        ui.displayMsg("- Go back to start-menu (Type: 4)");

        String taskType = ui.getUserInput("\nUser input:");
        handleTournamentChoice(taskType);
    }

    public static void handleTournamentChoice(String taskType){
        if(taskType.equals("1")){
            Tournament.registerNewTournament();
        }else if(taskType.equals("2")){
            //todo Make tournament editing available
            ui.displayMsg("Editing option not yet available");
        }else if(taskType.equals("3")){
            Tournament.deleteTournament();
        }else if(taskType.equals("4")){
            return;
        }else{
            ui.displayMsg("Invalid input");
        }
    }

    public static void registerNewTeam(){
        ui.displayMsg("\n(REGISTER NEW TEAM)");

        if(Main.tournaments.size() != 0){
            ui.displayMsg("\nTournaments currently in the system: ");
            Tournament.displayAllTournaments();

            String userInput = ui.getUserInput("\nType the ID of the tournament the team would like to register " +
                    "into \nType -1 to cancel: ");

            if(!userInput.equals("-1")){
                int tournamentId = Integer.parseInt(userInput);

                Tournament tournamentToRegisterInto = Tournament.findTournament(tournamentId);

                if(tournamentToRegisterInto != null){
                    String teamName = ui.getUserInput("\nTeam name:");

                    ui.displayMsg("\nA team must have at least 2 players to get registered");
                    ui.displayMsg("Please enter 2 or more players to complete registration");
                    ui.displayMsg("Type -1 to end");

                    ArrayList<String> playerNames = new ArrayList<String>();
                    boolean stillAdding = true;

                    while(stillAdding){
                        String playerName = ui.getUserInput("\nPlayer name: ");

                        if(!playerName.equals("-1")){
                            playerNames.add(playerName);
                        }else{
                            stillAdding = false;
                        }
                    }

                    if(playerNames.size() == 0){
                        ui.displayMsg("You need at least 2 players to register into the tournament");
                        ui.displayMsg("\nThe team was not registered...");
                    }else{
                        Team team = new Team(teamName);
                        tournamentToRegisterInto.addTeam(team);

                        for(String playerName : playerNames){
                            team.addPlayer(playerName);
                        }

                        saveIdCounterData("src/data/idCounters/idCounter_Team.txt", "ID:" + Team.getIdCounter());
                        saveData("src/data/tournaments/" + tournamentToRegisterInto.getName() + "/teamData.txt",
                                team.toString());
                        ui.displayMsg("\nThe team was successfully registered!");
                    }
                }else{
                    ui.displayMsg("\nNo tournament matching the provided id could be found in the system...");
                }
            }
        }else{
            ui.displayMsg("There are currently no tournaments registered in the system.");
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

    public static void showDataMenu()
    {
        ui.displayMsg(" See data" +
                "\n" + " - Choose which data to view:" +
                "\n" + " - Press 1 for Tournament overview" +
                "\n" + " - Press 2 for Single game overview" +
                "\n" + " - Press 3 for Team overview" +
                "\n" + " - Press 4 for Timetable" +
                "\n" + " - Press 5 to go back to start-menu");
        String taskType = ui.getUserInput("\nUser input:");
        handleDataMenuChoice(taskType);
    }

    public static void handleDataMenuChoice(String taskType)
    {
        if(taskType.equals("1")){ //done
            ui.displayMsg("Displaying all tournaments: ");
            Tournament.displayAllTournaments();

        }else if(taskType.equals("2")){ //in progress
            ui.displayMsg("Displaying matches: ");
            // this needs to draw from matchData.txt

            //Match.displayAllMatches();

        }else if(taskType.equals("3")){ //in progress
            ui.displayMsg("Displaying all teams: ");
            Team.displayAllTeams();
            //make a new menu choice, to show where a seperate team ranks in the standings
            ui.displayMsg("Show team standings in tournaments? y/n");

            if (taskType.equals("y")){ //in progress
                ui.displayMsg("Team rankings: ");

            }
            else if(taskType.equals("n")){ //unfinished
                return;

            }
        }else if(taskType.equals("4")){ //done
            ui.displayMsg("Displaying timeslots: ");
            //make a method which displays timeslots of the upcoming matches


        }else if(taskType.equals("5")) {
            return;

        }else{
            ui.displayMsg("Invalid input");

        }
    }
}