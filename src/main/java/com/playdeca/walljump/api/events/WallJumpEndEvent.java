package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;

public class WallJumpEndEvent extends WallJumpEvent {
    private final double horizontalPower;
    // The vertical power is the power of the jump, not the power of the wall jump
    private final double verticalPower;

    public WallJumpEndEvent(@NotNull WPlayer who, double horizontalPower, double verticalPower) {
        super(who);
        this.horizontalPower = horizontalPower;
        this.verticalPower = verticalPower;
    }

    public double getHorizontalPower() {
        return horizontalPower;
    }
    public double getVerticalPower() {
        return verticalPower;
    }
}
