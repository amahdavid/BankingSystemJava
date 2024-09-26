package main.java.com.banksystem;

import main.java.com.banksystem.BankingMenu;
import main.java.com.banksystem.ExceptionHandler;

public class Main {
    public static void main(String[] args) throws ExceptionHandler {
        BankingMenu menu = new BankingMenu();
        menu.displayMenu();
    }
}