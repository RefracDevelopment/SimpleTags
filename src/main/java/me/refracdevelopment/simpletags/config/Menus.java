package me.refracdevelopment.simpletags.config;

import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Menus {

    public static String TAGS_TITLE;
    public static ConfigurationSection TAGS_ITEMS;

    public static void loadConfig() {
        TAGS_TITLE = SimpleTags.getInstance().getMenusFile().getString("tags.title");
        TAGS_ITEMS = SimpleTags.getInstance().getMenusFile().getConfigurationSection("tags.items");
    }
}