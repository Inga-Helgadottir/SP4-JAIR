package com.company;

import java.util.ArrayList;

public interface IO {
   public void readTournamentData(String path);
   public void readGameDateData(String path, Tournament tournament);
   public void readTeamData(String path, Tournament tournament);

   public void saveTournamentData(String path, Tournament tournament);
   public void saveGameDateData(String path, Tournament tournament, String date);
}