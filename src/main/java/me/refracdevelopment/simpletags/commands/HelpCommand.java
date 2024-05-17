package me.refracdevelopment.simpletags.commands;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleTags.getInstance().getCommands().HELP_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleTags.getInstance().getCommands().HELP_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleTags.getInstance().getLocaleFile().getString("command-help-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        RyMessageUtils.sendPluginMessage(commandSender, "command-help-title");
        SimpleTags.getInstance().getCommandManager().getCommands().forEach(command -> {
            StringPlaceholders placeholders;

            if (!command.getSyntax().isEmpty()) {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleTags.getInstance().getCommands().TAGS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("args", command.getSyntax())
                        .add("desc", command.getDescription())
                        .build();
                RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description", placeholders);
            } else {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleTags.getInstance().getCommands().TAGS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("desc", command.getDescription())
                        .build();
                RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description-no-args", placeholders);
            }
        });
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}