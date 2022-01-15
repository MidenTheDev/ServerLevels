package net.cozycosmos.serverlevels.extras;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Files {

    private static File file;
    private static FileConfiguration customFile;

    //Finds or generates the custom config file
    public static boolean setup(Player player){
        file = new File(Main.getPlugin(Main.class).getDataFolder(), "/playerdata/"+player.getUniqueId()+".yml");
        File path = new File(Main.getPlugin(Main.class).getDataFolder(), "/playerdata/");
        path.mkdirs();

        if (!file.exists()){
            try{
                file.createNewFile();
                customFile = YamlConfiguration.loadConfiguration(file);
                return true;
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

    }


}
