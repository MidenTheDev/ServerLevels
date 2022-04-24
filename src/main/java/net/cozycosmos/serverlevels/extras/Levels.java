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


    public static Boolean setExp(Player player, Double expIn, String levelsystem) {
        LevelupEvent levelup = new LevelupEvent(player, levelsystem);
        LeveldownEvent leveldownEvent = new LeveldownEvent(player, levelsystem);

        String path = levelsystem + ".";
        String systemPath = "systems."+levelsystem+".";
        File systemsYml;

        final Main plugin = Main.getPlugin(Main.class);
        FileConfiguration config;

        if (path.equals("default.")) {
            path = "";
            systemPath = "";
            config = plugin.getConfig();
        } else {
            systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");

            config = YamlConfiguration.loadConfiguration(systemsYml);
        }

        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        String puuid = player.getUniqueId().toString();

        Double newExp = data.getDouble(path+"exp") + expIn;


        if(newExp < data.getDouble(path+"exp")) {
            data.set(path+"exp", newExp);
            double expToLast = data.getDouble(path+"exp-to-next-level")-(((data.getInt(path+"level")-1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level"));
            if (expToLast>newExp) {
                data.set(path+"exp-to-next-level",expToLast);
                data.set(path+"level", data.getInt(path+"level")-1);
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(leveldownEvent));

            }
            try {
                data.save(dataYml);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            if (data.getInt(path+"level") < config.getInt(systemPath+"max-level", 0) || config.getInt(systemPath+"max-level", 0) == 0) {
                data.set(path+"exp", newExp);
                Double exp = data.getDouble(path+"exp");
                if (data.getInt(path+"level") == 1) {

                    if (exp >= config.getDouble(systemPath+"exp-per-level")) {
                        data.set(path+"level", 2);
                        data.set(path+"exp-to-next-level", data.getDouble(path+"exp-to-next-level") + (((data.getInt(path+"level") - 1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level")));
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));

                        if (exp >= data.getDouble(path+"exp-to-next-level")) {
                            while (data.getDouble(path+"exp-to-next-level") <= exp) {
                                data.set(path+"level", data.getInt(path+"level") + 1);

                                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                                data.set(path+"exp-to-next-level", data.getDouble(path+"exp-to-next-level") + (((data.getInt(path+"level") - 1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level")));
                            }
                        }
                    }
                } else if (data.getInt(path+"level") <= 0) {
                    data.set(path+"level", 1);
                    data.set(path+"exp-to-next-level", config.getDouble(systemPath+"exp-per-level"));
                } else if (data.getInt(path+"level") > 1) {
                    if (exp >= data.getDouble(path+"exp-to-next-level")) {
                        while (data.getDouble(path+"exp-to-next-level") <= exp) {
                            data.set(path+"level", data.getInt(path+"level") + 1);
                            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                            data.set(path+"exp-to-next-level", data.getDouble(path+"exp-to-next-level") + (((data.getInt(path+"level") - 1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level")));

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

    public static boolean checkMilestone(Player player, String levelsystem) {
        String path = levelsystem + ".";
        String systemPath = levelsystem;

        final Main plugin = Main.getPlugin(Main.class);

        if (path.equals("default.")) {
            path = "";
            systemPath = "milestones";
        }
        final String finalPath = path;
        final String finalSystemPath = systemPath;

        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        File milestonesYml = new File(plugin.getDataFolder()+"/milestones.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        FileConfiguration milestones = YamlConfiguration.loadConfiguration(milestonesYml);

        final Boolean[] foundMilestone = {false};

        int level = data.getInt(path+"level");

            milestones.getConfigurationSection(systemPath).getKeys(false).forEach(milestone -> {

                if (level == Integer.parseInt(milestone)) {
                    data.set(finalPath+"lastmilestone",Integer.parseInt(milestone));

                    if(milestones.get(finalSystemPath+"."+milestone+".commands") != null) {
                        List<String> commands = milestones.getStringList(finalSystemPath+"."+milestone+".commands");
                        commands.forEach(cmd -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%",player.getName()));
                        });
                    }
                    if(milestones.get(finalSystemPath+"."+milestone+".messages") != null) {
                        List<String> messages = milestones.getStringList(finalSystemPath+"."+milestone+".messages");
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

    public static Boolean setLevel(Player player, int newLevel, Boolean changeExp, String levelsystem) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

        LevelupEvent levelup = new LevelupEvent(player,levelsystem);
        LeveldownEvent leveldownEvent = new LeveldownEvent(player, levelsystem);

        String expPath = "exp";

        String path = levelsystem + ".";
        String systemPath = "systems."+levelsystem+".";
        File systemsYml;

        FileConfiguration config;

        if (path.equals("default.")) {
            path = "";
            systemPath = "";
            config = plugin.getConfig();

        } else {
            expPath = levelsystem+".exp";
            systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");

            config = YamlConfiguration.loadConfiguration(systemsYml);
        }


        if (newLevel <= config.getInt(systemPath+"max-level",0) || config.getInt(systemPath+"max-level", 0) == 0) {
            if(newLevel > data.getInt(path+"level")) {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(levelup));
                data.set(path+"exp-to-next-level", data.getDouble(path+"exp-to-next-level")+(((data.getInt(path+"level")-1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level")));

            } else if (newLevel < data.getInt(path+"level")) {
                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(leveldownEvent));
            }
            data.set(path+"level", newLevel);

            if (changeExp) {
                if (newLevel == 1) {
                    data.set(expPath, 0);
                } else if (newLevel >= 2){
                    data.set(path+"exp-to-next-level", data.getDouble(path+"exp-to-next-level")+(((data.getInt(path+"level")-1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level")));
                    Double expToNext = ((data.getInt(path+"level")-1) * config.getDouble(systemPath+"exp-multiplier")) * config.getDouble(systemPath+"exp-per-level");
                    data.set(expPath, data.getDouble(path+"exp-to-next-level")-expToNext);
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

    public static int getLevel(Player player, String levelsystem) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        String path = levelsystem+".";
        if (levelsystem.equals("default")) {
            path = "";
        }

        return data.getInt(path+"level");
    }

    public static Double getExp(Player player, String levelsystem) {
        final Main plugin = Main.getPlugin(Main.class);
        File dataYml = new File(plugin.getDataFolder()+"/playerdata/"+player.getUniqueId()+".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        String path = levelsystem+".";
        if (levelsystem.equals("default")) {
            path = "";
        }
        return data.getDouble(path+"exp");
    }


}
