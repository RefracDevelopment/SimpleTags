package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.command.BaseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.menu.TagsMenu;
import org.bukkit.entity.Player;

public class TagsCommand extends BaseCommand {

    public TagsCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
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

        new TagsMenu(SimpleTags.getInstance().getMenuManager().getPlayerMenuUtility(player)).open();
    }
}