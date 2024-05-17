package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VersionCommand extends SubCommand {

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().VERSION_COMMAND_NAME;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().VERSION_COMMAND_ALIASES;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-version-description");
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
        if (!commandSender.hasPermission(Permissions.VERSION_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        String baseColor = SimpleTags.getInstance().getLocaleFile().getString("base-command-color");
        RyMessageUtils.sendSender(commandSender, baseColor + "Running <gradient:#8A2387:#E94057:#F27121:0>" + SimpleTags.getInstance().getDescription().getName() + baseColor + " v" + SimpleTags.getInstance().getDescription().getVersion());
        RyMessageUtils.sendSender(commandSender, baseColor + "Plugin created by: <gradient:#41E0F0:#FF8DCE:0>" + SimpleTags.getInstance().getDescription().getAuthors().get(0));
        RyMessageUtils.sendPluginMessage(commandSender, "base-command-help", StringPlaceholders.of("cmd", SimpleTags.getInstance().getCommands().TAGS_COMMAND_NAME));
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