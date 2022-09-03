package me.arthed.walljump.api.events;

import me.arthed.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;


public class WallJumpEndEvent extends WallJumpEvent {

    private double horizontalPower;
    private double verticalPower;

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

    public void setHorizontalPower(double power) {
        this.horizontalPower = power;
    }

    public void setVerticalPower(double power) {
        this.verticalPower = power;
    }
}
