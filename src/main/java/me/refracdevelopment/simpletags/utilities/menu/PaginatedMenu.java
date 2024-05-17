package me.refracdevelopment.simpletags.utilities.menu;

import lombok.Getter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;

/**
 * A class extending the functionality of the regular Menu, but making it Paginated
 */
@Getter
public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {
        inventory.setItem(48, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.material")).parseMaterial(), RyMessageUtils.translate(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("left.name"))));

        inventory.setItem(49, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.material")).parseMaterial(), RyMessageUtils.translate(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("close.name"))));

        inventory.setItem(50, makeItem(Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.material")).parseMaterial(), RyMessageUtils.translate(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("right.name"))));

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

}