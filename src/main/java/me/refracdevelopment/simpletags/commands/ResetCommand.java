package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.Color;
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
        if (!commandSender.hasPermission(Permissions.RESET_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (args.length == 1) {
            Tasks.runAsync(() -> {
                switch (SimpleTags.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleTags.getInstance().getMySQLManager().delete();

                        Tasks.run(() -> Bukkit.getOnlinePlayers().forEach(player -> {
                            player.kickPlayer(Color.translate(player, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                        }));
                        break;
                    case SQLITE:
                        SimpleTags.getInstance().getSqLiteManager().delete();

                        Tasks.run(() -> Bukkit.getOnlinePlayers().forEach(player -> {
                            player.kickPlayer(Color.translate(player, SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                        }));
                        break;
                    default:
                        Tasks.run(() -> {
                            Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB and SQLite.");
                        });
                        break;
                }
            });
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                Tasks.runAsync(() -> {
                    switch (SimpleTags.getInstance().getDataType()) {
                        case MYSQL:
                            SimpleTags.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                            Tasks.run(() -> {
                                Color.sendMessage(commandSender, "tags-reset-player");

                                target.getPlayer().kickPlayer(Color.translate(target.getPlayer(), SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                            break;
                        case SQLITE:
                            SimpleTags.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                            Tasks.run(() -> {
                                Color.sendMessage(commandSender, "tags-reset-player");

                                target.getPlayer().kickPlayer(Color.translate(target.getPlayer(), SimpleTags.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                            break;
                        default:
                            Tasks.run(() -> {
                                Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB and SQLite.");
                            });
                            break;
                    }
                });
            } else if (target.hasPlayedBefore()) {
                switch (SimpleTags.getInstance().getDataType()) {
                    case MYSQL:
                        SimpleTags.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Tasks.run(() -> Color.sendMessage(commandSender, "gems-reset-player"));
                        break;
                    case SQLITE:
                        SimpleTags.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId().toString());

                        Tasks.run(() -> Color.sendMessage(commandSender, "gems-reset-player"));
                        break;
                    default:
                        Tasks.run(() -> {
                            Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB and SQLite.");
                        });
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
