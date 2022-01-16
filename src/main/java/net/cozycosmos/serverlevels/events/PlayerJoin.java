package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Files;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerJoin implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    File dataYml;
    FileConfiguration data;

    private boolean inConfig = false;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        if (Files.setup(e.getPlayer())) {
            dataYml = new File(plugin.getDataFolder()+"/playerdata/"+e.getPlayer().getUniqueId()+".yml");
            data = YamlConfiguration.loadConfiguration(dataYml);

            data.set("exp", 0.0);
            data.set("level", 1);
            data.set("lastmilestone", 0);
            data.set("exp-to-next-level", plugin.getConfig().getDouble("exp-per-level"));
            data.set("username", e.getPlayer().getName());
            try {
                data.save(dataYml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            dataYml = new File(plugin.getDataFolder()+"/playerdata/"+e.getPlayer().getUniqueId()+".yml");
            data = YamlConfiguration.loadConfiguration(dataYml);
            data.set("username", e.getPlayer().getName());
            try {
                data.save(dataYml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
