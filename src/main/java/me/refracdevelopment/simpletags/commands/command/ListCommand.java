package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.utilities.Permissions;
import org.bukkit.entity.Player;

public class ListCommand extends RoseCommand {

    public ListCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console");
            return;
        }

        Player player = (Player) context.getSender();

        // Show list of all available tags
        locale.sendCustomMessage(player, "&7-------------------------------------");
        SimpleTags.getInstance().getTagManager().getLoadedTags().forEach(tag -> {
            locale.sendCustomMessage(player, "&e" + tag.getConfigName() + "&7(" + tag.getTagName() + "&7) &7- " + tag.getTagPrefix());
        });
        locale.sendCustomMessage(player, "&7-------------------------------------");
    }

    @Override
    protected String getDefaultName() {
        return "list";
    }

    @Override
    public String getDescriptionKey() {
        return "command-list-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.LIST_COMMAND;
    }
}