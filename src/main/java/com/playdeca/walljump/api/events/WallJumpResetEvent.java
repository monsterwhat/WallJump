package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;

public class WallJumpResetEvent extends WallJumpEvent {

    public WallJumpResetEvent(@NotNull WPlayer who) {
        super(who);
    }

}
