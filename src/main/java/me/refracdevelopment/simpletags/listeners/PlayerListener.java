package me.refracdevelopment.simpletags.listeners;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.Profile;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final UUID getDevUUID = UUID.fromString("d9c670ed-d7d5-45fb-a144-8b8be86c4a2d");
    private final UUID getDevUUID2 = UUID.fromString("ab898e40-9088-45eb-9d69-e0b78e872627");

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        SimpleTags.getInstance().getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());

        Tasks.runAsync(() -> profile.getData().load());

        if (profile == null || profile.getData() == null) {
            player.kickPlayer(Color.translate(SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
            return;
        }

        if (player.getUniqueId().equals(getDevUUID)) {
            sendDevMessage(player);
        } else if (player.getUniqueId().equals(getDevUUID2)) {
            sendDevMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getData() == null) return;

        Tasks.runAsync(() -> profile.getData().save());
        SimpleTags.getInstance().getProfileManager().getProfiles().remove(player.getUniqueId());
    }

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

        if (!event.getMessage().equalsIgnoreCase("/reload confirm")) return;

        Color.sendCustomMessage(player, "&cUse of /reload is not recommended as it can cause issues often cases. Please restart your server when possible.");
    }

    private void sendDevMessage(Player player) {

        player.sendMessage("");
        Color.sendCustomMessage(player, "&aWelcome " + SimpleTags.getInstance().getDescription().getName() + " Developer!");
        Color.sendCustomMessage(player, "&aThis server is currently running " + SimpleTags.getInstance().getDescription().getName() + " &bv" + SimpleTags.getInstance().getDescription().getVersion() + "&a.");
        Color.sendCustomMessage(player, "&aPlugin name&7: &f" + SimpleTags.getInstance().getDescription().getName());
        player.sendMessage("");
        Color.sendCustomMessage(player, "&aServer version&7: &f" + Bukkit.getVersion());
        player.sendMessage("");
    }
}