package me.refracdevelopment.simpletags.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.data.ProfileData;
import me.refracdevelopment.simpletags.data.Tag;
import me.refracdevelopment.simpletags.manager.LocaleManager;
import me.refracdevelopment.simpletags.utilities.Permissions;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetCommand extends RoseCommand {

    public SetCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, String target, String configName) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console");
            return;
        }

        Player player = (Player) context.getSender();

        // Set a player tag
        if (SimpleTags.getInstance().getTagManager().getCachedTag(configName) == null) return;
        if (Bukkit.getPlayer(target) != null) {
            ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(Bukkit.getPlayer(target).getUniqueId()).getData();
            Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target)
                    .add("tag-name", tag.getTagName())
                    .add("tag-prefix", tag.getTagPrefix())
                    .build();

            profile.setTag(configName);
            profile.setTagPrefix(tag.getTagPrefix());
            locale.sendMessage(player, "tag-set", placeholders);
            locale.sendMessage(profile.getPlayer(), "tag-updated", placeholders);
        } else if (Bukkit.getOfflinePlayer(target) != null) {
            Tag tag = SimpleTags.getInstance().getTagManager().getCachedTag(configName);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target)
                    .add("tag-name", tag.getTagName())
                    .add("tag-prefix", tag.getTagPrefix())
                    .build();

            SimpleTags.getInstance().getPlayerMapper().saveOfflinePlayer(Bukkit.getOfflinePlayer(target).getUniqueId(), target, tag.getConfigName(), tag.getTagPrefix());
            locale.sendMessage(player, "tag-set", placeholders);
        }
    }

    @Override
    protected String getDefaultName() {
        return "set";
    }

    @Override
    public String getArgumentsString() {
        return "player identifier";
    }

    @Override
    public String getDescriptionKey() {
        return "command-set-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.SET_COMMAND;
    }
}