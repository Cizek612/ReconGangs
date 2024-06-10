package net.recondev.gangs.gang.player;

import lombok.Data;
import net.recondev.commons.storage.id.Id;

import java.util.UUID;

@Data
public class GangsPlayer {

    @Id private  final UUID uuid;
    private String gang;

    public GangsPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.gang = "";
    }
}
