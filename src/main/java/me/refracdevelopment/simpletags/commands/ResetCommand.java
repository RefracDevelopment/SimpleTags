package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().RESET_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().RESET_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-reset-description");
    }

    @Override
    public String getSyntax() {
        return "[player]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player player)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        if (!player.hasPermission(Permissions.RESET_COMMAND)) {
            RyMessageUtils.sendPluginMessage(player, "no-permission");
            return;
        }

        if (args.length == 1) {
            switch (SimpleTags.getInstance().getDataType()) {
                case MYSQL:
                    SimpleTags.getInstance().getMySQLManager().delete();

                    Bukkit.getOnlinePlayers().forEach(online -> {
                        online.kickPlayer(RyMessageUtils.translate(online, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                    });
                    break;
                default:
                    SimpleTags.getInstance().getSqLiteManager().delete();

                    Bukkit.getOnlinePlayers().forEach(online -> {
                        online.kickPlayer(RyMessageUtils.translate(online, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                    });
                    break;
            }
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                switch (SimpleTags.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleTags.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.kickPlayer(RyMessageUtils.translate(p, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                        });
                        break;
                    default:
                        SimpleTags.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Bukkit.getOnlinePlayers().forEach(p -> {
                            p.kickPlayer(RyMessageUtils.translate(p, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                        });
                        break;
                }
            } else if (target.hasPlayedBefore()) {
                switch (SimpleTags.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleTags.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        RyMessageUtils.sendPluginMessage(player, "tag-reset-player", StringPlaceholders.of("player", target.getName()));
                        break;
                    default:
                        SimpleTags.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        RyMessageUtils.sendPluginMessage(player, "tag-reset-player", StringPlaceholders.of("player", target.getName()));
                        break;
                }
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));

        if (args.length == 2) {
            return names;
        }
        return null;
    }
}
