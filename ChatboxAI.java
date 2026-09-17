/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatbox.ai;

 import java.util.Scanner;
/**
 *
 * @author Student
 */
public class ChatboxAI {

    

    public void main(String[] args) {
        Scanner input = new Scanner(System.in);

        login log  = new login();
        System.out.println("Enter a username: ");
        String username = input.nextLine();
        boolean usernameOk = log.checkUsername(username);

        System.out.println("Enter a password: ");
        String password = input.nextLine();
        boolean passwordOk = log.checkPassword(password);

        System.out.println("Enter a cell phone number: ");
        String phoneNumber = input.nextLine();
        boolean phoneOk = log.checkPhoneNumber(phoneNumber);

        if (usernameOk && passwordOk && phoneOk) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Please fix the errors above and try again.");
        }
    }

}
 
