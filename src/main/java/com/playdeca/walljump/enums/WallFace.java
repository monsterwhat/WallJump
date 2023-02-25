package com.playdeca.walljump.enums;

import org.bukkit.block.BlockFace;

// The wall faces
public enum WallFace {

    // Define the different wall faces
    NORTH(0, 0, -1, 1.42f), // offset of north-facing wall, and distance from player
    SOUTH(0, 0, 1, 0.42f), // offset of south-facing wall, and distance from player
    WEST(-1, 0, 0, 1.42f), // offset of west-facing wall, and distance from player
    EAST(1, 0, 0, 0.42f);  // offset of east-facing wall, and distance from player

    // The offsets and the distance
    public final int xOffset; // x-axis offset of the wall
    public final int yOffset; // y-axis offset of the wall
    public final int zOffset; // z-axis offset of the wall
    public final float distance; // distance from the wall

    // The constructor
    WallFace(int xOffset, int yOffset, int zOffset, float distance) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.distance = distance;
    }

    // Get the wall face from the block face
    public static WallFace fromBlockFace(BlockFace blockFace) {
        // Use a switch statement to return the correct WallFace based on the BlockFace input
        return switch (blockFace) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> EAST;
        }; //EAST
    }
}
