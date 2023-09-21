package me.refracdevelopment.simpletags.player.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.menu.TagsItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Tag {

    private String configName, tagName, tagPrefix;

    public Tag(String configName, String tagName, String tagPrefix) {
        this.configName = configName;
        this.tagName = tagName;
        this.tagPrefix = tagPrefix;
    }

    public ItemStack toItemStack(Player player, String tag) {
        if (SimpleTags.getInstance().getTagManager().getCachedTag(tag) == null) return null;

        return new TagsItem().getItem(player, SimpleTags.getInstance().getTagManager().getCachedTag(tag));
    }
}
