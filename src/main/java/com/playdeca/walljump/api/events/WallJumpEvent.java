package com.playdeca.walljump.api.events;

import com.playdeca.walljump.player.WPlayer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

// This event is called when a player starts a wall jump
public abstract class WallJumpEvent extends PlayerEvent implements Cancellable {

    // The handler list
    private static final HandlerList handlers = new HandlerList();
    // The player
    private final WPlayer wplayer;

    // The cancel variable
    private boolean cancel;

    // The constructor of the event
    public WallJumpEvent(@NotNull WPlayer who) {
        super(who.getPlayer());
        this.wplayer = who;
    }

    // Cancellable methods
    @Override
    public boolean isCancelled() {
        return cancel;
    }
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    // Getters
    @NotNull
    public WPlayer getWPlayer() {return wplayer;}
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
