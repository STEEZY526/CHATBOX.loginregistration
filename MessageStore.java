/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbox.ai;

/**
 *
 * @author Student
 */
public class MessageStore {

    public static final int MAX_MESSAGES = 50;

    private String[] sentMessages = new String[MAX_MESSAGES];
    private int sentCount = 0;

    private String[] disregardedMessages = new String[MAX_MESSAGES];
    private int disregardedCount = 0;

    private String[] storedMessages = new String[MAX_MESSAGES];
    private String[] storedRecipients = new String[MAX_MESSAGES];
    private int storedCount = 0;

    private String[] messageHashes = new String[MAX_MESSAGES];
    private String[] messageIds = new String[MAX_MESSAGES];
    private String[] allRecipients = new String[MAX_MESSAGES];
    private String[] allMessages = new String[MAX_MESSAGES];
    private int hashIdCount = 0;

    
    public boolean addSentMessage(String text) {
        if (sentCount < MAX_MESSAGES) {
            sentMessages[sentCount] = text;
            sentCount++;
            return true;
        }
        return false;
    }

    public boolean addDisregardedMessage(String text) {
        if (disregardedCount < MAX_MESSAGES) {
            disregardedMessages[disregardedCount] = text;
            disregardedCount++;
            return true;
        }
        return false;
    }

    public boolean addStoredMessage(String recipient, String text) {
        if (storedCount < MAX_MESSAGES) {
            storedRecipients[storedCount] = recipient;
            storedMessages[storedCount] = text;
            storedCount++;
            return true;
        }
        return false;
    }


    public boolean recordMessage(String hash, String id, String recipient, String text) {
        if (hashIdCount < MAX_MESSAGES) {
            messageHashes[hashIdCount] = hash;
            messageIds[hashIdCount] = id;
            allRecipients[hashIdCount] = recipient;
            allMessages[hashIdCount] = text;
            hashIdCount++;
            return true;
        }
        return false;
    }

    
    public int getSentCount() {
        return sentCount;
    }
        // a) Sender and recipient of all stored messages
    public String displayStored(String sender) {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        String result = "";
        for (int i = 0; i < storedCount; i++) {
            result = result + "Sender: " + sender
                    + " | Recipient: " + storedRecipients[i] + "\n";
        }
        return result;
    }

    // b) The longest stored message
    public String longestStored() {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        String longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest;
    }

    // c) Search by message ID: shows the recipient and the message
    public String searchById(String id) {
        for (int i = 0; i < hashIdCount; i++) {
            if (messageIds[i].equals(id)) {
                return "Recipient: " + allRecipients[i]
                        + " | Message: " + allMessages[i];
            }
        }
        return "Message ID " + id + " not found.";
    }

    // d) All messages sent or stored for one recipient
    public String searchByRecipient(String recipient) {
        String result = "";
        for (int i = 0; i < hashIdCount; i++) {
            if (allRecipients[i].equals(recipient)) {
                result = result + allMessages[i] + "\n";
            }
        }
        if (result.equals("")) {
            return "No messages found for " + recipient + ".";
        }
        return result;
    }

    // e) Delete a message using its hash
    public String deleteByHash(String hash) {
        int index = -1;
        for (int i = 0; i < hashIdCount; i++) {
            if (messageHashes[i].equals(hash)) {
                index = i;
            }
        }
        if (index == -1) {
            return "Message hash " + hash + " not found.";
        }

        String text = allMessages[index];

        // Remove it from every parallel array so they stay in sync
        removeAt(messageHashes, index, hashIdCount);
        removeAt(messageIds, index, hashIdCount);
        removeAt(allRecipients, index, hashIdCount);
        removeAt(allMessages, index, hashIdCount);
        hashIdCount--;

        // If it was a stored message, remove it from the stored arrays too
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].equals(text)) {
                removeAt(storedMessages, i, storedCount);
                removeAt(storedRecipients, i, storedCount);
                storedCount--;
                break;
            }
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // f) Full report of every message
    public String report() {
        if (hashIdCount == 0) {
            return "No messages to report.";
        }
        String result = "MESSAGE REPORT\n";
        for (int i = 0; i < hashIdCount; i++) {
            result = result + "----------\n"
                    + "Message Hash: " + messageHashes[i] + "\n"
                    + "Message ID: " + messageIds[i] + "\n"
                    + "Recipient: " + allRecipients[i] + "\n"
                    + "Message: " + allMessages[i] + "\n";
        }
        return result;
    }

    // Helper: removes one slot by shifting everything after it one place left
    private void removeAt(String[] array, int index, int count) {
        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        array[count - 1] = null;
    }
}

