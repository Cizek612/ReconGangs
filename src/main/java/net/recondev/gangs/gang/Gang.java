package net.recondev.gangs.gang;

import lombok.Data;
import net.recondev.commons.storage.id.Id;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Gang {

    @Id private final String name;

    private final List<UUID> players;
    private transient final List<UUID> pendingInvites;
    private UUID leader;

    private long value;

    public Gang(final String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.pendingInvites = new ArrayList<>();


        this.value = 0;

    }


}
