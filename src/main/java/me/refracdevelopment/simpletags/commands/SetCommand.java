package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
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

public class SetCommand extends SubCommand {

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().SET_COMMAND_NAME;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().SET_COMMAND_ALIASES;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-set-description");
    }

    /**
     * @return An example of how to use the subcommand
     */
    @Override
    public String getSyntax() {
        return "<identifier> [player]";
    }

    /**
     * @param commandSender The thing that ran the command
     * @param args          The args passed into the command when run
     */
    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (args.length <= 1) {
            RyMessageUtils.sendPluginMessage(commandSender, "usage", StringPlaceholders.builder()
                    .add("cmd", getName()).add("args", getSyntax()).build());
            return;
        }

        if (args.length == 2) {
            if (!(commandSender instanceof Player player)) {
                RyMessageUtils.sendPluginMessage(commandSender, "no-console");
                return;
            }

            String configName = args[1];

            if (!player.hasPermission(Permissions.SET_COMMAND)) {
                RyMessageUtils.sendPluginMessage(player, "no-permission");
                return;
            }

            if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) {
                RyMessageUtils.sendPluginMessage(commandSender, "invalid-tag", Placeholders.setPlaceholders(player));
                return;
            }

            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
            Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("player", player.getName())
                    .add("tag-name", tag.getTagName())
                    .add("tag-prefix", tag.getTagPrefix())
                    .build();

            if (!player.hasPermission("simpletags.tag." + configName)) {
                RyMessageUtils.sendPluginMessage(player, "tag-not-owned");
                return;
            }

            profile.setTag(configName);
            profile.setTagPrefix(tag.getTagPrefix());
            Tasks.runAsync(() -> profile.save(player));

            RyMessageUtils.sendPluginMessage(player, "tag-updated", placeholders);
            return;
        }

        if (args.length == 3) {
            String configName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);

            if (!commandSender.hasPermission(Permissions.SET_OTHER_COMMAND)) {
                RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
                return;
            }

            if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) {
                RyMessageUtils.sendPluginMessage(commandSender, "invalid-tag", Placeholders.setPlaceholders(commandSender));
                return;
            }

            if (target.isOnline()) {
                ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
                Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addAll(Placeholders.setPlaceholders(commandSender))
                        .add("player", target.getPlayer().getName())
                        .add("tag-name", tag.getTagName())
                        .add("tag-prefix", tag.getTagPrefix())
                        .build();

                profile.setTag(configName);
                profile.setTagPrefix(tag.getTagPrefix());
                Tasks.runAsync(() -> profile.save(target.getPlayer()));

                RyMessageUtils.sendPluginMessage(commandSender, "tag-set", placeholders);
                RyMessageUtils.sendPluginMessage(target.getPlayer(), "tag-updated", placeholders);
            } else if (target.hasPlayedBefore()) {
                Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addAll(Placeholders.setPlaceholders(commandSender))
                        .add("player", target.getName())
                        .add("tag-name", tag.getTagName())
                        .add("tag-prefix", tag.getTagPrefix())
                        .build();

                Tasks.runAsync(() -> Utilities.saveOfflinePlayer(target.getUniqueId().toString(), tag.getTagName(), tag.getTagPrefix()));

                RyMessageUtils.sendPluginMessage(commandSender, "tag-set", placeholders);
            }
        }
    }

    /**
     * @param player The player who ran the command
     * @param args   The args passed into the command when run
     * @return A list of arguments to be suggested for autocomplete
     */
    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> tagNames = SimpleTags.getInstance().getTagManager().getTagNames();
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> {
            names.add(p.getName());
        });

        if (args.length == 2) {
            return tagNames;
        }

        if (args.length == 3) {
            return names;
        }
        return null;
    }
}