package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Files;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
    File systemsYml;
    FileConfiguration systems;

    private boolean inConfig = false;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        File oldDataYml = new File(plugin.getDataFolder()+"/data.yml");
        FileConfiguration oldData = YamlConfiguration.loadConfiguration(oldDataYml);
        ConsoleCommandSender cs = Bukkit.getServer().getConsoleSender();
        systemsYml = new File(Main.getPlugin(Main.class).getDataFolder()+"/levelsystems.yml");
        systems = YamlConfiguration.loadConfiguration(systemsYml);
        dataYml = new File(plugin.getDataFolder()+"/playerdata/"+e.getPlayer().getUniqueId()+".yml");
        data = YamlConfiguration.loadConfiguration(dataYml);

        if (Files.setup(e.getPlayer())) {



            if(oldDataYml.exists() && !oldData.getBoolean("users."+e.getPlayer().getUniqueId()+".converted",false)) {
                data.set("exp", oldData.getDouble("users."+e.getPlayer().getUniqueId()+".exp"));
                data.set("level", oldData.getInt("users."+e.getPlayer().getUniqueId()+".level"));
                data.set("lastmilestone", oldData.getInt("users."+e.getPlayer().getUniqueId()+".lastmilestone"));
                data.set("exp-to-next-level", oldData.getDouble("users."+e.getPlayer().getUniqueId()+".exp-to-next-level"));
                data.set("username", e.getPlayer().getName());
                oldData.set("users."+e.getPlayer().getUniqueId()+".converted", true);
                cs.sendMessage("Check 1");
            } else {
                data.set("exp", 0.0);
                data.set("level", 1);
                data.set("lastmilestone", 0);
                data.set("exp-to-next-level", plugin.getConfig().getDouble("exp-per-level"));
                data.set("username", e.getPlayer().getName());
                cs.sendMessage("Check 2");
                if(plugin.getConfig().getBoolean("extra-level-systems",false)) {
                    cs.sendMessage("Check 3");
                    systems.getConfigurationSection("systems").getKeys(false).forEach(system -> {
                        cs.sendMessage("Check 4");
                        data.set(system+".exp", 0.0);
                        data.set(system+".level", 1);
                        data.set(system+".lastmilestone", 0);
                        data.set(system+".exp-to-next-level", systems.getDouble("systems."+system+".exp-per-level"));

                    });
                } else {
                    cs.sendMessage("Check 5");
                }

            }
            try {
                data.save(dataYml);

                oldData.save(oldDataYml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            data.set("username", e.getPlayer().getName());

            try {
                data.save(dataYml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
