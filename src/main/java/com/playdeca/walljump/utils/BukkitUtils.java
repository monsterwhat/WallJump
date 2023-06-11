package com.playdeca.walljump.utils;

import org.bukkit.Bukkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitUtils {

    // This is the version of the server
    public static final int currentVersionInt = Version.valueOf("V" + getVersion().replace(".", "_")).versionInt;

    /**
     * Returns the Minecraft version in use by the server.
     * If it cannot be determined, returns the string "1.19.3" (Feb 2023).
     * @return A string representing the Minecraft version in use.
     */
    private static String getVersion() {
        // Define a regex pattern to match the Minecraft version in the server's version string
        Pattern pattern = Pattern.compile("MC: [0-9]{1,2}\\.[0-9]{1,2}");
        // Use the pattern to find a match in the server's version string
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        // If a match is found, extract the Minecraft version and return it
        if(matcher.find()) {
            return matcher.group().replace("MC: ", "");
        }
        // If no match is found, return a default value of "1.20"
        return "1.20";
    }

    public static boolean isVersionBefore(Version version) {
        return currentVersionInt <= version.versionInt;
    }

    public static boolean isVersionAfter(Version version) {
        return currentVersionInt >= version.versionInt;
    }

    /**
     * Checks whether the server is running Paper.
     *
     * @return true if the server is running Paper, false otherwise.
     */
    public static boolean isPaper() {
        try {
            // Try to load the ParticleBuilder class from Paper
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true; // If it's loaded successfully, return true
        } catch (ClassNotFoundException notPaper) {
            return false; // If the class is not found, return false
        }
    }


    // Define an enum to represent different Minecraft versions
    public enum Version {
        V1_8(8),  // Minecraft 1.8.x
        V1_9(9),  // Minecraft 1.9.x
        V1_10(10),  // Minecraft 1.10.x
        V1_11(11),  // Minecraft 1.11.x
        V1_12(12),  // Minecraft 1.12.x
        V1_13(13),  // Minecraft 1.13.x
        V1_14(14),  // Minecraft 1.14.x
        V1_15(15),  // Minecraft 1.15.x
        V1_16(16),  // Minecraft 1.16.x
        V1_17(17),  // Minecraft 1.17.x
        V1_18(18),  // Minecraft 1.18.x
        V1_19(19),  // Minecraft 1.19.x
        V1_20(20);  // Minecraft 1.20.x

        public final int versionInt;

        // Constructor for the Version enum that takes a version integer
        Version(int versionInt) {
            this.versionInt = versionInt;
        }
    }
}
