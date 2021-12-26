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
    File dataYml = new File(plugin.getDataFolder()+"/data.yml");


    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(plugin.getConfig().getDouble("exp-on-chat") != -1.0) {
            Levels.SetLevel(player, plugin.getConfig().getDouble("exp-on-chat"));
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataYml);
        if (plugin.getConfig().getBoolean("show-level-as-prefix")) {
            e.setFormat("§"+config.getString("prefix-border-color")+"[§"+config.getString("prefix-number-color")+valueOf(data.getInt("users."+e.getPlayer().getUniqueId().toString()+".level"))+"§"+config.getString("prefix-border-color")+"]§r"+" %s : %s");

        }

    }
}
