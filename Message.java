/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbox.ai;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Vhutali */

public class Message {
 

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    private static final String JSON_FILE = "messages.json";
  private static final MessageStore store = new MessageStore();
  
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private static String generateMessageID() {
        Random rand = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + rand.nextInt(10);
        }
        return id;
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        boolean ok = recipient.matches("^\\+\\d{1,3}\\d{1,10}$");
        if (ok) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public static String checkMessageLength(String text) {
        int length = text.length();
        if (length <= 250) {
            return "Message ready to send.";
        } else {
            int over = length - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    private static String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

   public String sentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                sentMessages.add(this);
                store.addSentMessage(messageText);
                recordInStore();
                return "Message successfully sent.";
            }
            case 2 -> {
                store.addDisregardedMessage(messageText);
                recordInStore();
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                storedMessages.add(this);
                storeMessage();
                store.addStoredMessage(recipient, messageText);
                recordInStore();
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid option.";
            }
        }
    }

   
    private void recordInStore() {
        store.recordMessage(messageHash, messageID, recipient, messageText);
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        String result = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            result = result + sentMessages.get(i).getDetails() + "\n";
        }
        return result;
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    public static void storeMessage() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(escape(m.messageID)).append("\",\n");
            json.append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n");
            json.append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n");
            json.append("    \"message\": \"").append(escape(m.messageText)).append("\"\n");
            json.append("  }");
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not save to JSON file: " + e.getMessage());
        }
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n");
    }

    public String getDetails() {
        return "Message ID: " + messageID + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: " + recipient + "\n"
             + "Message: " + messageText;
    }

    public static void clearAll() {
        sentMessages.clear();
        storedMessages.clear();
    }
    public static MessageStore getStore() {
        return store;
    }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
}

