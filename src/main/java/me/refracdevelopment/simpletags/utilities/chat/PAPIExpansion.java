package me.refracdevelopment.simpletags.utilities.chat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return SimpleTags.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simpletags";
    }

    @Override
    public @NotNull String getVersion() {
        return SimpleTags.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        switch (params) {
            case "tag-prefix":
            case "tag":
                if (profile == null) {
                    return "";
                }
                if (profile.getTagPrefix().isEmpty()) {
                    return "";
                }
                return Color.translate(profile.getTagPrefix());
            case "tag-name":
            case "identifier":
                if (profile == null) {
                    return "";
                }
                if (profile.getTag().isEmpty()) {
                    return "";
                }
                return profile.getTag();
        }

        return null;
    }
}