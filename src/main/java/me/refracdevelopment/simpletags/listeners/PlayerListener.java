package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.Profile;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        SimpleTags.getInstance().getProfileManager().handleProfileCreation(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());

        Tasks.runAsync(() -> profile.getData().load(player));

        if (profile == null || profile.getData() == null) {
            player.kick(RyMessageUtils.adventureTranslate(player, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
            return;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null)
            return;
        if (profile.getData() == null)
            return;

        Tasks.runAsync(() -> profile.getData().save(player));
        SimpleTags.getInstance().getProfileManager().getProfiles().remove(player.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        if (SimpleTags.getInstance().getSettings().USE_CHAT)
            event.setFormat(ChatColor.translateAlternateColorCodes('&', profile.getTagPrefix() + ChatColor.RESET + event.getFormat()));
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!event.getMessage().equalsIgnoreCase("/reload confirm"))
            return;

        RyMessageUtils.sendPlayer(player, "&cUse of /reload is not recommended as it can cause issues often cases. Please restart your server when possible.");
    }
}