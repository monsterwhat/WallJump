package com.playdeca.walljump.utils;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

// This class is used to push a player in the direction he is looking
public class VelocityUtils {

    // Push the player in front of him
    // horizontalPower is the power of the horizontal push
    // verticalPower is the power of the vertical push
    // The vertical push is added to the player's current vertical velocity
    // This method is used to make the player jump
    public static void pushPlayerInFront(Player player, double horizontalPower, double verticalPower) {
        try {
            // Check if the server version is lower than 1.8, in which case the method can't be used.
            if(BukkitUtils.isVersionBefore(BukkitUtils.Version.V1_8)) {
                verticalPower += 0.1;
            }
            // Get the player's direction and multiply it by the horizontal power
            Vector velocity = player.getLocation().getDirection().normalize().multiply(horizontalPower);
            velocity.setY(verticalPower);
            player.setVelocity(velocity);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
