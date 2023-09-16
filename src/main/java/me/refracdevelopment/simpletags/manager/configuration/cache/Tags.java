package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Tags {

    public static ConfigurationSection TAGS;

    public static void loadConfig() {
        SimpleTags.getInstance().getTagsFile().load();
        TAGS = SimpleTags.getInstance().getTagsFile().getConfigurationSection("tags");
    }
}
