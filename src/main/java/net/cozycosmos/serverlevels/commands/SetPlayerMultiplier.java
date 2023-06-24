package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SetPlayerMultiplier {
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    File levelsystemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
    FileConfiguration levelsystems = YamlConfiguration.loadConfiguration(levelsystemsYml);

    public boolean SetPlayerMultiplier(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 4 || args[4].equalsIgnoreCase("default")) {
            if(Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {

                if(SetExp.isDouble(args[3])) {
                    File dataYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/playerdata/"+Bukkit.getOfflinePlayer(args[2]).getUniqueId()+".yml");
                    FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
                    data.set("multiplier-default",Double.parseDouble(args[3]));
                    try {
                        data.save(dataYml);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(ChatColor.GREEN+"Player Multipler set! The multipler will last until changed again or unset in the player's data file.");

                } else {
                    sender.sendMessage(ChatColor.RED+"Multiplier must be a valid double! Ex: 1.0");
                }



            } else {
                sender.sendMessage(ChatColor.RED+"Player "+ args[2]+" does not exist!");
            }
        }
        else if (args.length>=5) {
            if(Bukkit.getOfflinePlayer(args[2]).hasPlayedBefore()) {

                if(SetExp.isDouble(args[3])) {

                    if(levelsystems.getConfigurationSection("systems."+args[4]) != null) {
                        File dataYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/playerdata/"+Bukkit.getOfflinePlayer(args[2]).getUniqueId()+".yml");
                        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
                        data.set("multiplier-"+args[4],Double.parseDouble(args[3]));
                        try {
                            data.save(dataYml);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage(ChatColor.GREEN+"Player Multipler set! The multipler will last until changed again or unset in the player's data file.");

                    } else {
                        sender.sendMessage(ChatColor.RED+"There does not appear to be a level system titled: "+args[4]);
                    }


                } else {
                    sender.sendMessage(ChatColor.RED+"Multiplier must be a valid double! Ex: 1.0");
                }



            } else {
                sender.sendMessage(ChatColor.RED+"Player "+ args[2]+" does not exist!");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED+"Not Enough Arguments!");
            sender.sendMessage(ChatColor.RED+"/sl Set PlayerMultiplier [player] [multiplier] (system)");
        }
        return true;
    }
}
