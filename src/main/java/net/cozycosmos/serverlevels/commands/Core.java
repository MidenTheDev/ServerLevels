package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Core implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equals("sl") || cmd.getName().equals("serverlevels")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("set")) {
                    if(args[1].equalsIgnoreCase("exp")) {
                        if (sender.hasPermission("serverlevels.SetExp")) {
                            SetExp setexp = new SetExp();
                            setexp.SetExp(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    } else if(args[1].equalsIgnoreCase("level")) {
                        if (sender.hasPermission("serverlevels.SetLevel")) {
                            SetLevel setlevel = new SetLevel();
                            setlevel.SetLevel(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must specify what you want to set! Either EXP or Level!");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "You must enter a valid argument!");
                }
            } else {
                if (sender.hasPermission("serverlevels.command")) {
                    sender.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    sender.sendMessage(ChatColor.GRAY + "/sl set exp [player] [AmountToAdd]");
                    sender.sendMessage(ChatColor.GRAY + "Adds or removes exp from a player");
                    sender.sendMessage(ChatColor.GRAY + "/sl set level [player] [LevelsToAdd] [True/False]");
                    sender.sendMessage(ChatColor.GRAY + "Sets the level of the specified player.");
                    sender.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                } else {

                }

            }

        }
        return true;
    }
}
