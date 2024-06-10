package net.recondev.gangs.listeners;

import net.recondev.commons.abstracts.ReconListener;
import net.recondev.gangs.ReconGangs;
import net.recondev.gangs.gang.player.GangsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener extends ReconListener<ReconGangs> {

    private final ReconGangs plugin;
    public JoinListener(final ReconGangs plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();

        if(!plugin.getPlayerStorage().contains(uuid)) return;

        this.plugin.getPlayerStorage().save(new GangsPlayer(uuid));

    }
}
