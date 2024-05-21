package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListCommand extends SubCommand {

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().LIST_COMMAND_NAME;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().LIST_COMMAND_ALIASES;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-list-description");
    }

    /**
     * @return An example of how to use the subcommand
     */
    @Override
    public String getSyntax() {
        return "";
    }

    /**
     * @param commandSender The thing that ran the command
     * @param args          The args passed into the command when run
     */
    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the player has permission to use this command
        if (!(commandSender instanceof Player player)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-console");
            return;
        }

        if (!commandSender.hasPermission(Permissions.LIST_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        if (SimpleTags.getInstance().getTagManager().getLoadedTags().stream().noneMatch(tag -> player.hasPermission("simpletags.tag." + tag.getConfigName()))) {
            RyMessageUtils.sendPluginMessage(player, "no-available-tags");
            return;
        }

        // Show list of all available tags
        RyMessageUtils.sendPlayer(player, "&7-------------------------------------");
        SimpleTags.getInstance().getTagManager().getLoadedTags().forEach(tag -> {
            RyMessageUtils.sendPlayer(player, "&e" + tag.getConfigName() + "&7(" + tag.getTagName() + "&7) &7- " + tag.getTagPrefix());
        });
        RyMessageUtils.sendPlayer(player, "&7-------------------------------------");
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