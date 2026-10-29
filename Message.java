/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbox.ai;

import com.google.gson.Gson;   // PART 3
import java.io.FileReader;     // PART 3
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * JSON CODE ATTRIBUTION (Part 3)
  - https://github.com/google/gson
- https://github.com/google/gson/blob/main/UserGuide.md
 
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

    // PART 3: one shared store that holds the arrays for the whole program
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

    // PART 3: used only when reading a saved message back from the JSON file,
    // so the hash that was saved is kept exactly as it was
    private Message(String messageID, String messageHash, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = 0;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
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

    // PART 3: each choice now also puts the message into the arrays
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                store.addSentMessage(messageText);
                recordInStore();
                return "Message successfully sent.";
            case 2:
                store.addDisregardedMessage(messageText);
                recordInStore();
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                storeMessage();
                store.addStoredMessage(recipient, messageText);
                recordInStore();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // PART 3: puts this message's hash, ID, recipient and text into the arrays
    private void recordInStore() {
        store.recordMessage(messageHash, messageID, recipient, messageText);
    }

    // PART 3: lets other classes use the shared store
    public static MessageStore getStore() {
        return store;
    }

    // PART 3: one saved message exactly as it appears in messages.json
    private static class StoredEntry {
        private String messageID;
        private String messageHash;
        private String recipient;
        private String message;
    }

    // PART 3: reads messages.json INTO the Stored Messages array
    public static String loadStoredMessages() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(JSON_FILE)) {
            StoredEntry[] entries = gson.fromJson(reader, StoredEntry[].class);
            if (entries == null) {
                return "No stored messages on file yet.";
            }
            int loaded = 0;
            for (int i = 0; i < entries.length; i++) {
                StoredEntry e = entries[i];
                Message m = new Message(e.messageID, e.messageHash, e.recipient, e.message);
                storedMessages.add(m);
                store.addStoredMessage(m.recipient, m.messageText);
                store.recordMessage(m.messageHash, m.messageID, m.recipient, m.messageText);
                loaded++;
            }
            return loaded + " stored message(s) loaded from " + JSON_FILE;
        } catch (IOException e) {
            return "No stored messages on file yet.";
        }
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

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public int getMessageNumber() { return messageNumber; }
}