package com.company;
import java.util.Scanner;

public class UI {
   Scanner scanner = new Scanner(System.in);

   public void displayMsg(String msg){
      System.out.println(msg);
   }

   public String getUserInput(String msg){
      System.out.println(msg);

      String input = scanner.nextLine();

      return input;
   }

   public int getTeamInput(){

      int inp = scanner.nextInt();

      return inp;
   }
}
