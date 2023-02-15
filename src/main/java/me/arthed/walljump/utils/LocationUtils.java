package me.arthed.walljump.utils;

import me.arthed.walljump.enums.WallFace;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static boolean isTouchingAWall(Player player) {
        WallFace facing = getPlayerFacing(player);

        Location location = player.getLocation();
        Block block = location.clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();
        float distanceLimit = facing.distance;

        if(block.getType().isSolid()) {
            if(facing.equals(WallFace.EAST) || facing.equals(WallFace.WEST))
                return Math.abs(location.getX() - block.getX()) < distanceLimit;
            else
                return  Math.abs(location.getZ() - block.getZ()) < distanceLimit;
        }
        return false;
    }

    public static boolean isOnGround(Player player) {
        return player.getLocation().clone().subtract(0, 0.2, 0).getBlock().getType().isSolid();
    }

    public static Block getBlockPlayerIsStuckOn(Player player, WallFace facing) {
        return player.getLocation().clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();
    }

    public static WallFace getPlayerFacing(Player player) {
        if(BukkitUtils.isVersionAfter(BukkitUtils.Version.V1_13))
            return WallFace.fromBlockFace(player.getFacing());
        else {
            double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;

            if (rotation < 0.0D) {
                rotation += 360.0D;
            }
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
            return WallFace.NORTH;
        }
    }
}
