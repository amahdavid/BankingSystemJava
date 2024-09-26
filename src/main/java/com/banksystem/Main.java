package main.java.com.banksystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ExceptionHandler {
        Scanner scanner = ScannerSingleton.getInstance();
        BankingMenu menu = new BankingMenu(scanner);
        menu.displayMenu();
    }
}