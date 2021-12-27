package net.cozycosmos.serverlevels.events;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BlockBreak implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    @EventHandler
    public void onPlace(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Levels.setLevel(p, plugin.getConfig().getDouble("exp-on-blockbreak"));
    }
}
