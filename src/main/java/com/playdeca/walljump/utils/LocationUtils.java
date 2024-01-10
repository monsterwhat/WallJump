package com.playdeca.walljump.utils;

import com.playdeca.walljump.enums.WallFace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LocationUtils {

    /**
     * Determines if a player is touching a solid block in front of them
     * @param player The player to check
     * @return true if the player is touching a solid block in front of them, false otherwise
     */
    public static boolean isTouchingAWall(Player player) {
        try {
            // Get the direction the player is facing
            WallFace facing = getPlayerFacing(player);
            // Get the location of the player
            Location location = player.getLocation();
            // Get the block in front of the player
            Block block = location.clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();
            // Get the maximum distance the player can be from the block to be considered "touching" it
            float distanceLimit = facing.distance;
            // Check if the block is solid
            if(block.getType().isSolid()) {
                // If the player is facing east or west, check the distance in the X direction
                if(facing.equals(WallFace.EAST) || facing.equals(WallFace.WEST))
                    return Math.abs(location.getX() - block.getX()) < distanceLimit;
                    // If the player is facing north or south, check the distance in the Z direction
                else
                    return  Math.abs(location.getZ() - block.getZ()) < distanceLimit;
            }
            // If the block is not solid, the player is not touching a wall
            return false;
        }catch (Exception e) {
            Bukkit.getLogger().severe("Error checking if player is touching a wall");
            return false;
        }
    }

    /**
     * Checks if the player is currently standing on the ground
     * @param player the player to check
     * @return true if the player is on the ground, false otherwise
     */
    public static boolean isOnGround(Player player) {
        try {
            // Get the player's location and clone it to avoid modifying the original
            Location location = player.getLocation().clone();
            // Subtract 0.2 from the Y coordinate to move the location down slightly
            // This is done because the player's bounding box is larger than a single block,
            // and we want to check the block below the player's feet, not the one the player is inside.
            location.subtract(0, 0.2, 0);
            // Get the block at the modified location and check if it's solid
            Block block = location.getBlock();
            return block.getType().isSolid();
        }catch (Exception e) {
            Bukkit.getLogger().severe("Error checking if player is on the ground");
            return false;
        }
    }

    /**
     * Gets the block that the player is stuck on in the specified direction.
     * @param player The player whose position is being checked.
     * @param facing The direction in which the player is stuck.
     * @return The block the player is stuck on.
     */
    public static Block getBlockPlayerIsStuckOn(Player player, WallFace facing) {
        try {
            // Get the location of the player
            Location location = player.getLocation();
            // Get the block the player is stuck on by adding the offsets for the specified direction to the player's location
            return location.clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();
        }catch (Exception e) {
            Bukkit.getLogger().severe("Error getting block player is stuck on");
            return null;
        }
    }

    public static WallFace getPlayerFacing(Player player) {
        try {
            // Check if server version is after 1.13, which has a different method to get player's facing direction
            if(BukkitUtils.isVersionAfter(BukkitUtils.Version.V1_13))
                // If the server version is after 1.13, get the player's facing direction using the new method
                return WallFace.fromBlockFace(player.getFacing());
            else {
                // If the server version is before 1.13, calculate the player's facing direction using their rotation
                double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
                // Ensure rotation value is between 0 and 360 degrees
                if (rotation < 0.0D) {
                    rotation += 360.0D;
                }
                // Determine the player's facing direction based on their rotation
                if ((0.0D <= rotation) && (rotation < 45.0D))
                    return WallFace.WEST;
                if ((45.0D <= rotation) && (rotation < 135.0D))
                    return WallFace.NORTH;
                if ((135.0D <= rotation) && (rotation < 225.0D))
                    return WallFace.EAST;
                if ((225.0D <= rotation) && (rotation < 315.0D))
                    return WallFace.SOUTH;
                if ((315.0D <= rotation) && (rotation < 360.0D)) {
                    return WallFace.WEST;
                }
                // If no valid facing direction is found, return the default direction (NORTH)
                return WallFace.NORTH;
            }
        }catch (Exception e) {
            Bukkit.getLogger().severe("Error getting player facing direction");
            return WallFace.NORTH;
        }
    }
}
