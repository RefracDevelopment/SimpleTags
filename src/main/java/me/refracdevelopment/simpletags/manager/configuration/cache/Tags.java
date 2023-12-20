package me.refracdevelopment.simpletags.manager.configuration.cache;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.configuration.ConfigurationSection;

public class Tags {

    public Section TAGS;

    public Tags() {
        loadConfig();
    }

    public void loadConfig() {
        TAGS = SimpleTags.getInstance().getTagsFile().getSection("tags");
    }
}
