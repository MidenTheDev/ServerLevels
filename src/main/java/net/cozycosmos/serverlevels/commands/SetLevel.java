package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLevel {
    private final Main plugin = Main.getPlugin(Main.class);
    public boolean SetLevel(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length > 2) {
            if(Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {
                if(args.length > 3 ) {
                    if(isInt(args[3])) {
                        if (args.length>4) {
                            if (isBool(args[4])) {
                                if (!plugin.getConfig().getBoolean("extra-level-systems",false)) {
                                    if (Boolean.parseBoolean(args[4])) {
                                        if (Levels.setLevel((Player) Bukkit.getOfflinePlayer(args[2]), Integer.parseInt(args[3]), true, "default")) {
                                            sender.sendMessage(ChatColor.GREEN + "Level successfully changed");
                                        }
                                    } else {
                                        if (Levels.setLevel((Player) Bukkit.getOfflinePlayer(args[2]), Integer.parseInt(args[3]), false, "default")) {
                                            sender.sendMessage(ChatColor.GREEN + "Level successfully changed");
                                        }
                                    }
                                } else {
                                    if (args.length > 5) {
                                        if (Boolean.parseBoolean(args[4])) {
                                            if (Levels.setLevel((Player) Bukkit.getOfflinePlayer(args[2]), Integer.parseInt(args[3]), true, args[5])) {
                                                sender.sendMessage(ChatColor.GREEN + "Level successfully changed");
                                            }
                                        } else {
                                            if (Levels.setLevel((Player) Bukkit.getOfflinePlayer(args[2]), Integer.parseInt(args[3]), false, args[5])) {
                                                sender.sendMessage(ChatColor.GREEN + "Level successfully changed");
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "You must specify a level system! Write 'default' for the default system");
                                    }
                                }

                            } else {
                                sender.sendMessage(ChatColor.RED + "Specify if you want the player's exp to be changed. Add 'true' to your command to change the exp, add 'false' to leave exp as-is.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Specify if you want the player's exp to be changed. Add 'true' to your command to change the exp, add 'false' to leave exp as-is.");
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "The amount must be an integer!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount! (Ex. 10)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Player name not recognized.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Please specify a player");
        }

        return true;
    }

    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean isBool(String str) {
        try {
            Boolean.parseBoolean(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}



