package net.cozycosmos.serverlevels.extras;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.customevents.Levelup;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Levels {

    public static void SetLevel(Player player, Double expIn) {
        Levelup levelup = new Levelup(player);

        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/data.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        String puuid = player.getUniqueId().toString();

        data.set("users."+puuid+".exp", data.getDouble("users."+puuid+".exp") + expIn);
        Double exp = data.getDouble("users."+puuid+".exp");
        if (data.getInt("users."+puuid+".level") == 1) {

            if(exp >= plugin.getConfig().getDouble("exp-per-level")) {
                data.set("users."+puuid+".level",2);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));

                data.set("users."+puuid+".exp-to-next-level", plugin.getConfig().getDouble("exp-per-level")+(((data.getInt("users."+player.getUniqueId().toString()+".level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
                if(exp >= data.getDouble("users."+puuid+".exp-to-next-level")) {
                    while (data.getDouble("users."+puuid+".exp-to-next-level") <= exp) {
                        data.set("users."+puuid+".level", data.getInt("users."+puuid+".level") + 1);
                        data.set("users."+puuid+".exp-to-next-level", plugin.getConfig().getDouble("exp-per-level")+(((data.getInt("users."+player.getUniqueId().toString()+".level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));

                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                    }
                }
            }
        } else if (data.getInt("users."+puuid+".level") <= 0) {
            data.set("users."+puuid+".level",1);
        } else if (data.getInt("users."+puuid+".level") > 1) {
            data.set("users."+puuid+".exp-to-next-level", plugin.getConfig().getDouble("exp-per-level")+(((data.getInt("users."+player.getUniqueId().toString()+".level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
            if(exp >= data.getDouble("users."+puuid+".exp-to-next-level")) {
                while (data.getDouble("users."+puuid+".exp-to-next-level") <= exp) {
                    data.set("users."+puuid+".level", data.getInt("users."+puuid+".level") + 1);
                    data.set("users."+puuid+".exp-to-next-level", plugin.getConfig().getDouble("exp-per-level")+(((data.getInt("users."+player.getUniqueId().toString()+".level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));

                }
            }
        }

        try {
            data.save(dataYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void CheckMilestone(Player player) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/data.yml");
        File milestonesYml = new File(plugin.getDataFolder()+"/milestones.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        FileConfiguration milestones = YamlConfiguration.loadConfiguration(milestonesYml);

        String puuid = player.getUniqueId().toString();

        int level = data.getInt("users."+puuid+".level");

            milestones.getConfigurationSection("milestones").getKeys(false).forEach(milestone -> {
                if (level == Integer.parseInt(milestone)) {
                    data.set("users."+puuid+".lastmilestone",Integer.parseInt(milestone));

                    if(milestones.get("milestones."+milestone+".commands") != null) {
                        List<String> commands = milestones.getStringList("milestones."+milestone+".commands");
                        commands.forEach(cmd -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%",player.getName()));
                        });
                    }
                    if(milestones.get("milestones."+milestone+".messages") != null) {
                        List<String> messages = milestones.getStringList("milestones."+milestone+".messages");
                        messages.forEach(msg -> {
                            player.sendMessage(msg.replace('&','§'));
                        });
                    }

                } else {
                    //do nothing
                }
            });

        try {
            data.save(dataYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
