package io;

import constants.Strings;

import java.util.Scanner;

/**
 * Manages the user inputs and outputs
 */
public class IOManager {

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
        print(message + " ");
        StringBuilder sb = new StringBuilder(scanner.next());
        String nextLine = scanner.nextLine();
        if (!nextLine.isEmpty()) sb.append(nextLine);
        return sb.toString();
    }

    /**
     * Private constructor of InputManager
     */
    private IOManager() {
    }

    /**
     * Prints a message in console using the Alset format.
     *
     * @param message: Message to print
     */
    public static void print(String message) {
        message = message.replace("\n", "\n[Alset] ");
        System.out.print(Strings.ALSET_PREFIX + message);
    }

    /**
     * Prints an empty message in console using the Alset format.
     */
    public static void print() {
        print("");
    }

    /**
     * Prints a message in console using the Alset format, and creates a new line.
     *
     * @param message: Message to print out
     */
    public static void println(String message) {
        message = message.replace("\n", "\n[Alset] ");
        System.out.println(Strings.ALSET_PREFIX + message);
    }

    /**
     * Prints an empty message in console using the Alset format
     */
    public static void println() {
        println("");
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
        IOManager.print(message + " ");
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
     * Fetches an Integer input from the user
     *
     * @param message: The message to send to the user
     * @param min:     The minimum value to be accepted
     * @param max:     The maximum value to be accepted
     * @return the user input
     */
    public static Double getDoubleInput(String message, double min, double max) {
        IOManager.print(message + " ");
        String s = scanner.next();
        try {
            double i = Double.parseDouble(s);
            if (i < min || i > max) return null;
            else return i;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Fetches a Long input from the user
     *
     * @param message: The message to send to the user
     * @param min:     The minimum value to be accepted
     * @param max:     The maximum value to be accepted
     * @return the user input
     */
    public static Long getLongInput(String message, long min, long max) {
        IOManager.print(message + " ");
        String s = scanner.next();
        try {
            long i = Long.parseLong(s);
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
        print(message + " ");
        char[] pwd = System.console().readPassword();
        StringBuilder sb = new StringBuilder();
        for (char c : pwd) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Clears the console, prints the Alset logo, and prints a given message.
     *
     * @param message: Message to print
     */
    public static void clear(String message) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        IOManager.println(Strings.ALSET_LOGO);
        IOManager.println();
        IOManager.println(message);
        IOManager.println();
    }
}
