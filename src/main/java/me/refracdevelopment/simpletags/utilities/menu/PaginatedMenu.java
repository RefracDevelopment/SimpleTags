package me.refracdevelopment.simpletags.utilities.menu;

import me.refracdevelopment.simpletags.utilities.Utilities;
import org.bukkit.ChatColor;

/**
 * A class extending the functionality of the regular Menu, but making it Paginated
 */
public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {
        inventory.setItem(48, makeItem(Utilities.getMaterial("DARK_OAK_BUTTON").parseMaterial(), ChatColor.GREEN + "Left"));

        inventory.setItem(49, makeItem(Utilities.getMaterial("BARRIER").parseMaterial(), ChatColor.DARK_RED + "Close"));

        inventory.setItem(50, makeItem(Utilities.getMaterial("DARK_OAK_BUTTON").parseMaterial(), ChatColor.GREEN + "Right"));

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
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}