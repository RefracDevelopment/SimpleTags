package me.refracdevelopment.simpletags.managers.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Tags {

    public ConfigurationSection TAGS;

    public Tags() {
        loadConfig();
    }

    public void loadConfig() {
        TAGS = SimpleTags.getInstance().getTagsFile().getConfigurationSection("tags");
    }
}
