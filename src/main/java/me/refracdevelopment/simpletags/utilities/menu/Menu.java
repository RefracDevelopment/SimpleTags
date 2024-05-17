package me.refracdevelopment.simpletags.utilities.menu;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.ItemBuilder;
import me.refracdevelopment.simpletags.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Defines the behavior and attributes of all menus in our plugin
 */
public abstract class Menu implements InventoryHolder {

    protected SimpleTags plugin = SimpleTags.getInstance();
    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(Utilities.getMaterial("GRAY_STAINED_GLASS_PANE").parseMaterial(), "");

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack makeItem(Material material, String displayName, String... lore) {

        ItemBuilder item = new ItemBuilder(material);
        item.setName(displayName);
        item.setLore(lore);

        return item.toItemStack();
    }

}