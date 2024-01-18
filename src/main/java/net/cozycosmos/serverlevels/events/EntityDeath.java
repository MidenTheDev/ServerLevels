package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.io.File;

public class EntityDeath implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    File mobsYml = new File(plugin.getDataFolder()+"/mobs.yml");
    FileConfiguration mobs = YamlConfiguration.loadConfiguration(mobsYml);
    File systemsYml;
    FileConfiguration systems;
    private boolean tempBool = false;

    @EventHandler
    public void EntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null && e.getEntity().getType() != EntityType.PLAYER) {
            Player killer = e.getEntity().getKiller();
            mobs.getConfigurationSection("").getKeys(false).forEach(mob -> {
                if (e.getEntity().getType().toString().equals(mob)) {
                    Levels.setExp(killer,mobs.getDouble(mob),"default");

                    if (plugin.getConfig().getBoolean("extra-level-systems")) {
                        systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
                        systems = YamlConfiguration.loadConfiguration(systemsYml);
                        systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                            if (systems.getDouble("systems."+system+".exp-on-mobkill") != -1.0) {
                                Levels.setExp(killer, mobs.getDouble(mob), system);

                            }
                        });
                    }

                    tempBool = true;
                }
            });
            if (!tempBool) {

                if (plugin.getConfig().getBoolean("extra-level-systems")) {
                    systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
                    systems = YamlConfiguration.loadConfiguration(systemsYml);
                    systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                        if (systems.getDouble("systems."+system+".exp-on-mobkill") != -1.0) {
                            Levels.setExp(killer, systems.getDouble("systems."+system+".exp-on-mobkill"), system);
                        }
                    });
                }

                Levels.setExp(killer,plugin.getConfig().getDouble("exp-on-mobkill"),"default");
            }
        }
    }

}
