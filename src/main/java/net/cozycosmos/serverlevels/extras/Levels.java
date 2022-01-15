package net.cozycosmos.serverlevels.extras;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.customevents.LeveldownEvent;
import net.cozycosmos.serverlevels.customevents.LevelupEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Levels {

    public static Boolean setExp(Player player, Double expIn) {
        LevelupEvent levelup = new LevelupEvent(player);
        LeveldownEvent leveldownEvent = new LeveldownEvent(player);

        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        String puuid = player.getUniqueId().toString();

        Double newExp = data.getDouble("exp") + expIn;


        if(newExp < data.getDouble("exp")) {
            data.set("exp", newExp);
            double expToLast = data.getDouble("exp-to-next-level")-(((data.getInt("level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level"));
            if (expToLast>newExp) {
                data.set("exp-to-next-level",expToLast);
                data.set("level", data.getInt("level")-1);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(leveldownEvent));

            }
            try {
                data.save(dataYml);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            if (data.getInt("level") < plugin.getConfig().getInt("max-level", 0) || plugin.getConfig().getInt("max-level", 0) == 0) {
                data.set("exp", newExp);
                Double exp = data.getDouble("exp");
                if (data.getInt("level") == 1) {

                    if (exp >= plugin.getConfig().getDouble("exp-per-level")) {
                        data.set("level", 2);
                        data.set("exp-to-next-level", data.getDouble("exp-to-next-level") + (((data.getInt("level") - 1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));

                        if (exp >= data.getDouble("exp-to-next-level")) {
                            while (data.getDouble("exp-to-next-level") <= exp) {
                                data.set("level", data.getInt("level") + 1);

                                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                                data.set("exp-to-next-level", data.getDouble("exp-to-next-level") + (((data.getInt("level") - 1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
                            }
                        }
                    }
                } else if (data.getInt("level") <= 0) {
                    data.set("level", 1);
                    data.set("exp-to-next-level", plugin.getConfig().getDouble("exp-per-level"));
                } else if (data.getInt("level") > 1) {
                    if (exp >= data.getDouble("exp-to-next-level")) {
                        while (data.getDouble("exp-to-next-level") <= exp) {
                            data.set("level", data.getInt("level") + 1);
                            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                            data.set("exp-to-next-level", data.getDouble("exp-to-next-level") + (((data.getInt("level") - 1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));

                        }
                    }
                }

                try {
                    data.save(dataYml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }

    }

    public static boolean checkMilestone(Player player) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        File milestonesYml = new File(plugin.getDataFolder()+"/milestones.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        FileConfiguration milestones = YamlConfiguration.loadConfiguration(milestonesYml);

        final Boolean[] foundMilestone = {false};
        String puuid = player.getUniqueId().toString();

        int level = data.getInt("level");

            milestones.getConfigurationSection("milestones").getKeys(false).forEach(milestone -> {
                if (level == Integer.parseInt(milestone)) {
                    data.set("lastmilestone",Integer.parseInt(milestone));

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
                    foundMilestone[0] = true;
                } else {
                    //do nothing
                }
            });

        try {
            data.save(dataYml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundMilestone[0];
    }

    public static Boolean setLevel(Player player, int newLevel, Boolean changeExp) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        LevelupEvent levelup = new LevelupEvent(player);
        LeveldownEvent leveldownEvent = new LeveldownEvent(player);

        String expPath = "exp";


        if (newLevel <= plugin.getConfig().getInt("max-level",0) || plugin.getConfig().getInt("max-level", 0) == 0) {
            if(newLevel > data.getInt("level")) {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                data.set("exp-to-next-level", data.getDouble("exp-to-next-level")+(((data.getInt("level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));

            } else if (newLevel < data.getInt("level")) {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(leveldownEvent));
            }
            data.set("level", newLevel);

            if (changeExp) {
                if (newLevel == 1) {
                    data.set(expPath, 0);
                } else if (newLevel >= 2){
                    data.set("exp-to-next-level", data.getDouble("exp-to-next-level")+(((data.getInt("level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level")));
                    Double expToNext = ((data.getInt("level")-1) * plugin.getConfig().getDouble("exp-multiplier")) * plugin.getConfig().getDouble("exp-per-level");
                    data.set(expPath, data.getDouble("exp-to-next-level")-expToNext);
                }
            }
            try {
                data.save(dataYml);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }

    }

    public static int getLevel(Player player) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        return data.getInt("level");
    }

    public static Double getExp(Player player) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        return data.getDouble("exp");
    }


}
