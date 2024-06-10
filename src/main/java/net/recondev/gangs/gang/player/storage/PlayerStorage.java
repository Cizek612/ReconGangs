package net.recondev.gangs.gang.player.storage;

import net.recondev.commons.storage.json.JsonStorage;
import net.recondev.commons.utils.Files;
import net.recondev.gangs.ReconGangs;
import net.recondev.gangs.gang.player.GangsPlayer;

import java.util.UUID;

public class PlayerStorage extends JsonStorage<UUID, GangsPlayer> {
    public PlayerStorage(final ReconGangs plugin) {
        super(Files.file("player-data.json", plugin), GangsPlayer.class);
    }
}
