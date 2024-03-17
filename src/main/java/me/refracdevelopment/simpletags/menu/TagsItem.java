package me.refracdevelopment.simpletags.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.ReflectionUtils;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.utilities.ItemBuilder;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class TagsItem {

    private final XMaterial material;
    private final String name, skullOwner;
    private final boolean skulls, headDatabase, customData;
    private final int data, customModelData;
    private final List<String> lore;

    public TagsItem() {
        this.material = Utilities.getMaterial(SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.material"));
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
        this.name = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.name");
        this.skullOwner = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getString("tag-item.skullOwner");
        this.data = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getInt("tag-item.data");
        this.customModelData = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getInt("tag-item.customModelData");
        this.lore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.lore");
    }

    public ItemStack getItem(Player player, Tag tag) {
        ItemBuilder item;
        if (tag.getMaterial() == null || tag.getMaterial().isEmpty())
            item = new ItemBuilder(getMaterial().parseMaterial());
        else
            item = new ItemBuilder(Utilities.getMaterial(tag.getMaterial()).parseMaterial());

        if (isHeadDatabase()) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            item = new ItemBuilder(api.getItemHead(getSkullOwner()));
        } else if (isSkulls()) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(getSkullOwner()));
            item = new ItemBuilder(api.getItemStack());
        } else if (isCustomData()) {
            if (ReflectionUtils.MINOR_NUMBER >= 14)
                item.setCustomModelData(getCustomModelData());
            else
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
        }

        ItemBuilder finalItem = item;

        finalItem.setDurability(getData());

        if (!isSkulls() && !isHeadDatabase())
            finalItem.setSkullOwner(getSkullOwner());

        return makeItem(finalItem, player, tag);
    }

    private ItemStack makeItem(ItemBuilder item, Player player, Tag tag) {
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        item.setName(Color.translate(player, getName().replace("%tag-name%", tag.getTagName())));

        if (player.hasPermission("simpletags.tag." + tag.getConfigName())) {
            if (!profile.getTag().equals(tag.getConfigName())) {
                List<String> lore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.lore");
                lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            } else {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                item.setItemFlags(ItemFlag.HIDE_ENCHANTS);
                List<String> equippedLore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.equipped-lore");
                equippedLore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
            }
        } else {
            List<String> noPermissionLore = SimpleTags.getInstance().getMenus().TAGS_ITEMS.getStringList("tag-item.no-permission-lore");
            noPermissionLore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix()))));
        }

        NBTItem nbtItem = new NBTItem(item.toItemStack());
        nbtItem.setString("tag-name", tag.getConfigName());
        nbtItem.applyNBT(item.toItemStack());

        return item.toItemStack();
    }
}