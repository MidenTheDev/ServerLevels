package net.cozycosmos.serverlevels;

import net.cozycosmos.serverlevels.commands.Core;
import net.cozycosmos.serverlevels.events.*;
import net.cozycosmos.serverlevels.extras.ConfigUpdater;
import net.cozycosmos.serverlevels.extras.Metrics;
import net.cozycosmos.serverlevels.extras.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public PluginManager pm;
    public ConsoleCommandSender cs;

    File milestonesYml = new File(getDataFolder()+"/milestones.yml");
    File mobsYml = new File(getDataFolder()+"/mobs.yml");
    File systemsYml = new File(getDataFolder()+"/levelsystems.yml");


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

        ConfigUpdater configUpdater = new ConfigUpdater();
        configUpdater.updateConfig(getConfig().getString("config-version"));

        if(!milestonesYml.exists()){
            cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Creating milestones.yml");
            this.saveResource("milestones.yml", false);
        }else{
            // do nothing
        }
        if(!mobsYml.exists()){
            cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Creating mobs.yml");
            this.saveResource("mobs.yml", false);
        }else{
            // do nothing
        }
        if(!systemsYml.exists()){
            cs.sendMessage(ChatColor.GRAY+"[ServerLevels] "+ChatColor.GREEN + "Creating levelsystems.yml");
            this.saveResource("levelsystems.yml", false);
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

    public void reloadConfigs() {
        reloadConfig();
        YamlConfiguration.loadConfiguration(milestonesYml);
        YamlConfiguration.loadConfiguration(mobsYml);
        YamlConfiguration.loadConfiguration(systemsYml);

    }




}
