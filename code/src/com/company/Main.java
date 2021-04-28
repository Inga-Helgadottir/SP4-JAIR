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
    static boolean hasChosenDataSrc = false;
    static Tournament t = new Tournament("to", "sd", "sfd", "11-11-21 13:30");


    public static void main(String[] args) {

        while(hasChosenDataSrc == false){
            ui.displayMsg("(CHOOSE HOW YOU WOULD LIKE TO RUN THE SYSTEM)\n");
            ui.displayMsg("- Through database (Type: 1)");
            ui.displayMsg("- Locally (Type: 2)\n");
            String userInput = ui.getUserInput("User input:");

            if(userInput.equals("1") || userInput.equals("2")){
                Controller.setDataSrc(userInput);
                ui.displayMsg("");
                hasChosenDataSrc = true;
            }else{
                ui.displayMsg("Invalid input...\n");
                //todo do remove test code below
            }
        }

        // Load all system data
        Controller.loadData();

        ui.displayMsg("\n~ Tournament Manager ~");

        showStartMenu();
        String taskType = ui.getUserInput("\nUser input:");
        handleStartMenuChoice(taskType);

        while(systemOn){
            showStartMenu();
            taskType = ui.getUserInput("\nUser input:");
            handleStartMenuChoice(taskType);
        }
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
            Team.registerNewTeam();
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
        Controller.showMatchMenu(matchType);
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