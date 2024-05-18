package me.refracdevelopment.simpletags.menu;

import ca.tweetzy.flight.comp.ReflectionUtils;
import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XEnchantment;
import de.tr7zw.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.ItemBuilder;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class TagsItem {

    private String material;
    private String name, skullOwner;
    private boolean skulls, headDatabase, customData, itemsAdder, oraxen;
    private int data, customModelData;
    private List<String> lore;

    public TagsItem() {
        this.material = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.material");
        this.name = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.name");
        this.skullOwner = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.skullOwner");
        this.data = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getInt("tag-item.data");
        this.customModelData = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getInt("tag-item.customModelData");
        this.lore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.lore");

        if (SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.head-database"))
            this.headDatabase = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.head-database", false);
        else
            this.headDatabase = false;

        if (SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.skulls"))
            this.skulls = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.skulls", false);
        else
            this.skulls = false;

        if (SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.customData"))
            this.customData = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.customData", false);
        else
            this.customData = false;

        if (SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.itemsAdder"))
            this.itemsAdder = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.itemsAdder", false);
        else
            this.itemsAdder = false;

        if (SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.oraxen"))
            this.oraxen = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getBoolean("tag-item.oraxen", false);
        else
            this.oraxen = false;
    }

    public ItemStack getItem(Player player, Tag tag) {
        ItemBuilder item;

        if (tag.getMaterial() == null || tag.getMaterial().isEmpty())
            item = new ItemBuilder(Utilities.getMaterial(getMaterial()).parseMaterial());
        else
            item = new ItemBuilder(Utilities.getMaterial(tag.getMaterial()).parseMaterial());

        if (isHeadDatabase()) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            item = new ItemBuilder(api.getItemHead(getSkullOwner()));
        } else if (isSkulls()) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(getSkullOwner()));
            item = new ItemBuilder(api.getItemStack());
        } else if (isCustomData()) {
            if (ReflectionUtils.supports(14))
                item.setCustomModelData(getCustomModelData());
            else
                RyMessageUtils.sendPluginError("An error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
        } else if (isItemsAdder()) {
            CustomStack api = CustomStack.getInstance(getMaterial());

            if (api != null)
                item = new ItemBuilder(api.getItemStack());
            else
                RyMessageUtils.sendConsole(true, "&cAn error occurred when trying to set an items adder custom item. Make sure you are typing the correct namespaced id.");
        }

        ItemBuilder finalItem = item;

        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        finalItem.setName(RyMessageUtils.translate(player, getName()
                .replace("%tag%", tag.getTagName())
                .replace("%tag-name%", tag.getTagName())
        ));

        if (player.hasPermission("simpletags.tag." + tag.getConfigName())) {
            if (!profile.getTag().equals(tag.getConfigName())) {
                List<String> lore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.lore");
                lore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            } else {
                finalItem.addEnchant(XEnchantment.POWER.getEnchant(), 1);
                finalItem.setItemFlags(ItemFlag.HIDE_ENCHANTS);
                List<String> equippedLore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.equipped-lore");
                equippedLore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            }
        } else {
            List<String> noPermissionLore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.no-permission-lore");
            noPermissionLore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
        }

        if (!isSkulls() && !isHeadDatabase())
            finalItem.setSkullOwner(getSkullOwner());

        finalItem.setDurability(getData());

        NBTItem nbtItem = new NBTItem(finalItem.toItemStack());
        nbtItem.setString("tag-name", tag.getConfigName());
        nbtItem.applyNBT(finalItem.toItemStack());

        return nbtItem.getItem();
    }

}