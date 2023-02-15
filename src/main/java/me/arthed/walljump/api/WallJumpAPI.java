package me.arthed.walljump.api;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.config.WallJumpConfiguration;
import me.arthed.walljump.player.PlayerManager;
import me.arthed.walljump.player.WPlayer;
import org.bukkit.entity.Player;

// This class is used to get the API
public class WallJumpAPI {

    // The player manager and the config
    private static PlayerManager playerManager;
    private static WallJumpConfiguration config;

    // The constructor
    public WallJumpAPI() {
        WallJump wallJump = WallJump.getInstance();
        playerManager = wallJump.getPlayerManager();
        config = wallJump.getWallJumpConfig();
    }

    // Check if the player needs the permission to wall jump
    public static boolean requiresPermission() {
        return config.getBoolean("needPermission");
    }
    // Check if the player needs to be sneaking to wall jump
    public static boolean isSlidingEnabled() {
        return config.getBoolean("slide");
    }

    // Getters
    public static WPlayer getWPlayer(Player player) {
        return playerManager.getWPlayer(player);
    }
    public static int getMaxJumps() {
        return config.getInt("maxJumps");
    }
    public static float getDefaultHorizontalPower() {
        return (float)config.getDouble("horizontalJumpPower");
    }
    public static float getDefaultVerticalPower() {
        return (float) config.getDouble("verticalJumpPower");
    }
}
