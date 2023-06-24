package net.cozycosmos.serverlevels.extras;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigUpdater {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    File levelsystemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
    FileConfiguration levelsystems = YamlConfiguration.loadConfiguration(levelsystemsYml);
    public void updateConfig(String configVer) {
        if(configVer.equals("1.2")) {
            config.set("exp-required-multiplier",config.getDouble("exp-multiplier"));
            config.set("exp-gain-multiplier",1.0);
            levelsystems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                levelsystems.set("systems."+system+".exp-required-multiplier",levelsystems.getDouble("systems."+system+".exp-multiplier"));
                levelsystems.set("systems."+system+".exp-gain-multiplier",1.0);
                try {
                    levelsystems.save(levelsystemsYml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            config.set("config-version","1.3");
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"ServerLevels config automatically updated! Please remove the exp-multiplier field in both your config.yml and levelsystems.yml, as it has been replaced by exp-required-multiplier");
            config.options().copyDefaults(true);
            Main.getPlugin(Main.class).saveConfig();
        }
    }

}
