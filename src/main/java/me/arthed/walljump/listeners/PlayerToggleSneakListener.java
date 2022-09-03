package me.arthed.walljump.listeners;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.player.PlayerManager;
import me.arthed.walljump.player.WPlayer;
import me.arthed.walljump.utils.LocationUtils;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerToggleSneakListener implements Listener {

    private final PlayerManager playerManager = WallJump.getInstance().getPlayerManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleSneak(@NotNull PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(!player.isFlying()) {
            WPlayer wplayer = playerManager.getWPlayer(player);
            if(wplayer.isOnWall() && !event.isSneaking())
                wplayer.onWallJumpEnd();
            else if(LocationUtils.isTouchingAWall(player) && event.isSneaking() && !player.isOnGround())
                wplayer.onWallJumpStart();
        }
    }

}
