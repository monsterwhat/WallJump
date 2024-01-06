package com.playdeca.walljump.listeners;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.player.PlayerManager;
import com.playdeca.walljump.player.WPlayer;
import com.playdeca.walljump.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerToggleSneakListener implements Listener {

    // Get the player manager
    private final PlayerManager playerManager = WallJump.getInstance().getPlayerManager();

    // Handles the PlayerToggleSneakEvent, which is called when a player starts or stops sneaking
    // If the player is not flying, checks if the player is either currently on a wall and has stopped sneaking
    // or is touching a wall, sneaking, and not on the ground, and starts the wall jump process if so
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneak(@NotNull PlayerToggleSneakEvent event) {
        try {
            Player player = event.getPlayer();
            if(!player.isFlying()) {
                WPlayer wplayer = playerManager.getWPlayer(player);
                if(wplayer.isOnWall() && !event.isSneaking())
                    wplayer.onWallJumpEnd();
                else if(LocationUtils.isTouchingAWall(player) && event.isSneaking() && !player.isOnGround())
                    wplayer.onWallJumpStart();
            }
        }catch (Exception e){
            Bukkit.getLogger().warning("An error occurred while handling a player toggle sneak event.");
        }
    }
}
