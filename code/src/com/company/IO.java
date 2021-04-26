package com.company;

import java.util.ArrayList;

public interface IO {
   public void readTournamentData(String path);
   public void readGameDateData(String path, Tournament tournament);
   public void readTeamData(String path, Tournament tournament);

   public void saveTournamentData(String path, Tournament tournament);
   public void saveGameDateData(String path, Tournament tournament, String date);
   public void saveTeamData(String path, Team team, Tournament tournament);

   public void deleteTournamentData(String path, Tournament tournament);

   public void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner);

   public void saveMatches(Match data);
}