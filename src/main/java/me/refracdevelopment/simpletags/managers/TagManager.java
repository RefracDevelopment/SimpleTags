package me.refracdevelopment.simpletags.managers;

import lombok.Getter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TagManager {

    private final List<Tag> loadedTags = new ArrayList<>();
    private final List<ItemStack> loadedTagItems = new ArrayList<>();

    public void loadTags() {
        getLoadedTags().clear();

        SimpleTags.getInstance().getTags().TAGS.getKeys(false).forEach(tag ->
                addTag(new Tag(tag, SimpleTags.getInstance().getTagsFile().getString("tags." + tag + ".name"),
                        SimpleTags.getInstance().getTagsFile().getString("tags." + tag + ".prefix"),
                        SimpleTags.getInstance().getTagsFile().getString("tags." + tag + ".item.material")
                ))
        );

        RyMessageUtils.sendConsole(true, "&aLoaded &e" + loadedTags.size() + " &atags.");
    }

    public void updateTags() {
        SimpleTags.getInstance().getProfileManager().getProfiles().values().forEach(profile -> {
            if (getCachedTag(profile.getData().getTag()) == null) {
                profile.getData().setTag("");
                profile.getData().setTagPrefix("");
                return;
            }

            profile.getData().setTagPrefix(getCachedTag(profile.getData().getTag()).getTagPrefix());
        });
    }

    public void addTag(Tag tag) {
        if (!loadedTags.contains(tag))
            loadedTags.add(tag);
    }

    public void removeTag(Tag tag) {
        loadedTags.remove(tag);
    }

    public Tag getCachedTag(String name) {
        for (Tag tag : loadedTags)
            if (tag.getConfigName().equalsIgnoreCase(name))
                return tag;

        return null;
    }

    public List<String> getTagNames() {
        return loadedTags.stream().map(Tag::getConfigName).collect(Collectors.toList());
    }

    public List<ItemStack> getTagItems(Player player) {
        loadedTags.forEach(tag -> loadedTagItems.add(tag.toItemStack(player)));

        return loadedTagItems;
    }
}