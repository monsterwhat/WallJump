package me.arthed.walljump.api.events;

import me.arthed.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;

// This event is called when a player ends a wall jump
public class WallJumpEndEvent extends WallJumpEvent {

    // The horizontal and vertical power of the wall jump
    private double horizontalPower;
    // The vertical power is the power of the jump, not the power of the wall jump
    private double verticalPower;

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
    public void setHorizontalPower(double power) {this.horizontalPower = power;}
    public void setVerticalPower(double power) {this.verticalPower = power;}
}
