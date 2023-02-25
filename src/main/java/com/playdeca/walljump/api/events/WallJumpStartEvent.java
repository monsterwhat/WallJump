package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.Cancellable;

// This event is called when a player starts a wall jump
public class WallJumpStartEvent extends WallJumpEvent implements Cancellable {

    // The handler list
    public WallJumpStartEvent(@NotNull WPlayer who) {
        super(who);
    }
}
