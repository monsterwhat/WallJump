package me.arthed.walljump.handlers;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.player.PlayerManager;
import me.arthed.walljump.utils.LocationUtils;
import me.treyruffy.treysdoublejump.api.GroundPoundEvent;
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

    // This method handles the GroundPoundEvent from TreyRuffy's DoubleJump plugin
    @EventHandler
    public void onTreysDoubleJumpGroundPound(GroundPoundEvent event) {
        // If the player is wall jumping or touching a wall, cancel the event
        if(playerManager.getWPlayer(event.getPlayer()).isWallJumping() || LocationUtils.isTouchingAWall(event.getPlayer()))
            event.setCancelled(true);
    }
}
