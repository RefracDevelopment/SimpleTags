package me.refracdevelopment.simpletags.menu;

import de.tr7zw.nbtapi.NBTItem;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.menu.PaginatedMenu;
import me.refracdevelopment.simpletags.utilities.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class TagsMenu extends PaginatedMenu {

    public TagsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return RyMessageUtils.translate(SimpleTags.getInstance().getMenus().TAGS_TITLE.replace("%total-tags%", String.valueOf(SimpleTags.getInstance().getTagManager().getLoadedTags().size())));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ProfileData profile = plugin.getProfileManager().getProfile(player.getUniqueId()).getData();

        if (e.getCurrentItem() == null)
            return;

        if (e.getCurrentItem().getItemMeta() == null)
            return;

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.name"))) {
            if (page != 0) {
                page = page - 1;
                super.open();
            }

            return;
        } else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.name"))) {
            if (!((index + 1) >= plugin.getTagManager().getLoadedTags().size())) {
                page = page + 1;
                super.open();
            }

            return;
        } else if (e.getCurrentItem().getType().equals(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.material")).parseMaterial())) {
            player.closeInventory();
            return;
        }

        Tag tag = null;

        NBTItem nbtItem = new NBTItem(e.getCurrentItem());

        if (nbtItem.hasTag("tag-name"))
            tag = plugin.getTagManager().getCachedTag(nbtItem.getString("tag-name"));

        if (tag == null)
            return;

        if (!player.hasPermission("simpletags.tag." + tag.getConfigName())) {
            RyMessageUtils.sendPluginMessage(player, "tag-not-owned", Placeholders.setPlaceholders(player));
            player.closeInventory();
            return;
        }

        // Check if their tag is the same if not set it to the new tag
        if (!profile.getTag().equalsIgnoreCase(tag.getConfigName()) || profile.getTag().isEmpty()) {
            profile.setTag(tag.getConfigName());
            profile.setTagPrefix(tag.getTagPrefix());
            Tasks.runAsync(() -> profile.save(player));
            RyMessageUtils.sendPluginMessage(player, "tag-updated", Placeholders.setPlaceholders(player));
            player.closeInventory();
            return;
        }

        profile.setTag("");
        profile.setTagPrefix("");
        Tasks.runAsync(() -> profile.save(player));
        RyMessageUtils.sendPluginMessage(player, "tag-reset", Placeholders.setPlaceholders(player));
        player.closeInventory();
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        List<Tag> tags = plugin.getTagManager().getLoadedTags();

        if (!tags.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;

                if (index >= tags.size())
                    break;

                if (tags.get(index) != null) {
                    if (plugin.getSettings().REQUIRE_PERMISSION && !playerMenuUtility.getOwner().hasPermission("simpletags.tag." + tags.get(index).getConfigName()))
                        continue;

                    inventory.addItem(tags.get(index).toItemStack(playerMenuUtility.getOwner()));
                }
            }
        }
    }
}