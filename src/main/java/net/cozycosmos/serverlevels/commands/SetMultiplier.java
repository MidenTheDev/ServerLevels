package net.cozycosmos.serverlevels.commands;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SetMultiplier {
    private final FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    File levelsystemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
    FileConfiguration levelsystems = YamlConfiguration.loadConfiguration(levelsystemsYml);

    public boolean SetMultiplier(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 3) {
            if (SetExp.isDouble(args[2])) {
                config.set("exp-gain-multiplier",Double.parseDouble(args[2]));
                sender.sendMessage(ChatColor.GREEN+"Multipler set! The multipler will last until changed again or unset in the config.yml.");
                config.options().copyDefaults(true);
                Main.getPlugin(Main.class).saveConfig();
            } else {
                sender.sendMessage(ChatColor.RED+"Multiplier must be a valid double! Ex: 1.0");
            }

        } else if (args.length >= 4) {
            if (SetExp.isDouble(args[2])) {
                if (args[3].equalsIgnoreCase("default")) {
                    config.set("exp-gain-multiplier",Double.parseDouble(args[2]));
                    sender.sendMessage(ChatColor.GREEN+"Multipler set! The multipler will last until changed again or unset in the config.yml.");
                    config.options().copyDefaults(true);
                    Main.getPlugin(Main.class).saveConfig();
                } else {
                    if (levelsystems.getConfigurationSection("systems."+args[3]) != null) {
                        levelsystems.set("systems."+args[3]+".exp-gain-multiplier",Double.parseDouble(args[2]));
                        sender.sendMessage(ChatColor.GREEN+"Multipler set! The multipler will last until changed again or unset in the levelsystems.yml.");
                        try {
                            levelsystems.save(levelsystemsYml);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED+"There does not appear to be a level system titled: "+args[3]);
                    }


                }
            } else {
                sender.sendMessage(ChatColor.RED+"Multiplier must be a valid double! Ex: 1.0");
            }
        } else {
            sender.sendMessage(ChatColor.RED+"Not Enough Arguments!");
            sender.sendMessage(ChatColor.RED+"/sl Set Multiplier [multiplier] (level system)");
        }
        return true;
    }
}
