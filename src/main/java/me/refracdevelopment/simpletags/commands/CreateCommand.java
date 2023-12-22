package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.configuration.ConfigFile;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {

    /**
     * @return The name of the subcommand
     */
    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().CREATE_COMMAND_NAME;
    }

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().CREATE_COMMAND_ALIASES;
    }

    /**
     * @return A description of what the subcommand does to be displayed
     */
    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-create-description");
    }

    /**
     * @return An example of how to use the subcommand
     */
    @Override
    public String getSyntax() {
        return "<identifier> <name> <prefix>";
    }

    /**
     * @param commandSender The thing that ran the command
     * @param args          The args passed into the command when run
     */
    @Override
    public void perform(CommandSender commandSender, String[] args) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console");
            return;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission(Permissions.CREATE_COMMAND)) {
            Color.sendMessage(player, "no-permission");
            return;
        }

        if (args.length <= 3) {
            return;
        }

        // Create a tag
        String configName = args[1];
        String tagName = args[2];
        String tagPrefix = args[3];

        if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) != null) {
            Color.sendMessage(player, "tag-already-exists");
            return;
        }

        ConfigFile tagsFile = SimpleTags.getInstance().getTagsFile();
        tagsFile.set("tags." + configName + ".name", tagName);
        tagsFile.set("tags." + configName + ".prefix", tagPrefix);
        tagsFile.save();
        tagsFile.reload();
        SimpleTags.getInstance().getTags().loadConfig();

        SimpleTags.getInstance().getTagManager().addTag(new Tag(configName, tagName, tagPrefix));

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("tag-name", tagName)
                .add("tag-prefix", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagPrefix())
                .build();

        Color.sendMessage(player, "tag-created", placeholders);
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