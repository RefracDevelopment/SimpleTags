package me.refracdevelopment.simpletags.utilities.chat;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Placeholders {

    public static String setPlaceholders(CommandSender sender, String placeholder) {
        placeholder = placeholder.replace("%prefix%", SimpleTags.getInstance().getLocaleFile().getString("prefix"));

        if (sender instanceof Player player) {
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());

            if (SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()) != null) {
                placeholder = placeholder.replace("%identifier%", profile.getTag());
                placeholder = placeholder.replace("%tag%", profile.getTagPrefix());
                placeholder = placeholder.replace("%tag-name%", profile.getTag());
                placeholder = placeholder.replace("%tag-prefix%", profile.getTagPrefix());
            } else {
                placeholder = placeholder.replace("%identifier%", "");
                placeholder = placeholder.replace("%tag%", "");
                placeholder = placeholder.replace("%tag-name%", "");
                placeholder = placeholder.replace("%tag-prefix%", "");
            }
        }

        placeholder = placeholder.replace("%arrow%", "»");
        placeholder = placeholder.replace("%arrow_2%", "➥");
        placeholder = placeholder.replace("%star%", "✦");
        placeholder = placeholder.replace("%circle%", "∙");
        placeholder = placeholder.replace("|", "⎟");

        return placeholder;
    }

    public static StringPlaceholders setPlaceholders(CommandSender sender) {
        StringPlaceholders.Builder placeholders = StringPlaceholders.builder();

        placeholders.add("prefix", SimpleTags.getInstance().getLocaleFile().getString("prefix"));

        if (sender instanceof Player player) {
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

            placeholders.add("player", player.getName());
            placeholders.add("displayname", player.getDisplayName());

            if (SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()) != null) {
                placeholders.add("identifier", profile.getTag());
                placeholders.add("tag", profile.getTagPrefix());
                placeholders.add("tag-name", profile.getTag());
                placeholders.add("tag-prefix", profile.getTagPrefix());
            } else {
                placeholders.add("identifier", "");
                placeholders.add("tag", "");
                placeholders.add("tag-name", "");
                placeholders.add("tag-prefix", "");
            }
        }

        placeholders.add("arrow", "»");
        placeholders.add("arrow2", "➥");
        placeholders.add("arrow_2", "➥");
        placeholders.add("star", "✦");
        placeholders.add("circle", "∙");
        placeholders.add("|", "⎟");

        return placeholders.build();
    }
}
