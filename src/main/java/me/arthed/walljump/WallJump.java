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

// This class is the main class of the plugin
public final class WallJump extends JavaPlugin {

    // The plugin instance
    private static WallJump plugin;
    // The API
    private final WallJumpAPI api;
    // The player manager
    private PlayerManager playerManager;
    // The config
    private WallJumpConfiguration config;
    // The data config
    private WallJumpConfiguration dataConfig;
    // The WorldGuard handler
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

    //This code is part of an onEnable() method, which is executed when a plugin is loaded by the server.
    //
    //Creates a new PlayerManager instance
    //Registers various event listeners
    //Registers a command
    //Registers all online players with the PlayerManager
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

    //This code is part of an onLoad() method, which is executed when a plugin is loaded by the server.
    //
    //Sets the plugin instance
    //Creates a new WallJumpConfiguration instance for the config.yml file
    //Creates a new WallJumpConfiguration instance for the data.yml file
    //Checks if WorldGuard is installed
    //If it is, creates a new WorldGuardHandler instance
    //If it isn't, sets the WorldGuardHandler instance to null
    //Saves the config.yml file
    //Saves the data.yml file
    //If the toggleCommand option is enabled, loads the data from the data.yml file
    //If the toggleCommand option is disabled, enables wall jump for all players

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

    //This code is part of an onDisable() method, which is executed when a plugin is unloaded by the server.
    //If the toggleCommand option is enabled, saves the data to the data.yml file
    //If the toggleCommand option is disabled, disables wall jump for all players
    //Saves the config.yml file
    //Saves the data.yml file
    @Override
    public void onDisable() {
        if (config.getBoolean("toggleCommand")) {
            for (WPlayer wplayer : playerManager.getWPlayers()) {
                dataConfig.set(wplayer.getPlayer().getUniqueId().toString(), wplayer.enabled);
            }
            dataConfig.save();
        }
    }

    //This code is part of a registerEvents() method, which is executed when a plugin is loaded by the server.
    //Registers all the listeners passed as arguments

        private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
