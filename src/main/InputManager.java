package main;

import constants.Constants;

import java.io.Console;
import java.util.Scanner;

public class InputManager {

    /**
     * Scanner used for reading the user inputs
     */
    private static final Scanner scanner = new Scanner(System.in);

    public static String getStringInput(String message) {
        System.out.println(message);
        return scanner.next();
    }

    public static Integer getIntInput(String message, int min, int max) {
        System.out.println(message);
        try {
            var i = scanner.nextInt();
            if (i < min || i > max) return null;
            else return i;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getPasswordInput(String message) {
        System.out.println(message);
        char[] pwd = System.console().readPassword();
        var sb = new StringBuilder();
        for (var c : pwd) {
            sb.append(c);
        }
        return sb.toString();
    }
}
