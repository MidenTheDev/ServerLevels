package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;

public class BlockPlace implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    File systemsYml;
    FileConfiguration systems;
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (plugin.getConfig().getBoolean("extra-level-systems")) {
            systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
            systems = YamlConfiguration.loadConfiguration(systemsYml);
            systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                if (systems.getDouble("systems."+system+".exp-on-blockplace") != -1.0) {
                    Levels.setExp(p, systems.getDouble("systems."+system+".exp-on-blockplace"), system);
                }
            });
        }

        Levels.setExp(p, plugin.getConfig().getDouble("exp-on-blockplace"),"default");
    }

}
