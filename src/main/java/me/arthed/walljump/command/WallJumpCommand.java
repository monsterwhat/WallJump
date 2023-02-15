package me.arthed.walljump.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.arthed.walljump.config.WallJumpConfiguration;
import me.arthed.walljump.player.WPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import me.arthed.walljump.WallJump;
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
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        // Check if the command is walljump
        if(cmd.getName().equalsIgnoreCase("walljump")) {
            // Check if the sender is a player
            if(args.length > 0) {
                // Check if the sender wants to reload the config
                if(args[0].equalsIgnoreCase("reload")) {
                    // Check if the sender has the permission to reload the config
                    config.reload();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[WallJump] &7Config reloaded!"));
                    return true;
                }
                // Check if the sender wants to toggle the wall jump
                else if(sender instanceof Player && config.getBoolean("toggleCommand")) {
                    // Check if the sender wants to enable the wall jump
                    if(args[0].equalsIgnoreCase("on")) {
                        // Enable the wall jump
                        WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = true;
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("toggleCommandMessageOn"))));
                        return true;
                    }
                    // Check if the sender wants to disable the wall jump
                    else if(args[0].equalsIgnoreCase("off")) {
                        // Disable the wall jump
                        WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = false;
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("toggleCommandMessageOff"))));
                        return true;
                    }
                    // If the sender didn't specify on or off
                    sender.sendMessage(ChatColor.RED + "Unknown command!");
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
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("toggleCommandMessageOff"))));
                }
                // If the wall jump is disabled
                else {
                    // Enable the wall jump
                    wPlayer.enabled = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("toggleCommandMessageOn"))));
                }
                return true;
            }
            //Send the version
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&e[WallJump] &7Version &7&l" +
                    WallJump.getInstance().getDescription().getVersion() +
                    " &7by &7&lMonster_What"));
            return true;
        }
        // If the command is not walljump
        return false;
    }

    // The tab completer
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
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
    }

    // Get the matching arguments
    private List<String> getMatchingArgument(String arg, List<String> elements) {
        List<String> list = new ArrayList<>();
        for(String s : elements) {
            if(s.toLowerCase().contains(arg.toLowerCase()))
                list.add(s);
        }
        return list;
    }
}
