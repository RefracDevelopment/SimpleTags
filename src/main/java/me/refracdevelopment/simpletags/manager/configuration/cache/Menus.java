package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Menus {

    public String TAGS_TITLE;
    public ConfigurationSection TAGS_ITEMS;

    public Menus() {
        loadConfig();
    }

    public void loadConfig() {
        TAGS_TITLE = SimpleTags.getInstance().getMenusFile().getString("tags.title");
        TAGS_ITEMS = SimpleTags.getInstance().getMenusFile().getConfigurationSection("tags.items");
    }
}