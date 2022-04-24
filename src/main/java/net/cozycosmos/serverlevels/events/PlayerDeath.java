package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.File;

public class PlayerDeath implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    File systemsYml;
    FileConfiguration systems;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getKiller() != null) {
            Player killer = p.getKiller();
            Levels.setExp(killer,plugin.getConfig().getDouble("exp-on-playerkill"),"default");

            if (plugin.getConfig().getBoolean("extra-level-systems")) {
                systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
                systems = YamlConfiguration.loadConfiguration(systemsYml);
                systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                    if (systems.getDouble("systems."+system+".exp-on-playerkill") != -1.0) {
                        Levels.setExp(killer, systems.getDouble("systems."+system+".exp-on-playerkill"), system);
                    }
                });
            }

        }
    }
}
