package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void EntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            Levels.SetLevel(killer,plugin.getConfig().getDouble("exp-on-mobkill"));
        }
    }

}
