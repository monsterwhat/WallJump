package com.playdeca.walljump.player;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.api.events.WallJumpEndEvent;
import com.playdeca.walljump.api.events.WallJumpResetEvent;
import com.playdeca.walljump.api.events.WallJumpStartEvent;
import com.playdeca.walljump.config.WallJumpConfiguration;
import com.playdeca.walljump.enums.WallFace;
import com.playdeca.walljump.handlers.WorldGuardHandler;
import com.playdeca.walljump.utils.BukkitUtils;
import com.playdeca.walljump.utils.EffectUtils;
import com.playdeca.walljump.utils.LocationUtils;
import com.playdeca.walljump.utils.VelocityUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Objects;

public class WPlayer {

    private final Player player;
    private boolean wallJumping;
    private boolean onWall;
    private boolean sliding;
    private WallFace lastFacing;
    private Location lastJumpLocation;
    private int remainingJumps = -1;
    private BukkitTask velocityTask;
    private BukkitTask fallTask;
    private float velocityY;
    private BukkitTask stopWallJumpingTask;
    private final WallJumpConfiguration config;
    private final WorldGuardHandler worldGuard;
    public boolean enabled = true;

    /**
     * Constructor for the WPlayer class, which wraps a Player instance with additional functionality for wall jumping.
     * @param player the Player to be wrapped
     */
    protected WPlayer(Player player) {
            this.player = player;

            // get configuration and WorldGuard handler instances from the WallJump plugin
            config = WallJump.getInstance().getWallJumpConfig();
            worldGuard = WallJump.getInstance().getWorldGuardHandler();
    }

    /**
     * Starts the wall jump.
     * If the wall jump cannot be started, this method returns.
     * Otherwise, it triggers a WallJumpStartEvent and sets up the wall jumping process.
     */
    public void onWallJumpStart() {
        try {

            if (!canWallJump())
                return;

            // trigger WallJumpStartEvent
            WallJumpStartEvent event = new WallJumpStartEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;

            // set up wall jumping process
            onWall = true;
            wallJumping = true;
            lastFacing = LocationUtils.getPlayerFacing(player);
            lastJumpLocation = player.getLocation();
            if (remainingJumps > 0)
                remainingJumps--;

            // Stop some anti-cheat checks that might be caused by wall-jumping
            // AntiCheatUtils.stopPotentialAntiCheatChecks(player);

            // play sound and spawn particles is bugged. TODO: Fix
            EffectUtils.playWallJumpSound(player, lastFacing, 0.3f, 1.2f);
            EffectUtils.spawnSlidingParticles(player, 5, lastFacing);

            // stop the player from falling and moving while on the wall or make them slide down
            velocityY = 0;
            if (BukkitUtils.isVersionBefore(BukkitUtils.Version.V1_9))
                velocityY = 0.04f;
            velocityTask = Bukkit.getScheduler().runTaskTimerAsynchronously(WallJump.getInstance(), () -> {
                player.setVelocity(new Vector(0, velocityY, 0));
                if (velocityY != 0) {
                    EffectUtils.spawnSlidingParticles(player, 2, lastFacing);
                    if (sliding) {
                        if (player.isOnGround() || !Objects.requireNonNull(LocationUtils.getBlockPlayerIsStuckOn(player, lastFacing)).getType().isSolid()) {
                            // make the player slide down the wall and stop wall jumping
                            Bukkit.getScheduler().runTask(WallJump.getInstance(), () -> {
                                player.setFallDistance(0);
                                player.teleport(player.getLocation());
                                onWallJumpEnd(false);
                            });
                        }
                        if (lastJumpLocation.getY() - player.getLocation().getY() >= 1.2) {
                            lastJumpLocation = player.getLocation();
                            Bukkit.getScheduler().runTask(WallJump.getInstance(), () -> {
                                EffectUtils.playWallJumpSound(player, lastFacing, 0.2f, 0.6f);
                            });
                        }
                    }
                }
            }, 0, 1);

            // make the player fall | slide when the time runs out
            if (fallTask != null)
                fallTask.cancel();
            fallTask = Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), () -> {
                if (onWall) {
                    if (config.getBoolean("slide")) {
                        velocityY = (float) -config.getDouble("slidingSpeed");
                        sliding = true;
                    } else {
                        // make the player fall and stop wall jumping
                        Bukkit.getScheduler().runTask(WallJump.getInstance(), (Runnable) this::onWallJumpEnd);
                    }
                }
            }, (long)(config.getDouble("timeOnWall") * 20));

