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

public class WallJumpCommand implements CommandExecutor, TabExecutor {

    private final WallJumpConfiguration config;
    public WallJumpCommand() {
        config = WallJump.getInstance().getWallJumpConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        try {
            // Check if the command is walljump
            if (cmd.getName().equalsIgnoreCase("walljump")) {
                if (args.length > 0) {
                    // Check if the sender wants to reload the config
                    if (args[0].equalsIgnoreCase("reload")) {
                        return handleReloadCommand(sender);
                    }
                    if (args[0].equalsIgnoreCase("toggle")) {
                        // Check if the sender wants to toggle the wall jump
                        return handleToggleCommand((Player) sender, args[1]);
                    }
                    if (args[0].equalsIgnoreCase("help")) {
                        // Check if the command is walljump and the argument is help
                        return handleHelpCommand(sender);
                    }
                    if (args[0].equalsIgnoreCase("info")) {
                        // Check if the command is walljump and the argument is info
                        return handleWallJumpInfo(sender);
                    }
                }
                return handleHelpCommand(sender);
            }
            // If the command is not recognized
            return false;
        } catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while executing the command.");
            return false;
        }
    }

    private boolean handleHelpCommand(@NotNull CommandSender sender) {
        Component helpMessage = Component.text("[===]").color(NamedTextColor.GOLD).append(Component.text("WallJump Plugin Help").color(NamedTextColor.WHITE).append(Component.text("[===]").color(NamedTextColor.GOLD)))
                .append(Component.newline());
        sender.sendMessage(helpMessage);
        Component helpMessage1 = Component.text("/walljump help ").color(NamedTextColor.WHITE).clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/walljump help")).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click me to show this help message").color(NamedTextColor.YELLOW)))
                .append(Component.text("Show this help message ").color(NamedTextColor.YELLOW));
        sender.sendMessage(helpMessage1);
        Component helpMessage2 = Component.text("/walljump info ").color(NamedTextColor.WHITE).clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/walljump info")).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click me to show plugin info").color(NamedTextColor.YELLOW)))
                .append(Component.text("Show plugin info").color(NamedTextColor.YELLOW));
        sender.sendMessage(helpMessage2);
        Component helpMessage3 = Component.text("/walljump reload ").color(NamedTextColor.WHITE).clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/walljump reload")).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click me to reload the plugin config").color(NamedTextColor.YELLOW)))
                .append(Component.text("Reload the plugin config ").color(NamedTextColor.YELLOW));
        sender.sendMessage(helpMessage3);
        Component helpMessage4 = Component.text("/walljump toggle [on|off] ").color(NamedTextColor.WHITE).clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/walljump toggle")).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Component.text("Click me to toggle wall jump on or off").color(NamedTextColor.YELLOW)))
                .append(Component.text("Toggle wall jump on or off ").color(NamedTextColor.YELLOW));
        sender.sendMessage(helpMessage4);
        return true;
    }

    private boolean handleReloadCommand(@NotNull CommandSender sender) {
        config.reload();
        Component message = Component.text("Config reloaded!").color(NamedTextColor.YELLOW);
        sender.sendMessage(message);
        return true;
    }

    private boolean handleToggleCommand(@NotNull Player player, String arg) {
        WPlayer wPlayer = WallJump.getInstance().getPlayerManager().getWPlayer(player);
        if (arg == null) {
            // Toggle command without specifying on or off
            Component message = Component.text("You must specify a state!").color(NamedTextColor.RED).append(Component.newline())
                    .append(Component.text("Usage: /walljump toggle [on|off]").color(NamedTextColor.YELLOW));
            player.sendMessage(message);
            return false;
        } else if (arg.equalsIgnoreCase("on")) {
            wPlayer.enabled = true;
            Component message = Component.text("Wall jump enabled!").color(NamedTextColor.YELLOW);
            player.sendMessage(message);
            return true;
        } else if (arg.equalsIgnoreCase("off")) {
            wPlayer.enabled = false;
            Component message = Component.text("Wall jump disabled!").color(NamedTextColor.YELLOW);
            player.sendMessage(message);
            return true;
        } else {
            // Unknown argument for toggle command
            Component message = Component.text("Unknown command!").color(NamedTextColor.RED).append(Component.newline())
                    .append(Component.text("Usage: /walljump toggle [on|off]").color(NamedTextColor.YELLOW));
            player.sendMessage(message);
            return false;
        }
    }

    private boolean handleWallJumpInfo(@NotNull CommandSender sender) {
        Component message = Component.text("WallJump version " + WallJump.getInstance().getDescription().getVersion() + " by Monster_What").color(NamedTextColor.YELLOW);
        sender.sendMessage(message);
        return true;
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
