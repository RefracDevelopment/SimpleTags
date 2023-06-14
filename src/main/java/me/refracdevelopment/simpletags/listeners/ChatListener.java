package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.config.Config;
import me.refracdevelopment.simpletags.data.ProfileData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        if (Config.USE_CHAT) {
            event.setFormat(profile.getTagPrefix() + event.getFormat());
        }
    }
}