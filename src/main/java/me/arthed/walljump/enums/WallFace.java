package me.arthed.walljump.enums;

import org.bukkit.block.BlockFace;

// The wall faces
public enum WallFace {

    NORTH(0, 0, -1, 1.42f),
    SOUTH(0, 0, 1, 0.42f),
    WEST(-1, 0, 0, 1.42f),
    EAST(1, 0, 0, 0.42f);

    // The offsets and the distance
    public final int xOffset;
    public final int yOffset;
    public final int zOffset;
    public final float distance;

    // The constructor
    WallFace(int xOffset, int yOffset, int zOffset, float distance) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.distance = distance;
    }

    // Get the wall face from the block face
    public static WallFace fromBlockFace(BlockFace blockFace) {
        return switch (blockFace) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> EAST;
        }; //EAST
    }

}
