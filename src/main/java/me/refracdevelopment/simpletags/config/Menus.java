package me.refracdevelopment.simpletags.config;

import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Menus {

    public static ConfigurationSection TAGS_ITEMS;

    public static void loadConfig() {
        SimpleTags.getInstance().getMenusFile().load();
        TAGS_ITEMS = SimpleTags.getInstance().getMenusFile().getConfigurationSection("tags.items");
    }
}