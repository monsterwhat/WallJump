package com.playdeca.walljump.utils;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitUtils {

    // This is the version of the server
   public static final int currentVersionInt;
    static {
        String parsedVersion = getVersion().replace(".", "_");
        Version version;
        try {
            version = Version.valueOf("V" + parsedVersion);
        } catch (IllegalArgumentException e) {
            version = Version.V1_20; // Default to the latest supported version
            Bukkit.getLogger().log(Level.WARNING, "WallJump does not recognize server version: {0}", parsedVersion);
        }
        currentVersionInt = version.versionInt;
    }

    /**
     * Returns the Minecraft version in use by the server.
     * If it cannot be determined, returns the string "1.21" (latest version).
     * @return A string representing the Minecraft version in use.
     */
      
    private static String getVersion() {
        Pattern pattern = Pattern.compile("MC: ([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        if (matcher.find()) {
            return matcher.group(1); // Extract only the major and minor versions
        }
        return "1.21"; // Default fallback
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
            Bukkit.getLogger().info("WallJump is not running on Paper. Some features may not work.");
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
        V1_20(20),  // Minecraft 1.20.x
        V1_21(21);  // Minecraft 1.21.x

        public final int versionInt;

        // Constructor for the Version enum that takes a version integer
        Version(int versionInt) {
            this.versionInt = versionInt;
        }
    }

    public static boolean isVersionBefore(Version version) {
        return currentVersionInt <= version.versionInt;
    }

    public static boolean isVersionAfter(Version version) {
        return currentVersionInt >= version.versionInt;
    }
}
