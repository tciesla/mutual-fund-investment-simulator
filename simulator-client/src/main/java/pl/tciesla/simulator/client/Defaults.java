package pl.tciesla.simulator.client;

public class Defaults {

    private static String username = "tciesla";

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Defaults.username = username;
    }
}
