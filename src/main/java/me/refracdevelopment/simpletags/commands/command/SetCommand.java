package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetCommand extends SubCommand {

    @RoseExecutable
    public void execute(CommandContext context, @Optional OfflinePlayer target, @Optional String configName) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);


        Player player = null;

        // Make sure the sender is a player.
        if ((context.getSender() instanceof Player)) {
            
            player = (Player) context.getSender();

            // Set a player tag
            if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) {
                locale.sendMessage(player, "invalid-tag", Placeholders.setPlaceholders(player));
                return;
            }

            if (target == null) {
                ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();
                Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addAll(Placeholders.setPlaceholders(context.getSender()))
                        .add("player", player.getName())
                        .add("tag-name", tag.getTagName())
                        .add("tag-prefix", tag.getTagPrefix())
                        .build();

                if (!player.hasPermission("simpletags.tag." + configName)) {
                    locale.sendMessage(player, "tag-not-owned");
                    return;
                }

                profile.setTag(configName);
                profile.setTagPrefix(tag.getTagPrefix());
                Tasks.runAsync(rosePlugin, profile::save);
                locale.sendMessage(player, "tag-updated", placeholders);
                return;
            }

            if (!player.hasPermission(Permissions.SET_OTHER_COMMAND)) {
                locale.sendMessage(player, "no-permission");
                return;
            }
        }

        if (target == null) {
            locale.sendCustomMessage(context.getSender(), "You need to specify a target"); // maybe translate?
            return;
        }

        if (target.isOnline()) {
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();
            Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target.getPlayer().getName())
                    .add("tag-name", tag.getTagName())
                    .add("tag-prefix", tag.getTagPrefix())
                    .build();

            profile.setTag(configName);
            profile.setTagPrefix(tag.getTagPrefix());
            Tasks.runAsync(rosePlugin, profile::save);
            if(player != null) {
                locale.sendMessage(player, "tag-set", placeholders);
            }
            else {
                locale.sendMessage(context.getSender(), "tag-set", placeholders);
            }
            locale.sendMessage(profile.getPlayer(), "tag-updated", placeholders);
        } else {
            Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target.getName())
                    .add("tag-name", tag.getTagName())
                    .add("tag-prefix", tag.getTagPrefix())
                    .build();

            Tasks.runAsync(rosePlugin, () -> {
                Utilities.saveOfflinePlayer(target.getUniqueId(), target.getName(), tag.getTagName(), tag.getTagPrefix());
            });
            if(player != null) {
                locale.sendMessage(player, "tag-set", placeholders);
            }
            else {
                locale.sendMessage(context.getSender(), "tag-set", placeholders);
            }
        }
    }

    @Override
    protected String getDefaultName() {
        return "set";
    }

    @Override
    public String getArgumentsString() {
        return "[player] <identifier>";
    }

    @Override
    public String getDescriptionKey() {
        return "command-set-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.SET_COMMAND;
    }

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return null;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * @return An example of how to use the subcommand
     */
    @Override
    public String getSyntax() {
        return "[player] <identifier>";
    }

    /**
     * @param sender The thing that ran the command
     * @param args   The args passed into the command when run
     */
    @Override
    public void perform(CommandSender sender, String[] args) {

    }

    /**
     * @param player The player who ran the command
     * @param args   The args passed into the command when run
     * @return A list of arguments to be suggested for autocomplete
     */
    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}