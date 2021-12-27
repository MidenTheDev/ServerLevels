package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addexp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equals("sl") || cmd.getName().equals("serverlevels")) {
            if(args.length > 0) {
                if(args[0].equals("addexp")) {
                    if (sender.hasPermission("serverlevels.addexp")) {
                        if(args.length > 1) {
                            if(Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                                if(args.length > 2) {
                                    if(isDouble(args[2])) {
                                        Levels.setLevel((Player) Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
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
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must enter a valid argument!");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "The only command for now is /sl addexp!");
            }

        }
        return true;
    }

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}



