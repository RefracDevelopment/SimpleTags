package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.configuration.LocaleManager;
import me.refracdevelopment.simpletags.manager.configuration.cache.ConfigFile;
import me.refracdevelopment.simpletags.manager.configuration.cache.Tags;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import org.bukkit.entity.Player;

public class DeleteCommand extends RoseCommand {

    public DeleteCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, String configName) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console");
            return;
        }

        Player player = (Player) context.getSender();

        // Delete a tag
        if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) {
            locale.sendMessage(player, "invalid-tag");
            return;
        }

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(context.getSender()))
                .add("tag-name", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagName())
                .add("tag-prefix", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagPrefix())
                .build();

        ConfigFile tagsFile = SimpleTags.getInstance().getTagsFile();
        tagsFile.set("tags." + configName, null);
        tagsFile.save();
        tagsFile.load();
        Tags.loadConfig();
        SimpleTags.getInstance().getTagManager().removeTag(SimpleTags.getInstance().getTagManager().getCachedTag(configName));
        SimpleTags.getInstance().getTagManager().updateTags();

        locale.sendMessage(player, "tag-deleted", placeholders);
    }

    @Override
    protected String getDefaultName() {
        return "delete";
    }

    @Override
    public String getArgumentsString() {
        return "<identifier>";
    }

    @Override
    public String getDescriptionKey() {
        return "command-delete-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.DELETE_COMMAND;
    }
}