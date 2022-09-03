package me.arthed.walljump.command;

import java.util.ArrayList;
import java.util.List;
import me.arthed.walljump.config.WallJumpConfiguration;
import me.arthed.walljump.player.WPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import me.arthed.walljump.WallJump;
import org.bukkit.entity.Player;

public class WallJumpCommand implements CommandExecutor, TabExecutor {

    private final WallJumpConfiguration config;

    public WallJumpCommand() {
        config = WallJump.getInstance().getWallJumpConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("walljump")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("reload")) {
                    config.reload();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[WallJump] &7Config reloaded!"));
                    return true;
                }
                else if(sender instanceof Player && config.getBoolean("toggleCommand")) {
                    if(args[0].equalsIgnoreCase("on")) {
                        WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = true;
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("toggleCommandMessageOn")));
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("off")) {
                        WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender).enabled = false;
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("toggleCommandMessageOff")));
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "Unknown command!");
                    return false;
                }
            }
            else if (sender instanceof Player && config.getBoolean("toggleCommand")) {
                WPlayer wPlayer = WallJump.getInstance().getPlayerManager().getWPlayer((Player)sender);
                if(wPlayer.enabled) {
                    wPlayer.enabled = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("toggleCommandMessageOff")));
                }
                else {
                    wPlayer.enabled = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("toggleCommandMessageOn")));
                }
                return true;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&e[WallJump] &7Version &7&l" +
                    WallJump.getInstance().getDescription().getVersion() +
                    " &7by &7&lArthed"));
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            List<String> arguments = new ArrayList<>();
            if(sender.hasPermission("walljump.reload"))
                arguments.add("reload");
            if(config.getBoolean("toggleCommand")) {
                arguments.add("on");
                arguments.add("off");
            }
            return getMatchingArgument(args[0], arguments);
        }
        return null;
    }

    private List<String> getMatchingArgument(String arg, List<String> elements) {
        List<String> list = new ArrayList<String>();
        for(String s : elements) {
            if(s.toLowerCase().contains(arg.toLowerCase()))
                list.add(s);
        }
        return list;
    }


}
