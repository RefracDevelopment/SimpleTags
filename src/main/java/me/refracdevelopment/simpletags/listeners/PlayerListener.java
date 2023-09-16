package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.configuration.cache.Config;
import me.refracdevelopment.simpletags.player.Profile;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SimpleTags.getInstance().getProfileManager().handleProfileCreation(player.getUniqueId(), player.getName());

        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Tasks.runAsync(SimpleTags.getInstance(), () -> profile.getData().load());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null || profile.getData() == null) {
            player.kickPlayer(Color.translate(Config.KICK_MESSAGES_ERROR));
            return;
        }

        if (player.getUniqueId().equals(Utilities.getDevUUID)) {
            Utilities.sendDevMessage(player);
        } else if (player.getUniqueId().equals(Utilities.getDevUUID2)) {
            Utilities.sendDevMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getData() == null) return;

        Tasks.runAsync(SimpleTags.getInstance(), () -> profile.getData().save());
    }
}