package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
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
        return "[player] <identifier>";
    }

    /**
     * @param commandSender The thing that ran the command
     * @param args   The args passed into the command when run
     */
    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console");
            return;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission(Permissions.SET_COMMAND)) {
            Color.sendMessage(player, "no-permission");
            return;
        }

        if (args.length <= 1) {
            return;
        }

        if (args.length == 2) {
            String configName = args[1];

            if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) {
                Color.sendMessage(player, "invalid-tag", Placeholders.setPlaceholders(player));
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
                Color.sendMessage(player, "tag-not-owned");
                return;
            }

            profile.setTag(configName);
            profile.setTagPrefix(tag.getTagPrefix());
            Tasks.runAsync(profile::save);

            Color.sendMessage(player, "tag-updated", placeholders);
            return;
        }

        if (args.length == 3) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            String configName = args[2];

            if (target.isOnline()) {
                if (!player.hasPermission(Permissions.SET_OTHER_COMMAND)) {
                    Color.sendMessage(player, "no-permission");
                    return;
                }

                ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
                Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addAll(Placeholders.setPlaceholders(player))
                        .add("player", target.getPlayer().getName())
                        .add("tag-name", tag.getTagName())
                        .add("tag-prefix", tag.getTagPrefix())
                        .build();

                profile.setTag(configName);
                profile.setTagPrefix(tag.getTagPrefix());
                Tasks.runAsync(profile::save);

                Color.sendMessage(player, "tag-set", placeholders);
                Color.sendMessage(profile.getPlayer(), "tag-updated", placeholders);
            } else {
                Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addAll(Placeholders.setPlaceholders(player))
                        .add("player", target.getName())
                        .add("tag-name", tag.getTagName())
                        .add("tag-prefix", tag.getTagPrefix())
                        .build();

                Tasks.runAsync(() -> Utilities.saveOfflinePlayer(target.getUniqueId(), tag.getTagName(), tag.getTagPrefix()));

                Color.sendMessage(player, "tag-set", placeholders);
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
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> {
            names.add(p.getName());
        });

        if (args.length == 2) {
            return names;
        }
        return null;
    }
}