package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.config.Menus;
import me.refracdevelopment.simpletags.config.Tags;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.utilities.Permissions;

public class ReloadCommand extends RoseCommand {

    public ReloadCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        SimpleTags.getInstance().getTagsFile().load();
        SimpleTags.getInstance().getMenusFile().load();
        Tags.loadConfig();
        Menus.loadConfig();
        SimpleTags.getInstance().getTagManager().loadTags();
        SimpleTags.getInstance().getTagManager().updateTags();
        locale.sendMessage(context.getSender(), "command-reload-success");
    }

    @Override
    protected String getDefaultName() {
        return "reload";
    }

    @Override
    public String getDescriptionKey() {
        return "command-reload-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.RELOAD_COMMAND;
    }
}