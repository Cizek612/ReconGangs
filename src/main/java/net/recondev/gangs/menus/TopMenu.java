package net.recondev.gangs.menus;

import net.recondev.commons.builders.improved.ItemBuilder;
import net.recondev.commons.builders.improved.ItemUtils;
import net.recondev.commons.menus.MenuBuilder;
import net.recondev.commons.menus.MenuSimple;
import net.recondev.gangs.ReconGangs;
import net.recondev.gangs.gang.Gang;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopMenu {

    private final ReconGangs plugin;
    private final FileConfiguration config;

    private final List<Integer> gangSlots;

    private final Map<Integer, Gang> gangTop;



    public TopMenu(final ReconGangs plugin) {
        this.plugin = plugin;


        this.config = plugin.getSettingsConfig();
        this.gangSlots = config.getIntegerList("Menus.TOP-MENU.Top-Slots");

        this.gangTop = new HashMap<>();

        this.updateTop();
    }


    public void openMenu(final Player player) {

        final MenuSimple menu = MenuBuilder.builder().buildSimple(config.getString("Menus.TOP-MENU.Title"), config.getInt("Menus.TOP-MENU.Size"));


        final ItemBuilder item  = ItemUtils.getItem(config, "Menus.TOP-MENU.Items.GANG");
    }


    private void updateTop() {

    }
}
