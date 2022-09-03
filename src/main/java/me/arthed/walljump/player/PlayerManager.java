package me.arthed.walljump.player;

import me.arthed.walljump.WallJump;
import me.arthed.walljump.config.WallJumpConfiguration;
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

    public void registerPlayer(Player player) {
        WPlayer wplayer = new WPlayer(player);
        if(dataConfig.contains(player.getUniqueId().toString()))
            wplayer.enabled = dataConfig.getBoolean(player.getUniqueId().toString());
        players.put(player, wplayer);
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
