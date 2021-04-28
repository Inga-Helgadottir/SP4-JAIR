package com.company;

import java.util.ArrayList;

public interface IO {
   public void setTournamentIdCounterData(String path);
   public void setTeamIdCounterData(String path);

   public void readTournamentData(String path);
   public void readGameDateData(String path, Tournament tournament);
   public void readTeamData(String path, Tournament tournament);
   public void readPlayerData(String path, Team team);

   public void saveTournamentData(String path, Tournament tournament);
   public void saveGameDateData(String path, Tournament tournament);
   public void saveTeamData(String path, Team team, Tournament tournament);
   public void savePlayerData(String path, Team team);

   public void deleteTournamentData(String path, Tournament tournament);

   public void updateGoals(Team[] teams, int team1Goals, int team2Goals, String winner);

   public void saveMatches(Match data);
   public void showMatchMenu(String matchType);
}