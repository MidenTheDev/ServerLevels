package net.cozycosmos.serverlevels.customevents;

import net.cozycosmos.serverlevels.Main;
import net.cozycosmos.serverlevels.extras.Levels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class Levelup extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Main plugin = Main.getPlugin(Main.class);

    public Levelup(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
