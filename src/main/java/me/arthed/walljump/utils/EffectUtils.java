package me.arthed.walljump.utils;

import me.arthed.walljump.enums.WallFace;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.arthed.walljump.utils.BukkitUtils.Version;

public class EffectUtils {

    public static void spawnSlidingParticles(Player player, int count, WallFace facing) {
        // Check if the server version is lower than 1.8, in which case the method can't be used.
        if (BukkitUtils.isVersionBefore(Version.V1_8))
            return;

        Object data;
        Location location = player.getLocation();
        Block block = location.clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();

        // If the server version is between 1.8 and 1.11, get the block data using its byte value.
        if (BukkitUtils.isVersionBefore(Version.V1_12))
            data = block.getType().getNewData(block.getData());
        else // If the server version is 1.12 or higher, get the block data directly.
            data = block.getBlockData();

        // Spawn a particle effect at the player's location, offset in the direction they're facing.
        player.getWorld().spawnParticle(
                Particle.BLOCK_DUST, // The particle effect to use
                location.clone().add(facing.xOffset*0.3, facing.yOffset*0.3-0.3, facing.zOffset*0.3), // The location to spawn particles
                count, // The number of particles to spawn
                0.2f, // The X-axis offset of the particles
                0.2f, // The Y-axis offset of the particles
                0.2f, // The Z-axis offset of the particles
                data // The data value for the particles
        );
    }

    public static void playWallJumpSound(Player player, WallFace facing, float volume, float pitch) {
        // Get the world the player is in and play a sound at their current location
        player.getWorld().playSound(
                player.getLocation(),
                // Get the block the player is stuck on in the given direction
                NmsUtils.getStepSoundForBlock(LocationUtils.getBlockPlayerIsStuckOn(player, facing)),
                volume, // Set the volume of the sound
                pitch // Set the pitch of the sound
        );
    }


}
