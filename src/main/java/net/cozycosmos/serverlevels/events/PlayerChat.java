package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;

import static java.lang.String.valueOf;

public class PlayerChat implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();
    File dataYml;
    File systemsYml;
    FileConfiguration systems;

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        dataYml = new File(plugin.getDataFolder()+"/playerdata/"+e.getPlayer().getUniqueId()+".yml");

        Player player = e.getPlayer();

        Levels.setExp(player, plugin.getConfig().getDouble("exp-on-chat"), "default");

        if (plugin.getConfig().getBoolean("extra-level-systems")) {
            systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
            systems = YamlConfiguration.loadConfiguration(systemsYml);
            systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                if (systems.getDouble("systems."+system+".exp-on-chat") != -1.0) {
                    Levels.setExp(player, systems.getDouble("systems."+system+".exp-on-chat"), system);
                }
            });
        }


        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        if (plugin.getConfig().getBoolean("show-level-as-prefix")) {
            String DName = e.getPlayer().getDisplayName();
            e.getPlayer().setDisplayName("§"+config.getString("prefix-border-color")+"[§"+config.getString("prefix-number-color")+valueOf(data.getInt("level"))+"§"+config.getString("prefix-border-color")+"]§r"+" "+DName);

        }

    }
}
