package net.cozycosmos.serverlevels.customevents;

import net.cozycosmos.serverlevels.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LeveldownEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Main plugin = Main.getPlugin(Main.class);
    private String levelSystem;

    public LeveldownEvent(Player player, String levelsystem) {
        super(player);        levelSystem = levelsystem;
    }

    public String getLevelSystem() {return levelSystem;}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
