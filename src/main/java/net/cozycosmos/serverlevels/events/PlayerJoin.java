package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayerJoin implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);
    File dataYml = new File(plugin.getDataFolder()+"/data.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPlayedBefore()) {
            //check for them in the config?
        } else {
            data.set("users."+e.getPlayer().getUniqueId().toString()+".exp", 0.0);
            data.set("users."+e.getPlayer().getUniqueId().toString()+".level", 1);
            data.set("users."+e.getPlayer().getUniqueId().toString()+".lastmilestone", 0);
            data.set("users."+e.getPlayer().getUniqueId().toString()+".username", e.getPlayer().getName());
        }
    }
}
