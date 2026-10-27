/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatbox.ai;

/**
 *
 * @author Student
 */

public class login {

    private String registeredUsername;
    private String registeredPassword;
    private boolean registered = false;

    public boolean checkUsername(String username) {
        boolean hasUnderscore = username.contains("_");
        boolean lengthOk = username.length() <= 5;

        boolean hasUpper = false;
        for (int i = 0; i < username.length(); i++) {
            if (Character.isUpperCase(username.charAt(i))) {
                hasUpper = true;
            }
        }
        return hasUnderscore && lengthOk && hasUpper;
    }

    public boolean checkPasswordComplexity(String password) {
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
        return password.length() >= 8 && hasUpper && hasDigit && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser(String username, String password, String cellNumber) {
        String result = "";
        boolean usernameOk = checkUsername(username);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellOk = checkCellPhoneNumber(cellNumber);

        if (usernameOk) {
            result = result + "Username successfully captured.\n";
        } else {
            result = result + "Username is not correctly formatted; please ensure your username contains an underscore, a capital letter, and is no more than five characters in length.\n";
        }

        if (passwordOk) {
            result = result + "Password successfully captured.\n";
        } else {
            result = result + "Password is not correctly formatted; please ensure the password contains at least eight characters, a capital letter, a number, and a special character.\n";
        }

        if (cellOk) {
            result = result + "Cell phone number successfully added.\n";
        } else {
            result = result + "Cell phone number incorrectly formatted or does not contain international code.\n";
        }

        if (usernameOk && passwordOk && cellOk) {
            registeredUsername = username;
            registeredPassword = password;
            registered = true;
            result = result + "Registration successful!";
        } else {
            result = result + "Registration failed. Please try again.";
        }
        return result;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean loginUser(String username, String password) {
        return registered
                && registeredUsername.equals(username)
                && registeredPassword.equals(password);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Login successful.";
        } else {
            return "Login failed: username or password is incorrect.";
        }
    }
}
