package net.recondev.gangs.gang.storage;

import net.recondev.commons.storage.json.JsonStorage;
import net.recondev.commons.utils.Files;
import net.recondev.gangs.ReconGangs;
import net.recondev.gangs.gang.Gang;



public class GangStorage extends JsonStorage<String, Gang> {
    public GangStorage(final ReconGangs plugin) {
        super(Files.file("data.json", plugin), Gang.class);
    }
}
