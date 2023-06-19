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
import me.refracdevelopment.simpletags.data.Tag;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import org.bukkit.entity.Player;

public class CreateCommand extends RoseCommand {

    public CreateCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, String configName, String tagName, String tagPrefix) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console");
            return;
        }

        Player player = (Player) context.getSender();

        // Create a tag
        if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) != null) {
            locale.sendMessage(player, "tag-already-exists");
            return;
        }

        ConfigFile tagsFile = SimpleTags.getInstance().getTagsFile();
        tagsFile.set("tags." + configName + ".name", tagName);
        tagsFile.set("tags." + configName + ".prefix", tagPrefix);
        tagsFile.save();
        tagsFile.load();
        Tags.loadConfig();
        SimpleTags.getInstance().getTagManager().addTag(new Tag(configName, tagName, tagPrefix));

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(context.getSender()))
                .add("tag-name", tagName)
                .add("tag-prefix", SimpleTags.getInstance().getTagManager().getCachedTag(configName).getTagPrefix())
                .build();

        locale.sendMessage(player, "tag-created", placeholders);
    }

    @Override
    protected String getDefaultName() {
        return "create";
    }

    @Override
    public String getArgumentsString() {
        return "<identifier> <name> <prefix>";
    }

    @Override
    public String getDescriptionKey() {
        return "command-create-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.CREATE_COMMAND;
    }
}