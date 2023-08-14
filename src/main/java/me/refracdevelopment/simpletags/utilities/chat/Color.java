package me.refracdevelopment.simpletags.utilities.chat;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.HexUtils;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.config.Config;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Color {

    public static String translate(CommandSender sender, String source) {
        source = Placeholders.setPlaceholders(sender, source);

        if (sender instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPIHook.applyPlaceholders((Player) sender, translate(source));
        } else return translate(source);
    }

    public static String translate(String source) {
        return HexUtils.colorify(source);
    }

    public static void log(String message) {
        final LocaleManager locale = SimpleTags.getInstance().getManager(LocaleManager.class);

        String prefix = locale.getLocaleMessage("prefix");

        locale.sendCustomMessage(Bukkit.getConsoleSender(), prefix + message);
    }

    public static void sendDebug(@Nullable CommandSender sender, String message) {
        if (!Config.DEBUG) return;
        if (sender != null) {
            sender.sendMessage(translate("&7Debug: " + message));
            return;
        }

        log("&7Debug: " + message);
    }
}
