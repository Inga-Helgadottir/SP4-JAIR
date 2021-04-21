package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class Match {
    Team[] teams;
    Date gameDate;
    int team1Goals;
    int team2Goals;
    String winner;

    public Match(Team[] teams) {//add later: gameDate
        this.teams = teams;
    }

    public void whoWon() {
        if(team1Goals > team2Goals){
            System.out.println(teams[0].getName() + " won");
            this.winner = teams[0].getName();
        }else if(team1Goals < team2Goals){
            System.out.println(teams[1].getName() + " won");
            this.winner = teams[1].getName();
        }else if(team1Goals == team2Goals){
            System.out.println("It was a tie");
            this.winner = "tie";
        }
        updateGoalsMade();
    }

    public void updateGoalsMade(){
        teams[0].setGoalsMade(team1Goals);
        teams[1].setGoalsMade(team2Goals);
        teams[0].setOpposingTeamsGoals(team2Goals);
        teams[1].setOpposingTeamsGoals(team1Goals);
        updateGoalsFile();
    }

    public void updateGoalsFile(){
        try{
            File file = new File("src/data/matches/matchWinnerData.txt");
            FileWriter fr = new FileWriter(file, true);
            String data = "team1, " + teams[0].getName() + ", team1goals, " + team1Goals + ", team2, " + teams[1].getName() + ", team2goals, " + team2Goals + ", Winner, " + this.winner + ",\n";
            fr.write(data);
            fr.close();
            Main.printTournamentData(file);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    public Team[] getTeams() {
        return teams;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public int getTeam1Goals() {
        return team1Goals;
    }

    public void setTeam1Goals(int team1Goals) {
        this.team1Goals = team1Goals;
    }

    public int getTeam2Goals() {
        return team2Goals;
    }

    public void setTeam2Goals(int team2Goals) {
        this.team2Goals = team2Goals;
    }

    public Team printTeamsArray(){
        for (int i = 0; i < teams.length; i++) {
            return teams[i];
        }
        return null;
    }

    @Override
    public String toString() {
        return "Match" +
                ", teams=" + printTeamsArray() +
                ", gameDate=" + gameDate +
                ", team1Goals=" + team1Goals +
                ", team2Goals=" + team2Goals +
                ", winner='" + winner + '\n';
    }
}