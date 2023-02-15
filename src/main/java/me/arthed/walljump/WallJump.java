package me.arthed.walljump;

import me.arthed.walljump.api.WallJumpAPI;
import me.arthed.walljump.command.WallJumpCommand;
import me.arthed.walljump.config.WallJumpConfiguration;
import me.arthed.walljump.handlers.OtherPluginsHandler;
import me.arthed.walljump.handlers.WorldGuardHandler;
import me.arthed.walljump.listeners.*;
import me.arthed.walljump.player.PlayerManager;
import me.arthed.walljump.player.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class WallJump extends JavaPlugin {

    private static WallJump plugin;
    private final WallJumpAPI api;
    private PlayerManager playerManager;
    private WallJumpConfiguration config;
    private WallJumpConfiguration dataConfig;
    private WorldGuardHandler worldGuard;

    public WallJump() {
        api = new WallJumpAPI();
    }

    public static WallJump getInstance() {
        return plugin;
    }

    public WallJumpAPI getAPI() {
        return api;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public WallJumpConfiguration getWallJumpConfig() {
        return config;
    }

    public WallJumpConfiguration getDataConfig() {
        return dataConfig;
    }

    public WorldGuardHandler getWorldGuardHandler() {
        return worldGuard;
    }

    @Override
    public void onEnable() {
        playerManager = new PlayerManager();

        registerEvents(
                new PlayerJoinListener(),
                new PlayerQuitListener(),
                new PlayerToggleSneakListener(),
                new PlayerDamageListener(),
                new OtherPluginsHandler()
        );

        Objects.requireNonNull(this.getCommand("walljump")).setExecutor(new WallJumpCommand());

        //in case the plugin has been loaded while the server is running using plugman or any other similar methods, register all the online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.registerPlayer(player);
        }

    }

    @Override
    public void onLoad() {
        plugin = this;
        config = new WallJumpConfiguration("config.yml");
        dataConfig = new WallJumpConfiguration("data.yml");

        Plugin worldGuardPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin != null) {
            worldGuard = new WorldGuardHandler(worldGuardPlugin, this);
        }
    }

    @Override
    public void onDisable() {
        if (config.getBoolean("toggleCommand")) {
            for (WPlayer wplayer : playerManager.getWPlayers()) {
                dataConfig.set(wplayer.getPlayer().getUniqueId().toString(), wplayer.enabled);
            }
            dataConfig.save();
        }
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
