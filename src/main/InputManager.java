package main;

import java.util.Scanner;

public class InputManager {

    /**
     * Scanner used for reading the user inputs
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Fetches a String input from the user
     *
     * @param message: The message to be sent to the user
     * @return the user input
     */
    public static String getStringInput(String message) {
        System.out.println(message);
        return scanner.next();
    }

    /**
     * Fetches an Integer input from the user
     *
     * @param message: The message to send to the user
     * @param min:     The minimum value to be accepted
     * @param max:     The maximum value to be accepted
     * @return the user input
     */
    public static Integer getIntInput(String message, int min, int max) {
        System.out.println(message);
        String s = scanner.next();
        try {
            int i = Integer.parseInt(s);
            if (i < min || i > max) return null;
            else return i;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetches a String input from the user in a password-secure manner
     *
     * @param message: The message to send to the user
     * @return the user input
     */
    public static String getPasswordInput(String message) {
        System.out.println(message);
        char[] pwd = System.console().readPassword();
        StringBuilder sb = new StringBuilder();
        for (char c : pwd) {
            sb.append(c);
        }
        return sb.toString();
    }
}
