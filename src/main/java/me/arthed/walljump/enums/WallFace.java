package me.arthed.walljump.enums;

import org.bukkit.block.BlockFace;

public enum WallFace {

    NORTH(0, 0, -1, 1.42f),
    SOUTH(0, 0, 1, 0.42f),
    WEST(-1, 0, 0, 1.42f),
    EAST(1, 0, 0, 0.42f);


    public final int xOffset;
    public final int yOffset;
    public final int zOffset;

    public final float distance;

    WallFace(int xOffset, int yOffset, int zOffset, float distance) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.distance = distance;
    }

    public static WallFace fromBlockFace(BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            default: //EAST
                return EAST;
        }
    }

}
