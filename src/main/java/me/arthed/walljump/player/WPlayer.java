package me.arthed.walljump.player;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.api.events.WallJumpEndEvent;
import me.arthed.walljump.api.events.WallJumpResetEvent;
import me.arthed.walljump.api.events.WallJumpStartEvent;
import me.arthed.walljump.config.WallJumpConfiguration;
import me.arthed.walljump.enums.WallFace;
import me.arthed.walljump.handlers.WorldGuardHandler;
import me.arthed.walljump.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

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

    protected WPlayer(Player player) {
        this.player = player;

        config = WallJump.getInstance().getWallJumpConfig();
        worldGuard = WallJump.getInstance().getWorldGuardHandler();
    }

    public void onWallJumpStart() {
        if(!canWallJump())
            return;

        WallJumpStartEvent event = new WallJumpStartEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())
            return;

        onWall = true;
        wallJumping = true;
        lastFacing = LocationUtils.getPlayerFacing(player);
        lastJumpLocation = player.getLocation();
        if(remainingJumps > 0)
            remainingJumps--;

        //Stop some anti cheat checks that might be caused by wall-jumping
        //AntiCheatUtils.stopPotentialAntiCheatChecks(player);

        //play sound and spawn particles
        //EffectUtils.playWallJumpSound(player, lastFacing, 0.3f, 1.2f);
        EffectUtils.spawnSlidingParticles(player, 5, lastFacing);

        //stop the player from falling and moving while on the wall
        //or make them slide down
        velocityY = 0;
        if(BukkitUtils.isVersionBefore(BukkitUtils.Version.V1_9))
            velocityY = 0.04f;
        velocityTask = Bukkit.getScheduler().runTaskTimerAsynchronously(WallJump.getInstance(), () -> {
            player.setVelocity(new Vector(0, velocityY, 0));
            if(velocityY != 0) {
                EffectUtils.spawnSlidingParticles(player, 2, lastFacing);
                if(sliding) {
                    if (player.isOnGround() || !LocationUtils.getBlockPlayerIsStuckOn(player, lastFacing).getType().isSolid()) {
                        Bukkit.getScheduler().runTask(WallJump.getInstance(), () -> {
                            player.setFallDistance(0);
                            player.teleport(player.getLocation());
                            onWallJumpEnd(false);
                        });
                    }
                    if (lastJumpLocation.getY() - player.getLocation().getY() >= 1.2) {
                        lastJumpLocation = player.getLocation();
                        //EffectUtils.playWallJumpSound(player, lastFacing, 0.2f, 0.6f);
                    }
                }
            }
        }, 0, 1);

        //make the player fall | slide when the time runs out
        if(fallTask != null)
            fallTask.cancel();
        fallTask = Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), () -> {
            if(onWall) {
                if (config.getBoolean("slide")) {
                    velocityY = (float) -config.getDouble("slidingSpeed");
                    sliding = true;
                } else {
                    Bukkit.getScheduler().runTask(WallJump.getInstance(), (Runnable) this::onWallJumpEnd);
                }
            }
        }, (long)(config.getDouble("timeOnWall")*20));

        //cancel the task for resetting wall jumping if the player wall jumps
        if(stopWallJumpingTask != null)
            stopWallJumpingTask.cancel();


    }

    public void onWallJumpEnd() {
        onWallJumpEnd(true);
    }

    public void onWallJumpEnd(boolean jump) {
        //AntiCheatUtils.restartPotentialAntiCheatChecks(player);

        onWall = false;
        sliding = false;


        //allow the player to move again
        player.setFallDistance(0);
        velocityTask.cancel();

        //call event
        WallJumpEndEvent event = new WallJumpEndEvent(this, config.getDouble("horizontalJumpPower"), config.getDouble("verticalJumpPower"));
        Bukkit.getPluginManager().callEvent(event);
        //if the player is not sliding or can jump while sliding and is not looking down
        if(jump &&// !event.isCancelled() &&
                ((velocityY == 0 && player.getLocation().getPitch() < 85) ||
                (config.getBoolean("canJumpWhileSliding") && player.getLocation().getPitch() < 60)))
            //push the player in the direction that they are looking
            VelocityUtils.pushPlayerInFront(player,
                    event.getHorizontalPower(),
                    event.getVerticalPower());

        //after 1.5 seconds, if the player hasn't walled jumped again, reset everything
        Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), () -> {
            if(LocationUtils.isOnGround(player)) {
                reset();
            }
        }, 12);

        stopWallJumpingTask = Bukkit.getScheduler().runTaskLaterAsynchronously(WallJump.getInstance(), this::reset, 24);

    }

    private void reset() {
        wallJumping = false;

        lastFacing = null;
        lastJumpLocation = null;
        remainingJumps = config.getInt("maxJumps");
        if(remainingJumps == 0)
            remainingJumps = -1;
        if(stopWallJumpingTask != null)
            stopWallJumpingTask.cancel();
        stopWallJumpingTask = null;
        Bukkit.getScheduler().runTask(WallJump.getInstance(), () -> {
            WallJumpResetEvent event = new WallJumpResetEvent(this);
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    public boolean canWallJump() {
        WallFace facing = LocationUtils.getPlayerFacing(player);
        if(lastJumpLocation != null)
            //used so height doesn't matter when calculating distance between the players location and the last jump location
            lastJumpLocation.setY(player.getLocation().getY());
        if(
                        !enabled ||
                        onWall || //player is already stuck to a wall
                        remainingJumps == 0 || //player reached jump limit
                        (lastFacing != null && lastFacing.equals(facing)) || //player is facing the same direction as the last jump
                        (lastJumpLocation != null && player.getLocation().distance(lastJumpLocation) <= config.getDouble("minimumDistance")) ||  //player is too close to the last jump location
                        player.getVelocity().getY() < config.getDouble("maximumVelocity") || //player is falling too fast
                        (config.getBoolean("needPermission") && !player.hasPermission("walljump.use")) || //player does not have the permission to wall-jump
                        (worldGuard != null && !worldGuard.canWallJump(player)) //wall-jumping is not allowed in the region the player is in
        )

            return false;
        //check if the block the player is wall jumping on is blacklisted
        boolean onBlacklistedBlock = config.getMaterialList("blacklistedBlocks").contains(
                player.getLocation().clone().add(facing.xOffset,
                        facing.yOffset,
                        facing.zOffset)
                        .getBlock()
                        .getType());
        boolean reverseBlockBlacklist = config.getBoolean("reversedBlockBlacklist");
        if((!reverseBlockBlacklist && onBlacklistedBlock) ||
                (reverseBlockBlacklist && !onBlacklistedBlock))
            return false;

        //check if the world the player is in is blacklisted
        boolean inBlacklistedWorld = config.getWorldList("blacklistedWorlds").contains(
                player.getWorld());
        boolean reverseWorldBlacklist = config.getBoolean("reversedWorldBlacklist");
        return (reverseWorldBlacklist || !inBlacklistedWorld) &&
                (!reverseWorldBlacklist || inBlacklistedWorld);
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
