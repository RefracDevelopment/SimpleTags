package me.refracdevelopment.simpletags.menu;

import de.tr7zw.nbtapi.NBTItem;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.manager.configuration.LocaleManager;
import me.refracdevelopment.simpletags.manager.configuration.cache.Menus;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.menu.PaginatedMenu;
import me.refracdevelopment.simpletags.utilities.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class TagsMenu extends PaginatedMenu {

    public TagsMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Color.translate(Menus.TAGS_TITLE.replace("%total-tags%", String.valueOf(SimpleTags.getInstance().getTagManager().getLoadedTags().size())));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ProfileData profile = plugin.getProfileManager().getProfile(player.getUniqueId()).getData();
        final LocaleManager locale = plugin.getManager(LocaleManager.class);

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getItemMeta() == null) return;

        if (e.getCurrentItem().getType().equals(Utilities.getMaterial(Menus.TAGS_ITEMS.getString("left.material")).parseMaterial())) {
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                if (page != 0) {
                    page = page - 1;
                    super.open();
                }
            }
        } else if (e.getCurrentItem().getType().equals(Utilities.getMaterial(Menus.TAGS_ITEMS.getString("right.material")).parseMaterial())) {
            if (!((index + 1) >= plugin.getTagManager().getLoadedTags().size())) {
                page = page + 1;
                super.open();
            }
        } else if (e.getCurrentItem().getType().equals(Utilities.getMaterial(Menus.TAGS_ITEMS.getString("close.material")).parseMaterial())) {
            player.closeInventory();
        }

        if (e.getCurrentItem().getType().equals(Utilities.getMaterial(Menus.TAGS_ITEMS.getString("tag-item.material")).parseMaterial())) {
            Tag tag;

            NBTItem nbtItem = new NBTItem(e.getCurrentItem());
            if (nbtItem.hasTag("tag-name")) {
                tag = plugin.getTagManager().getCachedTag(nbtItem.getString("tag-name"));
            } else {
                tag = plugin.getTagManager().findByName(e.getCurrentItem().getItemMeta().getDisplayName()).get();
            }

            if (!player.hasPermission("simpletags.tag." + tag.getConfigName()) && !player.hasPermission("simpletags.tag.*")) {
                locale.sendMessage(player, "tag-not-owned", Placeholders.setPlaceholders(player));
                player.closeInventory();
                return;
            }

            // Check if their tag is the same if not set it to the new tag
            if (!profile.getTag().equalsIgnoreCase(tag.getConfigName()) || profile.getTag().equalsIgnoreCase("")) {
                profile.setTag(tag.getConfigName());
                profile.setTagPrefix(tag.getTagPrefix());
                locale.sendMessage(player, "tag-updated", Placeholders.setPlaceholders(player));
                player.closeInventory();
                return;
            }

            profile.setTag("");
            profile.setTagPrefix("");
            locale.sendMessage(player, "tag-reset", Placeholders.setPlaceholders(player));
            player.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        List<Tag> tags = new ArrayList<>(plugin.getTagManager().getLoadedTags());

        if (tags != null && !tags.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= tags.size()) break;
                if (tags.get(index) != null) {
                    inventory.addItem(tags.get(index).toItemStack(playerMenuUtility.getOwner(), tags.get(index).getConfigName()));
                }
            }
        }
    }
}