package me.refracdevelopment.simpletags.utilities.paginated;

import lombok.Getter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simpletags.utilities.exceptions.MenuManagerNotSetupException;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * A class extending the functionality of the regular Menu, but making it Paginated
 */
@Getter
public abstract class PaginatedMenu extends Menu {

    //The items being paginated
    protected List<Object> data;

    //Keep track of what page the menu is on
    protected int page = 0;
    //28 is max items because with the border set below,
    //28 empty slots are remaining.
    protected int maxItemsPerPage = 28;

    public PaginatedMenu(PlayerMenuUtil playerMenuUtil) {
        super(playerMenuUtil);
    }

    /**
     * @return A list of ItemStacks that you want to be placed in the menu. This is the data that will be paginated
     * You can also use this as a way to convert your data to items if you need to
     */
    public abstract List<Tag> dataToItems();

    /**
     * @return A hashmap of items you want to be placed in the paginated menu border. This will override any items already placed by default. Key = slot, Value = Item
     */
    public abstract HashMap<Integer, ItemStack> getCustomMenuBorderItems();

    public void addMenuBorder() {
        inventory.setItem(48, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.material")).parseMaterial(), SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.name")));

        inventory.setItem(49, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.material")).parseMaterial(), SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.name")));

        inventory.setItem(50, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.material")).parseMaterial(), SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.name")));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        //place the custom items if they exist
        if (getCustomMenuBorderItems() != null) {
            getCustomMenuBorderItems().forEach((integer, itemStack) -> inventory.setItem(integer, itemStack));
        }
    }

    /**
     * Place each item in the paginated menu, automatically coded by default but override if you want custom functionality. Calls the loopCode() method you define for each item returned in the getData() method
     */
    @Override
    public void setMenuItems() {
        addMenuBorder();

        //add the items to the inventory based on the current page and max items per page
        List<Tag> items = dataToItems();
        for (int i = 0; i < maxItemsPerPage; i++) {
            int index = maxItemsPerPage * page + i;

            if (index >= items.size())
                break;

            if (items.get(index) != null) {
                if (SimpleTags.getInstance().getSettings().REQUIRE_PERMISSION && !player.hasPermission("simpletags.tag." + items.get(index).getConfigName()))
                    continue;

                inventory.addItem(items.get(index).toItemStack(player));
            }
        }

    }

    /**
     * @return true if successful, false if already on the first page
     */
    public boolean prevPage() throws MenuManagerException, MenuManagerNotSetupException {
        if (page == 0) {
            return false;
        } else {
            page = page - 1;
            reloadItems();
            return true;
        }
    }

    /**
     * @return true if successful, false if already on the last page
     */
    public boolean nextPage() throws MenuManagerException, MenuManagerNotSetupException {
        if (!((page + 1) * maxItemsPerPage >= dataToItems().size())) {
            page = page + 1;
            reloadItems();
            return true;
        } else {
            return false;
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}