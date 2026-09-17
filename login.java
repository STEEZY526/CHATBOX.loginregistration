/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbox.ai;

/**
 *
 * @author Student
 */
class login {
    public String name;
    public String cell_num;
    public String username;
    public String password;

    
            
    public boolean checkUsername(String username) {
        boolean hasUnderscore = username.contains("_");
        boolean lengthOk = username.length() <= 5;

        boolean hasUpperUsername = false;
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperUsername = true;
            }
        }

        if (hasUnderscore && lengthOk && hasUpperUsername) {
            System.out.println("Username successfully captured.");
            return true;
        } else {
            System.out.println("Username is not correctly formatted; please ensure your username contains an underscore, a capital letter, and is no more than five characters in length.");
            return false;
        }
    }

    public boolean checkPassword(String password) {
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        boolean lengthOk = password.length() >= 8;

        if (lengthOk && hasUpper && hasDigit && hasSpecial) {
            System.out.println("Password successfully captured.");
            return true;
        } else {
            System.out.println("Password is not correctly formatted; please ensure the password contains at least eight characters, a capital letter, a number, and a special character.");
            return false;
        }
    }

    public  boolean checkPhoneNumber(String phoneNumber) {
        boolean phoneOk = phoneNumber.matches("^\\+27\\d{9}$");

        if (phoneOk) {
            System.out.println("Cell phone number added.");
            return true;
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            return false;
        }
    }


}
