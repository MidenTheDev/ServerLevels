package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class SetExp {
    private final Main plugin = Main.getPlugin(Main.class);
    File systemsYml;
    FileConfiguration systems;

    public boolean SetExp(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length > 2) {
            if(Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {
                if(args.length > 3) {
                    if(isDouble(args[3])) {
                        if (args.length > 4) {
                            if (plugin.getConfig().getBoolean("extra-level-systems",false)) {
                                Levels.setExp((Player) Bukkit.getOfflinePlayer(args[2]), Double.parseDouble(args[3]), args[4]);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Too many arguments! You only need the system name if you have extra-level-systems enabled!");
                            }
                        } else {
                            if (Levels.setExp((Player) Bukkit.getOfflinePlayer(args[2]), Double.parseDouble(args[3]), "default")) {
                                sender.sendMessage(ChatColor.GREEN + "EXP successfully changed");
                            }
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "The amount must be a Double! Instead of 1, try 1.0");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount! (Ex. 1.0)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Player name not recognized.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Please specify a player");
        }

        return true;
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}



