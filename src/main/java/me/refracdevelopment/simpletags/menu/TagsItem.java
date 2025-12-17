package me.refracdevelopment.simpletags.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import de.tr7zw.nbtapi.NBT;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class TagsItem {

    private final Tag tag;
    private String material;
    private String name, skullOwner;
    private boolean skulls, headDatabase, customData, itemsAdder;
    private int data, customModelData;
    private List<String> lore, equippedLore, noPermissionLore;

    public TagsItem(Tag tag) {
        ConfigurationSection section = SimpleTags.getInstance().getMenus().TAGS_ITEMS;
        
        this.tag = tag;

        this.material = section.getString("tag-item.material");
        this.name = section.getString("tag-item.name");
        this.skullOwner = section.getString("tag-item.skullOwner");
        this.data = section.getInt("tag-item.data");
        this.customModelData = section.getInt("tag-item.customModelData");
        this.lore = section.getStringList("tag-item.lore");
        this.equippedLore = section.getStringList("tag-item.equipped-lore");
        this.noPermissionLore = section.getStringList("tag-item.no-permission-lore");

        if (section.get("tag-item.head-database") != null)
            this.headDatabase = section.getBoolean("tag-item.headDatabase", false);
        else
            this.headDatabase = false;

        if (section.get("tag-item.skulls") != null)
            this.skulls = section.getBoolean("tag-item.skulls", false);
        else
            this.skulls = false;

        if (section.get("tag-item.customData") != null)
            this.customData = section.getBoolean("tag-item.customData", false);
        else
            this.customData = false;

        if (section.get("tag-item.itemsAdder") != null)
            this.itemsAdder = section.getBoolean("tag-item.itemsAdder", false);
        else
            this.itemsAdder = false;
    }

    public ItemStack getItem(Player player, Tag tag) {
        ItemBuilder item;

        if (tag.getMaterial() == null || tag.getMaterial().isEmpty())
            item = new ItemBuilder(Utilities.getMaterial(getMaterial()).get());
        else
            item = new ItemBuilder(Utilities.getMaterial(tag.getMaterial()).get());

        if (isHeadDatabase()) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            item = new ItemBuilder(api.getItemHead(getSkullOwner()));
        } else if (isSkulls()) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(getSkullOwner()));
            item = new ItemBuilder(api.getItemStack());
        } else if (isCustomData()) {
            item.setCustomModelData(getCustomModelData());
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
                lore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            } else {
                // Add Glow effect for equipped tag
                finalItem.addEnchant(XEnchantment.POWER.get(), 1);

                // Remove additional tooltip lore
                finalItem.setItemFlags(XItemFlag.HIDE_ENCHANTS.get());
                finalItem.setItemFlags(XItemFlag.HIDE_ADDITIONAL_TOOLTIP.get());

                equippedLore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            }
        } else {
            noPermissionLore.forEach(s -> finalItem.addLoreLine(RyMessageUtils.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
        }

        if (!isSkulls() && !isHeadDatabase())
            finalItem.setSkullOwner(getSkullOwner());

        finalItem.setDurability(getData());

        NBT.modify(finalItem.toItemStack(), nbt -> {
            nbt.setString("tag-name", tag.getConfigName());
        });

        return finalItem.toItemStack();
    }

}