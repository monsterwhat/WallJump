package com.playdeca.walljump.utils;

import com.playdeca.walljump.enums.WallFace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.playdeca.walljump.utils.BukkitUtils.Version;

public class EffectUtils {

    public static void spawnSlidingParticles(Player player, int count, WallFace facing) {
        try {
            // Check if the server version is lower than 1.8, in which case the method can't be used.
            if (BukkitUtils.isVersionBefore(Version.V1_8))
                return;

            Location location = player.getLocation();

            // Spawn a particle effect at the player's location, offset in the direction they're facing.
            player.getWorld().spawnParticle(
                    Particle.CLOUD, // The particle effect to used
                    location.clone().add(facing.xOffset * 0.3, facing.yOffset * 0.3, facing.zOffset * 0.3), // The location to spawn particles
                    count, // The number of particles to spawn
                    0.2f, // The X-axis offset of the particles
                    0.2f, // The Y-axis offset of the particles
                    0.2f // The Z-axis offset of the particles
            );
        }catch (Exception e) {
            Bukkit.getLogger().warning("Failed to spawn sliding particles for player " + player.getName() + "!");
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public static void playWallJumpSound(Player player, WallFace facing, float volume, float pitch) {
        try {
            // Get the world the player is in and play a sound at their current location
            player.getWorld().playSound(
                    player.getLocation(),
                    // Get the block the player is stuck on in the given direction
                    //NmsUtils.getStepSoundForBlock(LocationUtils.getBlockPlayerIsStuckOn(player, facing)),
                    Material.STONE.createBlockData().getSoundGroup().getStepSound(),
                    volume, // Set the volume of the sound
                    pitch // Set the pitch of the sound
            );
        }catch (Exception e){
            Bukkit.getLogger().warning("Failed to play wall jump sound for player " + player.getName() + "!");
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}
