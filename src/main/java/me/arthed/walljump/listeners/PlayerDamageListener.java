package me.arthed.walljump.listeners;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.player.PlayerManager;
import me.arthed.walljump.player.WPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final PlayerManager playerManager = WallJump.getInstance().getPlayerManager();

    /**
     * Cancels fall damage if the player is currently wall sliding.
     * @param event The EntityDamageEvent representing the damage event.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        // Check if the damage is caused by falling and the entity is a player
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && event.getEntityType().equals(EntityType.PLAYER)) {
            WPlayer wplayer = playerManager.getWPlayer((Player)event.getEntity()); // Get the WPlayer instance for the player
            if (wplayer != null && wplayer.isSliding()) { // Check if the player is wall sliding
                event.setCancelled(true); // Cancel the fall damage
                wplayer.onWallJumpEnd(false); // End the wall slide
            }
        }
    }


}
