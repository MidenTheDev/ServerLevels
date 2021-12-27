package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getKiller() != null) {
            Player killer = p.getKiller();
            Levels.setLevel(killer,plugin.getConfig().getDouble("exp-on-playerkill"));
        }
    }
}
