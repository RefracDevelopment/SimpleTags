package me.refracdevelopment.simpletags.utilities.chat;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.data.ProfileData;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Placeholders {

    public static String setPlaceholders(CommandSender sender, String placeholder) {
        final LocaleManager locale = SimpleTags.getInstance().getManager(LocaleManager.class);

        placeholder = placeholder.replace("%prefix%", locale.getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

            placeholder = placeholder.replace("%player%", player.getName());
            placeholder = placeholder.replace("%displayname%", player.getDisplayName());
            placeholder = placeholder.replace("%tag%", profile.getTagPrefix());
            if (SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()) != null) {
                placeholder = placeholder.replace("%tag-name%", SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()).getTagName());
            } else {
                placeholder = placeholder.replace("%tag-name%", "");
            }
            placeholder = placeholder.replace("%tag-prefix%", profile.getTagPrefix());
        }
        placeholder = placeholder.replace("%arrow%", "\u00BB");
        placeholder = placeholder.replace("%arrow_2%", "\u27A5");
        placeholder = placeholder.replace("%star%", "\u2726");
        placeholder = placeholder.replace("%circle%", "\u2219");
        placeholder = placeholder.replace("|", "\u239F");

        return placeholder;
    }

    public static StringPlaceholders setPlaceholders(CommandSender sender) {
        StringPlaceholders.Builder placeholders = StringPlaceholders.builder();
        final LocaleManager locale = SimpleTags.getInstance().getManager(LocaleManager.class);

        placeholders.add("prefix", locale.getLocaleMessage("prefix"));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

            placeholders.add("player", player.getName());
            placeholders.add("displayname", player.getDisplayName());
            placeholders.add("tag", profile.getTagPrefix());
            if (SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()) != null) {
                placeholders.add("tag-name", SimpleTags.getInstance().getTagManager().getCachedTag(profile.getTag()).getTagName());
            } else {
                placeholders.add("tag-name", "");
            }
            placeholders.add("tag-prefix", profile.getTagPrefix());
        }
        placeholders.add("arrow", "\u00BB");
        placeholders.add("arrow2", "\u27A5");
        placeholders.add("arrow_2", "\u27A5");
        placeholders.add("star", "\u2726");
        placeholders.add("circle", "\u2219");
        placeholders.add("|", "\u239F");

        return placeholders.build();
    }
}