/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatbox.ai;

 import java.util.Scanner;
/**
 *
 * @author vhutali
 */

public class ChatboxAI {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        login login = new login();

        while (!login.isRegistered()) {
            System.out.println("=== Register ===");
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();
            System.out.print("Cell phone number (e.g. +27821234567): ");
            String cell = input.nextLine();

            System.out.println(login.registerUser(username, password, cell));
        }

      
        String loggedInUser = "";

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("=== Login ===");
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();

            loggedIn = login.loginUser(username, password);
            System.out.println(login.returnLoginStatus(loggedIn));

            // PART 3
            if (loggedIn) {
                loggedInUser = username;
            }
        }

        System.out.println("Welcome to QuickChat.");
 System.out.println(Message.loadStoredMessages());
        int maxMessages = askForNumber(input, "How many messages do you want to enter? ");
        int messagesEntered = 0;

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("Choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");   // PART 3

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    if (messagesEntered >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    } else {
                        handleSendMessage(input, messagesEntered);
                        messagesEntered++;
                        if (messagesEntered == maxMessages) {
                            System.out.println("All messages entered. Total messages sent: "
                                    + Message.returnTotalMessages());
                        }
                    }
                }
                case "2" -> System.out.println("Coming Soon.");
                case "3" -> {
                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye!");
                    running = false;
                }
                case "4" -> // PART 3
                    showStoredMenu(input, loggedInUser);
                default -> System.out.println("Please enter 1, 2, 3 or 4.");
            }
        }
    }

    private static void handleSendMessage(Scanner input, int messageNumber) {
        String recipient;
        while (true) {
            System.out.print("Recipient cell number (with international code): ");
            recipient = input.nextLine().trim();
            Message check = new Message(messageNumber, recipient, "check");
            String result = check.checkRecipientCell();
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = input.nextLine();
            String result = Message.checkMessageLength(text);
            System.out.println(result);
            if (result.equals("Message ready to send.")) {
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
        }

        Message message = new Message(messageNumber, recipient, text);

        int option = 0;
        while (option < 1 || option > 3) {
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            try {
                option = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                option = 0;
            }
        }

        System.out.println(message.sentMessage(option));

        if (option == 1) {
            System.out.println(message.getDetails());
        } else if (option == 2) {
            String answer = input.nextLine().trim();
            if (answer.equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }

   
    private static void showStoredMenu(Scanner input, String sender) {
        MessageStore store = Message.getStore();
        boolean inStored = true;

        while (inStored) {
            System.out.println();
            System.out.println("STORED MESSAGES");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display full report");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1" -> System.out.println(store.displayStored(sender));
                case "2" -> System.out.println(store.longestStored());
                case "3" -> {
                    System.out.print("Message ID: ");
                    System.out.println(store.searchById(input.nextLine().trim()));
                }
                case "4" -> {
                    System.out.print("Recipient: ");
                    System.out.println(store.searchByRecipient(input.nextLine().trim()));
                }
                case "5" -> {
                    System.out.print("Message hash: ");
                    System.out.println(store.deleteByHash(input.nextLine().trim().toUpperCase()));
                }
                case "6" -> System.out.println(store.report());
                case "7" -> inStored = false;
                default -> System.out.println("Please choose a number from 1 to 7.");
            }
        }
    }

    private static int askForNumber(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int number = Integer.parseInt(input.nextLine().trim());
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException e) {
                
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }
}

 
