package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.utilities.Permissions;

public class VersionCommand extends RoseCommand {

    public VersionCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final LocaleManager locale = rosePlugin.getManager(LocaleManager.class);

        String baseColor = locale.getLocaleMessage("base-command-color");
        locale.sendCustomMessage(context.getSender(), baseColor + "Running " + locale.getLocaleMessage("prefix") + baseColor + " v" + rosePlugin.getDescription().getVersion());
        locale.sendCustomMessage(context.getSender(), baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + rosePlugin.getDescription().getAuthors().get(0));
        locale.sendSimpleMessage(context.getSender(), "base-command-help", StringPlaceholders.single("cmd", this.parent.getName()));
    }

    @Override
    protected String getDefaultName() {
        return "version";
    }

    @Override
    public String getDescriptionKey() {
        return "command-version-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.VERSION_COMMAND;
    }
}