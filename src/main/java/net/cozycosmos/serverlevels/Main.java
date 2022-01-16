package net.cozycosmos.serverlevels;

import net.cozycosmos.serverlevels.commands.Core;
import net.cozycosmos.serverlevels.commands.SetExp;
import net.cozycosmos.serverlevels.events.*;
import net.cozycosmos.serverlevels.extras.Metrics;
import net.cozycosmos.serverlevels.extras.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public JavaPlugin instance;
    public PluginManager pm;
    public ConsoleCommandSender cs;

    File milestonesYml = new File(getDataFolder()+"/milestones.yml");


    public void onEnable() {
        pm = Bukkit.getServer().getPluginManager();
        cs = Bukkit.getServer().getConsoleSender();

        cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Enabling ServerLevels");

        new UpdateChecker(this, 98696).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                cs.sendMessage(ChatColor.GRAY +"[ServerLevels] "+ChatColor.GREEN + "You're running the latest version!");
            } else {
                cs.sendMessage(ChatColor.GRAY +"[ServerLevels] "+ChatColor.GREEN + "There's a new update available!");
                cs.sendMessage(ChatColor.GRAY +"[ServerLevels] "+ChatColor.GREEN + "You're running version "+this.getDescription().getVersion()+ " While the latest version is "+version+"!");
            }
        });

        registerConfigs();
        registerEvents();
        registerCommands();


        int pluginId = 13727;
        Metrics metrics = new Metrics(this, pluginId);

        cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "ServerLevels Enabled");

    }

    public void onDisable() {

    }

    public void registerConfigs() {
        cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Registering Configs");
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        if(!milestonesYml.exists()){
            cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Creating milestones.yml");
            this.saveResource("milestones.yml", false);
        }else{
            // do nothing
        }

    }

    public void registerCommands() {
        cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Registering commands");
        getCommand("serverlevels").setExecutor(new Core());
    }

    public void registerEvents() {
        cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Registering Events");
        pm.registerEvents(new LevelupLis(), this);

        if(getConfig().getDouble("exp-on-blockbreak") != -1.0) {
            pm.registerEvents(new BlockBreak(), this);
        }
        if(getConfig().getDouble("exp-on-blockplace") != -1.0) {
            pm.registerEvents(new BlockPlace(), this);
        }
        if(getConfig().getDouble("exp-on-mobkill") != -1.0) {
            pm.registerEvents(new EntityDeath(), this);
        }

            pm.registerEvents(new PlayerChat(), this);

        if(getConfig().getDouble("exp-on-playerkill") != -1.0) {
            pm.registerEvents(new PlayerDeath(), this);
        }
        pm.registerEvents(new PlayerJoin(), this);
    }



}
