package net.recondev.gangs;

import lombok.Getter;
import net.recondev.commons.chat.MessageCache;
import net.recondev.commons.config.ConfigManager;
import net.recondev.commons.storage.Storage;
import net.recondev.gangs.commands.GangsCommand;
import net.recondev.gangs.gang.Gang;
import net.recondev.gangs.gang.player.GangsPlayer;
import net.recondev.gangs.gang.player.storage.PlayerStorage;
import net.recondev.gangs.gang.storage.GangStorage;
import net.recondev.gangs.listeners.JoinListener;
import net.recondev.gangs.menus.TopMenu;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class ReconGangs extends JavaPlugin {

    public static ReconGangs plugin;
    private ConfigManager<ReconGangs> configManager;

    private FileConfiguration settingsConfig, messagesConfig;

    private MessageCache messageCache;

    public Storage<String, Gang> gangStorage;
    public Storage<UUID, GangsPlayer> playerStorage;

    public TopMenu topMenu;


    @Override
    public void onEnable() {

        plugin = this;



        this.gangStorage = new GangStorage(this);
        this.playerStorage = new PlayerStorage(this);

        this.loadConfigurations();
        this.loadMessages();
        this.loadListeners();
        this.loadCommands();

        this.topMenu = new TopMenu(this);



    }

    @Override
    public void onDisable() {
        this.gangStorage.write();
        this.playerStorage.write();
    }


    private void loadCommands() {
        new GangsCommand(this);
    }

    private void loadListeners() {
        new JoinListener(this);

    }

    private void loadMessages() {
        this.messageCache = new MessageCache(messagesConfig);

        for(final String key : this.messagesConfig.getConfigurationSection("Messages").getKeys(false)) {
            this.messageCache.loadMessage("Messages." + key);
        }
    }

    private void loadConfigurations() {
        this.configManager = new ConfigManager<>(this);

        this.configManager.loadConfiguration("settings", "messages");

        this.settingsConfig = this.configManager.getConfig("settings");
        this.messagesConfig = this.configManager.getConfig("messages");
    }
}
