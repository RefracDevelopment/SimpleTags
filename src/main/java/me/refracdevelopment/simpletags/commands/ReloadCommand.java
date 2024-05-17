package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().RELOAD_COMMAND_NAME;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().RELOAD_COMMAND_ALIASES;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-reload-description");
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
        if (!commandSender.hasPermission(Permissions.RELOAD_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        SimpleTags.getInstance().getPaperLib().scheduling().cancelGlobalTasks();
        reloadFiles();
        SimpleTags.getInstance().getTagManager().loadTags();
        SimpleTags.getInstance().getTagManager().updateTags();
        RyMessageUtils.sendPluginMessage(commandSender, "command-reload-success");
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

    private void reloadFiles() {
        SimpleTags.getInstance().getConfigFile().reload();
        SimpleTags.getInstance().getTagsFile().reload();
        SimpleTags.getInstance().getMenusFile().reload();
        SimpleTags.getInstance().getLocaleFile().reload();
        SimpleTags.getInstance().getCommandsFile().reload();

        SimpleTags.getInstance().getSettings().loadConfig();
        SimpleTags.getInstance().getTags().loadConfig();
        SimpleTags.getInstance().getMenus().loadConfig();
        SimpleTags.getInstance().getCommands().loadConfig();

        RyMessageUtils.sendConsole(true, "&aReloaded all files.");
    }
}