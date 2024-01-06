package com.playdeca.walljump.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** The type Ground pound event. */
public class GroundPoundEvent extends Event implements Cancellable {

    private final Player player;
    private boolean isCancelled;

    /**
     * Instantiates a new Ground pound event.
     */
    public GroundPoundEvent(Player player, boolean isCancelled) {
        this.player = player;
        this.isCancelled = isCancelled;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets handler list.
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}