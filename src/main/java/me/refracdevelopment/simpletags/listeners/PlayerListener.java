package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.data.Profile;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        try {
            SimpleTags.getInstance().getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        } catch (NullPointerException exception) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "&cERROR: Could not create profile!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());

        try {
            Tasks.runAsync(SimpleTags.getInstance(), () -> profile.getData().load());
        } catch (NullPointerException exception) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }

        if (profile == null || profile.getData() == null) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
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