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
    private double velocityDown;

    /**
     * Instantiates a new Ground pound event.
     */
    public GroundPoundEvent(Player player, boolean isCancelled, double velocityDown) {
        this.player = player;
        this.isCancelled = isCancelled;
        this.velocityDown = velocityDown;
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

    /**
     * Gets cancelled.
     * @return if cancelled
     */
    public boolean getCancelled() {
        return this.isCancelled;
    }

    /**
     * Gets velocity down.
     * @return the velocity down
     */
    public double getVelocityDown() {
        return this.velocityDown;
    }

    /**
     * Sets velocity down.
     * @param velocityDown the velocity down
     */
    public void setVelocityDown(double velocityDown) {
        this.velocityDown = velocityDown;
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