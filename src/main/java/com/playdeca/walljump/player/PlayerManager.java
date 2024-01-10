package com.playdeca.walljump.player;

import com.playdeca.walljump.WallJump;
import com.playdeca.walljump.config.WallJumpConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private final WallJumpConfiguration dataConfig;
    private final Map<Player, WPlayer> players = new HashMap<>();

    public PlayerManager() {
        dataConfig = WallJump.getInstance().getDataConfig();
    }

    /**
     * Registers a player by creating a new WPlayer instance and adding it to the players map.
     * If the player is already present in the dataConfig, their enabled status is retrieved and set on the WPlayer instance.
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
            if(dataConfig == null) return;

            WPlayer wplayer = new WPlayer(player);
            // If the player is already present in the dataConfig, their enabled status is retrieved and set on the WPlayer instance.
            if (dataConfig.contains(player.getUniqueId().toString())) {
                wplayer.enabled = dataConfig.getBoolean(player.getUniqueId().toString());
            }
            players.put(player, wplayer); // Add the WPlayer instance to the players map.
    }

    public void unregisterPlayer(Player player) {
        players.remove(player);
    }

    public WPlayer getWPlayer(Player player) {
        return players.get(player);
    }

    public Collection<WPlayer> getWPlayers() {
        return players.values();
    }

}
