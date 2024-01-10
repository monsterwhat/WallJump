package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.Cancellable;

public class WallJumpStartEvent extends WallJumpEvent implements Cancellable {

    public WallJumpStartEvent(@NotNull WPlayer who) {
        super(who);
    }
}
