package me.arthed.walljump.utils;

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
        if(BukkitUtils.isVersionBefore(BukkitUtils.Version.V1_8))
            verticalPower += 0.1;
        Vector velocity = player.getLocation().getDirection().normalize().multiply(horizontalPower);
        velocity.setY(verticalPower);
        player.setVelocity(velocity);
    }

}
