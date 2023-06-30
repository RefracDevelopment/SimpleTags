package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.config.ConfigFile;
import me.refracdevelopment.simpletags.config.Tags;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import org.bukkit.entity.Player;

public class EditCommand extends RoseCommand {

    public EditCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, String configName, String tagPrefix) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console");
            return;
        }

        Player player = (Player) context.getSender();

        // Edit a tag
        if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) return;

        ConfigFile tagsFile = SimpleTags.getInstance().getTagsFile();
        tagsFile.set("tags." + configName + ".prefix", tagPrefix);
        tagsFile.save();
        tagsFile.load();
        Tags.loadConfig();
        SimpleTags.getInstance().getTagManager().updateTags();

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(context.getSender()))
                .add("tag-name", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagName())
                .add("tag-prefix", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagPrefix())
                .build();

        locale.sendMessage(player, "tag-edited", placeholders);
    }

    @Override
    public String getArgumentsString() {
        return "<identifier> <prefix>";
    }

    @Override
    protected String getDefaultName() {
        return "edit";
    }

    @Override
    public String getDescriptionKey() {
        return "command-edit-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.EDIT_COMMAND;
    }
}