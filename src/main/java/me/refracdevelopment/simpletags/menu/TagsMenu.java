package me.refracdevelopment.simpletags.menu;

import de.tr7zw.nbtapi.NBTItem;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Placeholders;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simpletags.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simpletags.utilities.exceptions.MenuManagerNotSetupException;
import me.refracdevelopment.simpletags.utilities.paginated.PaginatedMenu;
import me.refracdevelopment.simpletags.utilities.paginated.PlayerMenuUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class TagsMenu extends PaginatedMenu {

    public TagsMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);
    }

    @Override
    public List<Tag> dataToItems() {
        return SimpleTags.getInstance().getTagManager().getLoadedTags();
    }

    @Nullable
    @Override
    public HashMap<Integer, ItemStack> getCustomMenuBorderItems() {
        return null;
    }

    @Override
    public Component getMenuName() {
        return RyMessageUtils.translate(SimpleTags.getInstance().getMenus().TAGS_TITLE.replace("%total-tags%", String.valueOf(SimpleTags.getInstance().getTagManager().getLoadedTags().size())));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        if (event.getCurrentItem().getItemMeta() == null)
            return;

        if (event.getCurrentItem().getType().equals(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.material")).parseMaterial()) && page != 0) {
            try {
                prevPage();
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                RyMessageUtils.sendPluginError("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
            return;
        } else if (event.getCurrentItem().getType().equals(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.material")).parseMaterial()) && !((page + 1) * maxItemsPerPage >= dataToItems().size())) {
            try {
                nextPage();
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                RyMessageUtils.sendPluginError("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
            return;
        } else if (event.getCurrentItem().getType().equals(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.material")).parseMaterial())) {
            player.closeInventory();
            return;
        }

        Tag tag = null;

        NBTItem nbtItem = new NBTItem(event.getCurrentItem());

        if (nbtItem.hasTag("tag-name"))
            tag = SimpleTags.getInstance().getTagManager().getCachedTag(nbtItem.getString("tag-name"));

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
}