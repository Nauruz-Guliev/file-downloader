package ru.kpfu.itis.gnt.entitites;

public class EntityColors {
    private static final String ANSI_LIGHT_RED = "\u001B[31m";
    private static final String ANSI_LIGHT_BLUE = "\u001B[36m";
    private static final String ANSI_RESET = "\u001b[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    public static String makeGreen(String str) {
        return ANSI_GREEN + str + ANSI_RESET;
    }
    public static String makeRed(String str){
        return ANSI_LIGHT_RED + str + ANSI_RESET;
    }
    public static String makeBlue(String str) {
        return ANSI_LIGHT_BLUE + str + ANSI_RESET;
    }
    public static String makeYellow(String str){
        return ANSI_YELLOW + str + ANSI_RESET;
    }
    public static String makePurple(String str){
        return ANSI_PURPLE + str + ANSI_RESET;
    }
}
