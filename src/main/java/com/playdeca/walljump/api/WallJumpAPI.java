package com.playdeca.walljump.api;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.config.WallJumpConfiguration;
import com.playdeca.walljump.player.PlayerManager;
import com.playdeca.walljump.player.WPlayer;
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

    // Getters
    public static WPlayer getWPlayer(Player player) {
        return playerManager.getWPlayer(player);
    }
    public static int getMaxJumps() {
        return config.getInt("maxJumps");
    }
    public static boolean requiresPermission() {
        return config.getBoolean("needPermission");
    }
    public static float getDefaultHorizontalPower() {
        return (float)config.getDouble("horizontalJumpPower");
    }
    public static float getDefaultVerticalPower() {
        return (float) config.getDouble("verticalJumpPower");
    }
    public static boolean isSlidingEnabled() {
        return config.getBoolean("slide");
    }
}