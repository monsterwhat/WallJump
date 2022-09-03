package me.arthed.walljump.utils;

import org.bukkit.Bukkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BukkitUtils {

    public static final int currentVersionInt = Version.valueOf("V" + getVersion().replace(".", "_")).versionInt;

    private static String getVersion() {
        Pattern pattern = Pattern.compile("MC: [0-9]{1,2}\\.[0-9]{1,2}");
        Matcher matcher = pattern.matcher(Bukkit.getVersion());
        if(matcher.find()) {
            return matcher.group().replace("MC: ", "");
        }
        return "1.19";
    }

    public static boolean isVersionBefore(Version version) {
        return currentVersionInt <= version.versionInt;
    }

    public static boolean isVersionAfter(Version version) {
        return currentVersionInt >= version.versionInt;
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException notPaper) {
            return false;
        }
    }

    public static boolean isPluginInstalled(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null;
    }

    public enum Version {

        V1_8(8),
        V1_9(9),
        V1_10(10),
        V1_11(11),
        V1_12(12),
        V1_13(13),
        V1_14(14),
        V1_15(15),
        V1_16(16),
        V1_17(17),
        V1_18(18),
        V1_19(19);

        public final int versionInt;

        Version(int versionInt) {
            this.versionInt = versionInt;
        }

    }

}
