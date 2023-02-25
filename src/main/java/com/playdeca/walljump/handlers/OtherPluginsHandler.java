package com.playdeca.walljump.handlers;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.player.PlayerManager;
import com.playdeca.walljump.utils.LocationUtils;
import com.playdeca.walljump.api.events.GroundPoundEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// This class handles events from other plugins
public class OtherPluginsHandler implements Listener {

    // The player manager
    private final PlayerManager playerManager;

    // The constructor of the class
    public OtherPluginsHandler() {
        playerManager = WallJump.getInstance().getPlayerManager();
    }

    // This method handles the GroundPoundEvent
    @EventHandler
    public void onGroundPound(GroundPoundEvent event) {
        try {
            // If the player is wall jumping or touching a wall, cancel the event
            if(playerManager.getWPlayer(event.getPlayer()).isWallJumping() || LocationUtils.isTouchingAWall(event.getPlayer()))
                event.setCancelled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
