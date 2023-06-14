package me.refracdevelopment.simpletags.manager;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.data.Tag;
import me.refracdevelopment.simpletags.utilities.chat.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagManager {

    private final List<Tag> loadedTags = new ArrayList<>();

    public void loadTags() {
        loadedTags.clear();
        SimpleTags.getInstance().getTagsFile().getConfigurationSection("tags").getKeys(false).forEach(tag -> {
            addTag(new Tag(tag, SimpleTags.getInstance().getTagsFile().getString("tags." + tag + ".name"), SimpleTags.getInstance().getTagsFile().getString("tags." + tag + ".prefix")));
        });
        Color.log("&eLoaded " + loadedTags.size() + " tags.");
    }

    public void addTag(Tag tag) {
        if (!loadedTags.contains(tag)) {
            loadedTags.add(tag);
        }
    }

    public void removeTag(Tag tag) {
        loadedTags.remove(tag);
    }

    public Tag getCachedTag(String name) {
        for (Tag tag : loadedTags) {
            if (tag.getConfigName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public List<Tag> getLoadedTags() {
        return loadedTags;
    }

    public void updateTags() {
        SimpleTags.getInstance().getProfileManager().getProfiles().values().forEach(profile -> {
            if (getCachedTag(profile.getData().getTag()) == null) {
                profile.getData().setTag("");
                profile.getData().setTagPrefix("");
            } else {
                profile.getData().setTagPrefix(getCachedTag(profile.getData().getTag()).getTagPrefix());
            }
        });
    }

    public Optional<Tag> findByName(String tagName) {
        return this.loadedTags.stream().filter(tag -> tag.getTagName().equalsIgnoreCase(tagName))
                .findFirst();
    }
}