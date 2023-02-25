package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;

// This event is called when a player resets a wall jump
public class WallJumpResetEvent extends WallJumpEvent {

    // The constructor of the event
    public WallJumpResetEvent(@NotNull WPlayer who) {
        super(who);
    }

}
