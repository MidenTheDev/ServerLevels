package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.customevents.Levelup;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class LevelupLis implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    File dataYml = new File(plugin.getDataFolder()+"/data.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);

    @EventHandler
    public void onLevelUp(Levelup e) {
        if(plugin.getConfig().getBoolean("use-milestones")) {
            Levels.checkMilestone(e.getPlayer());
        }
        if (plugin.getConfig().getBoolean("send-message-on-levelup")) {
            e.getPlayer().sendMessage(plugin.getConfig().getString("levelup-message").replace('&','§').replaceAll("%level%",String.valueOf(data.getInt("users."+e.getPlayer().getUniqueId().toString()+".level"))));
        }
    }
}
