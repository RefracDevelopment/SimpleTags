package me.refracdevelopment.simpletags.utilities.chat;

import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.HexUtils;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.configuration.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Color {

    public static String translate(CommandSender sender, String source) {
        source = Placeholders.setPlaceholders(sender, source);

        if (sender instanceof Player) {
            return PlaceholderAPIHook.applyPlaceholders((Player) sender, translate(source));
        } else return translate(source);
    }

    public static String translate(String source) {
        return HexUtils.colorify(source);
    }

    public static void sendMessage(Player player, String message) {
        final LocaleManager locale = SimpleTags.getInstance().getManager(LocaleManager.class);

        message = Placeholders.setPlaceholders(player, message);

        locale.sendCustomMessage(player, message);
    }

    public static void log(String message) {
        final LocaleManager locale = SimpleTags.getInstance().getManager(LocaleManager.class);

        String prefix = locale.getLocaleMessage("prefix");

        locale.sendCustomMessage(Bukkit.getConsoleSender(), prefix + message);
    }
}
