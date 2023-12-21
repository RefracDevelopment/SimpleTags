package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        if (SimpleTags.getInstance().getSettings().USE_CHAT) {
            event.setFormat(Color.translate(player, profile.getTagPrefix() + event.getFormat()));
        }
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().equalsIgnoreCase("/reload") ||
                event.getMessage().equalsIgnoreCase("/reload confirm")) {
            Color.sendMessage(player, "%prefix% Use of /reload is not recommended as it can cause issues often cases. Please restart your server when possible.");
        }
    }
}