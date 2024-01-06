package com.playdeca.walljump.command;

import java.util.ArrayList;
import java.util.List;
import com.playdeca.walljump.config.WallJumpConfiguration;
import com.playdeca.walljump.player.WPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import com.playdeca.walljump.WallJump;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// This class is used to handle the /walljump command
public class WallJumpCommand implements CommandExecutor, TabExecutor {

    // The config
    private final WallJumpConfiguration config;
    // The constructor
    public WallJumpCommand() {
        config = WallJump.getInstance().getWallJumpConfig();
    }

    // The command executor
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        try {
            // Check if the command is walljump
            if(cmd.getName().equalsIgnoreCase("walljump")) {
                // Check if the sender is a player
                if(args.length > 0) {
                    // Check if the sender wants to reload the config
                    if(args[0].equalsIgnoreCase("reload")) {
                        // Check if the sender has the permission to reload the config
                        config.reload();
                        Component message = Component.text("Config reloaded!").color(NamedTextColor.YELLOW);
                        sender.sendMessage(message);
                        return true;
                    }
                    // Check if the sender wants to toggle the wall jump
                    else if(sender instanceof Player && config.getBoolean("toggleCommand")) {
                        // Check if the sender wants to enable the wall jump
                        if(args[0].equalsIgnoreCase("on")) {
                            // Enable the wall jump
                            WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = true;
                            Component message = Component.text("Wall jump enabled!").color(NamedTextColor.YELLOW);
                            sender.sendMessage(message);
                            return true;
                        }
                        // Check if the sender wants to disable the wall jump
                        else if(args[0].equalsIgnoreCase("off")) {
                            // Disable the wall jump
                            WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = false;
                            Component message = Component.text("Wall jump disabled!").color(NamedTextColor.YELLOW);
                                sender.sendMessage(message);
                            return true;
                        }
                        // If the sender didn't specify on or off
                        Component message = Component.text("Unknown command!").color(NamedTextColor.RED);
                        sender.sendMessage(message);
                        return false;
                    }
                }
                // If the sender is a player and the toggle command is enabled
                else if (sender instanceof Player && config.getBoolean("toggleCommand")) {
                    // Toggle the wall jump
                    WPlayer wPlayer = WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender);
                    // Check if the wall jump is enabled
                    if(wPlayer.enabled) {
                        // Disable the wall jump
                        wPlayer.enabled = false;
                        Component message = Component.text("Wall jump disabled!").color(NamedTextColor.YELLOW);
                        sender.sendMessage(message);
                    }
                    // If the wall jump is disabled
                    else {
                        // Enable the wall jump
                        wPlayer.enabled = true;
                        Component message = Component.text("Wall jump enabled!").color(NamedTextColor.YELLOW);
                        sender.sendMessage(message);
                    }
                    return true;
                }
                //Send the version
                Component message = Component.text("WallJump version " + WallJump.getInstance().getDescription().getVersion() + " by Monster_What").color(NamedTextColor.YELLOW);
                sender.sendMessage(message);
                return true;
            }
            // If the command is not walljump
            return false;

        }catch (Exception e){
            Bukkit.getLogger().warning("An error occurred while executing the command.");
            return false;
        }
    }

    // The tab completer
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        try {
            if(args.length > 0) {
                List<String> arguments = new ArrayList<>();
                // Check if the sender has the permission to reload the config
                if(sender.hasPermission("walljump.reload"))
                    arguments.add("reload");
                // Check if the sender is a player and the toggle command is enabled
                if(config.getBoolean("toggleCommand")) {
                    arguments.add("on");
                    arguments.add("off");
                }
                // Return the matching arguments
                return getMatchingArgument(args[0], arguments);
            }
            return null;
        }catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while tab completing the command.");
            return null;
        }
    }

    // Get the matching arguments
    private List<String> getMatchingArgument(String arg, List<String> elements) {
        try {
            List<String> list = new ArrayList<>();
            for(String s : elements) {
                if(s.toLowerCase().contains(arg.toLowerCase()))
                    list.add(s);
            }
            return list;
        }catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while getting the matching arguments.");
            return null;
        }
    }
}