            // cancel the task for resetting wall jumping if the player wall jumps
            if (stopWallJumpingTask != null)
                stopWallJumpingTask.cancel();
        }catch (Exception e){
           Bukkit.getLogger().warning("Failed to start wall jump for player " + player.getName() + "!");
        }
    }

    public void onWallJumpEnd() {
        onWallJumpEnd(true);
    }

    /**
     * Called when the player stops wall jumping.
     * Resets the wall jumping status of the player.
     *
     * @param jump a boolean indicating whether the player is jumping after wall jumping
     */
    public void onWallJumpEnd(boolean jump) {
        try {
            // reset status
            onWall = false;
            sliding = false;

            // allow player to move again
            player.setFallDistance(0);
            velocityTask.cancel();

            // call event
            WallJumpEndEvent event = new WallJumpEndEvent(this, config.getDouble("horizontalJumpPower"), config.getDouble("verticalJumpPower"));
            Bukkit.getPluginManager().callEvent(event);

            // if the player can jump and is not looking down, push them in the direction they are facing
            if (jump && ((velocityY == 0 && player.getLocation().getPitch() < 85) ||
                    (config.getBoolean("canJumpWhileSliding") && player.getLocation().getPitch() < 60))) {
                VelocityUtils.pushPlayerInFront(player, event.getHorizontalPower(), event.getVerticalPower());
            }

            // after 1.5 seconds, if the player hasn't walled jumped again, reset everything
            Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), () -> {
                if (LocationUtils.isOnGround(player)) {
                    reset();
                }
            }, 12);

            // reset wall jumping after 2.4 seconds
            stopWallJumpingTask = Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), this::reset, 24);
        }catch (Exception e){
            Bukkit.getLogger().warning("Failed to end wall jump for player " + player.getName() + "!");
        }
    }

    // Resets the wall jump status of the player and cancels any ongoing tasks
    private void reset() {
        try {
            wallJumping = false; // reset wall jumping status
            lastFacing = null; // clear last facing direction
            lastJumpLocation = null; // clear last jump location
            remainingJumps = config.getInt("maxJumps"); // reset remaining jumps to maximum allowed jumps
            if (remainingJumps == 0) // if maximum allowed jumps is 0, set it to -1 to allow unlimited jumps
                remainingJumps = -1;
            if (stopWallJumpingTask != null) // cancel the stop wall jumping task if it is running
                stopWallJumpingTask.cancel();
            stopWallJumpingTask = null;

            // call WallJumpResetEvent event to notify listeners that wall jump has been reset
            Bukkit.getScheduler().runTask(WallJump.getInstance(), () -> {
                WallJumpResetEvent event = new WallJumpResetEvent(this);
                Bukkit.getPluginManager().callEvent(event);
            });
        }catch (Exception e){
           Bukkit.getLogger().warning("Failed to reset wall jump for player " + player.getName() + "!");
        }
    }

    /**
     * Checks if the player is able to wall jump based on various conditions
     * @return true if the player is able to wall jump, false otherwise
     */
    public boolean canWallJump() {
        try {
            WallFace facing = LocationUtils.getPlayerFacing(player);
            // Adjusts lastJumpLocation so that height does not matter when calculating distance
            if(lastJumpLocation != null)
                lastJumpLocation.setY(player.getLocation().getY());

            // Check various conditions to see if the player can wall jump
            if(!enabled || // Plugin is not enabled
                    onWall || // Player is already stuck to a wall
                    remainingJumps == 0 || // Player reached jump limit
                    (lastFacing != null && lastFacing.equals(facing)) || // Player is facing the same direction as the last jump
                    (lastJumpLocation != null && player.getLocation().distance(lastJumpLocation) <= config.getDouble("minimumDistance")) || // Player is too close to the last jump location
                    player.getVelocity().getY() < config.getDouble("maximumVelocity") || // Player is falling too fast
                    (config.getBoolean("needPermission") && !player.hasPermission("walljump.use")) || // Player does not have the permission to wall-jump
                    (worldGuard != null && !worldGuard.canWallJump(player))) { // Wall-jumping is not allowed in the region the player is in
                return false;
            }

            // Check if the block the player is wall jumping on is blacklisted
            boolean onBlacklistedBlock = config.getMaterialList("blacklistedBlocks").contains(
                    player.getLocation().clone().add(facing.xOffset, facing.yOffset, facing.zOffset)
                            .getBlock()
                            .getType());
            boolean reverseBlockBlacklist = config.getBoolean("reversedBlockBlacklist");
            if((!reverseBlockBlacklist && onBlacklistedBlock) ||
                    (reverseBlockBlacklist && !onBlacklistedBlock)) {
                return false;
            }

            // Check if the world the player is in is blacklisted
            boolean inBlacklistedWorld = config.getWorldList("blacklistedWorlds").contains(player.getWorld());
            boolean reverseWorldBlacklist = config.getBoolean("reversedWorldBlacklist");
            return (reverseWorldBlacklist || !inBlacklistedWorld) && (!reverseWorldBlacklist || inBlacklistedWorld);
        }catch (Exception e){
            Bukkit.getLogger().warning("Failed to check if player " + player.getName() + " can wall jump!");
            return false;
        }
    }
    public boolean isOnWall() {
        return onWall;
    }

    public boolean isWallJumping() {
        return wallJumping;
    }

    public boolean isSliding() {
        return sliding;
    }

    public Player getPlayer() {
        return player;
    }

}
