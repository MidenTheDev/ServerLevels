package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Core implements CommandExecutor {
    private final Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equals("sl") || cmd.getName().equals("serverlevels")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("set")) {
                    if(args[1].equalsIgnoreCase("exp") || args[1].equalsIgnoreCase("e")) {
                        if (sender.hasPermission("serverlevels.SetExp")) {
                            SetExp setexp = new SetExp();
                            setexp.SetExp(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    } else if(args[1].equalsIgnoreCase("level") || args[1].equalsIgnoreCase("l")) {
                        if (sender.hasPermission("serverlevels.SetLevel")) {
                            SetLevel setlevel = new SetLevel();
                            setlevel.SetLevel(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    } else if(args[1].equalsIgnoreCase("multiplier") || args[1].equalsIgnoreCase("m")) {
                        if (sender.hasPermission("serverlevels.SetMultiplier")) {
                            SetMultiplier setmult = new SetMultiplier();
                            setmult.SetMultiplier(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    } else if(args[1].equalsIgnoreCase("playermultiplier") || args[1].equalsIgnoreCase("pm")) {
                        if (sender.hasPermission("serverlevels.SetPlayerMultiplier")) {
                            SetPlayerMultiplier setmult = new SetPlayerMultiplier();
                            setmult.SetPlayerMultiplier(sender, cmd, label, args);
                        } else {
                            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "You must specify what you want to set! Either EXP or Level!");
                    }

                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("serverlevels.reload")) {
                        sender.sendMessage(ChatColor.GREEN + "Running a shallow reload (playerdata not reloaded). Please note that the reload command is not perfect and may cause issues. It is always recommended to fully restart your server.");
                        //plugin.unregisterEvents();
                        plugin.reloadConfigs();
                        //plugin.registerEvents();
                        sender.sendMessage(ChatColor.GREEN + "Serverlevels reloaded.");

                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
                    }

                }

                else {
                    sender.sendMessage(ChatColor.RED + "You must enter a valid argument!");
                }
            } else {
                if (sender.hasPermission("serverlevels.command")) {
                    sender.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    sender.sendMessage(ChatColor.GRAY + "/sl set exp [player] [AmountToAdd]");
                    sender.sendMessage(ChatColor.GRAY + "Adds or removes exp from a player");

                    sender.sendMessage(ChatColor.GRAY + "/sl set level [player] [LevelsToAdd] [True/False]");
                    sender.sendMessage(ChatColor.GRAY + "Sets the level of the specified player.");

                    sender.sendMessage(ChatColor.GRAY + "/sl set multiplier [multiplier] (system)");
                    sender.sendMessage(ChatColor.GRAY + "Sets the exp gain multiplier for the specified level system.");

                    sender.sendMessage(ChatColor.GRAY + "/sl set playermultiplier [player] [multiplier] (system)");
                    sender.sendMessage(ChatColor.GRAY + "Sets the player's individual exp gain multiplier for the specified system.");

                    sender.sendMessage(ChatColor.GRAY + "/sl reload");
                    sender.sendMessage(ChatColor.GRAY + "Reloads config files of the plugin.");
                    sender.sendMessage(ChatColor.GOLD + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                } else {

                }

            }

        }
        return true;
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
