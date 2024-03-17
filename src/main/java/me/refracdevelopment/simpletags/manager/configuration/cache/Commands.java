package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;

import java.util.List;

public class Commands {

    public String TAGS_COMMAND_NAME;
    public List<String> TAGS_COMMAND_ALIASES;

    public String CREATE_COMMAND_NAME;
    public List<String> CREATE_COMMAND_ALIASES;

    public String DELETE_COMMAND_NAME;
    public List<String> DELETE_COMMAND_ALIASES;

    public String EDIT_COMMAND_NAME;
    public List<String> EDIT_COMMAND_ALIASES;

    public String LIST_COMMAND_NAME;
    public List<String> LIST_COMMAND_ALIASES;

    public String SET_COMMAND_NAME;
    public List<String> SET_COMMAND_ALIASES;

    public String RELOAD_COMMAND_NAME;
    public List<String> RELOAD_COMMAND_ALIASES;

    public String HELP_COMMAND_NAME;
    public List<String> HELP_COMMAND_ALIASES;

    public String VERSION_COMMAND_NAME;
    public List<String> VERSION_COMMAND_ALIASES;

    public String RESET_COMMAND_NAME;
    public List<String> RESET_COMMAND_ALIASES;

    public Commands() {
        loadConfig();
    }

    public void loadConfig() {
        TAGS_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("name");
        TAGS_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("aliases");

        CREATE_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.create.name");
        CREATE_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.create.aliases");

        DELETE_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.delete.name");
        DELETE_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.delete.aliases");

        EDIT_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.edit.name");
        EDIT_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.edit.aliases");

        LIST_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.list.name");
        LIST_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.list.aliases");

        SET_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.set.name");
        SET_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.set.aliases");

        RELOAD_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.reload.name");
        RELOAD_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.reload.aliases");

        HELP_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.help.name");
        HELP_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.help.aliases");

        VERSION_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.version.name");
        VERSION_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.version.aliases");

        RESET_COMMAND_NAME = SimpleTags.getInstance().getCommandsFile().getString("subcommands.reset.name");
        RESET_COMMAND_ALIASES = SimpleTags.getInstance().getCommandsFile().getStringList("subcommands.reset.aliases");
    }
}