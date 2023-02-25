package com.playdeca.walljump.listeners;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PlayerManager playerManager = WallJump.getInstance().getPlayerManager();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerManager.unregisterPlayer(event.getPlayer());
    }

}
