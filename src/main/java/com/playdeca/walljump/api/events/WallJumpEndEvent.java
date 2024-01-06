package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;

// This event is called when a player ends a wall jump
public class WallJumpEndEvent extends WallJumpEvent {

    // The horizontal and vertical power of the wall jump
    private final double horizontalPower;
    // The vertical power is the power of the jump, not the power of the wall jump
    private final double verticalPower;

    // The constructor of the event
    public WallJumpEndEvent(@NotNull WPlayer who, double horizontalPower, double verticalPower) {
        super(who);
        this.horizontalPower = horizontalPower;
        this.verticalPower = verticalPower;
    }

    // Getters and setters
    public double getHorizontalPower() {
        return horizontalPower;
    }
    public double getVerticalPower() {
        return verticalPower;
    }
}
