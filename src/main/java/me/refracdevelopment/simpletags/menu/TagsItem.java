package me.refracdevelopment.simpletags.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.Tag;
import me.refracdevelopment.simpletags.manager.configuration.cache.Menus;
import me.refracdevelopment.simpletags.player.data.ProfileData;
import me.refracdevelopment.simpletags.utilities.ItemBuilder;
import me.refracdevelopment.simpletags.utilities.Utilities;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
public class TagsItem {

    private final XMaterial material;
    private final String skullOwner;
    private final boolean skulls, headDatabase, customData;
    private final int data, customModelData;
    private final List<String> lore;

    public TagsItem() {
        this.material = Utilities.getMaterial(Menus.TAGS_ITEMS.getString("tag-item.material"));
        if (Menus.TAGS_ITEMS.getBoolean("tag-item.head-database")) {
            this.headDatabase = Menus.TAGS_ITEMS.getBoolean("tag-item.head-database", false);
        } else {
            this.headDatabase = false;
        }
        if (Menus.TAGS_ITEMS.getBoolean("tag-item.skulls")) {
            this.skulls = Menus.TAGS_ITEMS.getBoolean("tag-item.skulls", false);
        } else {
            this.skulls = false;
        }
        if (Menus.TAGS_ITEMS.getBoolean("tag-item.customData")) {
            this.customData = Menus.TAGS_ITEMS.getBoolean("tag-item.customData", false);
        } else {
            this.customData = false;
        }
        this.skullOwner = Menus.TAGS_ITEMS.getString("tag-item.skullOwner");
        this.data = Menus.TAGS_ITEMS.getInt("tag-item.data");
        this.customModelData = Menus.TAGS_ITEMS.getInt("tag-item.customModelData");
        this.lore = Menus.TAGS_ITEMS.getStringList("tag-item.lore");
    }

    public ItemStack getItem(Player player, Tag tag) {
        if (headDatabase) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemBuilder item = new ItemBuilder(api.getItemHead(this.skullOwner));

            return makeItem(item, player, tag);
        } else if (skulls) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(this.skullOwner));
            ItemBuilder item = new ItemBuilder(api.getItemStack());

            return makeItem(item, player, tag);
        } else if (customData) {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (NMSUtil.getVersionNumber() >= 14) {
                item.setCustomModelData(this.customModelData);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }

            return makeItem(item, player, tag);
        } else {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            return makeItem(item, player, tag);
        }
    }

    private ItemStack makeItem(ItemBuilder item, Player player, Tag tag) {
        ProfileData profile = SimpleTags.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        item.setName(Color.translate(player, Menus.TAGS_ITEMS.getString("tag-item.name")
                .replace("%tag-name%", tag.getTagName())
        ));

        if (player.hasPermission("simpletags.tag." + tag.getConfigName()) || player.hasPermission("simpletags.tag.*")) {
            if (!profile.getTag().equals(tag.getConfigName())) {
                for (String s : Menus.TAGS_ITEMS.getStringList("tag-item.lore")) {
                    item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix())));
                }
            } else {
                // Make equipped tag glow
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);

                for (String s : Menus.TAGS_ITEMS.getStringList("tag-item.equipped-lore")) {
                    item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix())));
                }
            }
        } else if (!player.hasPermission("simpletags.tag." + tag.getConfigName()) || !player.hasPermission("simpletags.tag.*")) {
            for (String s : Menus.TAGS_ITEMS.getStringList("tag-item.no-permission-lore")) {
                item.addLoreLine(Color.translate(player, s.replace("%tag-prefix%", tag.getTagPrefix())));
            }
        }

        NBTItem nbtItem = new NBTItem(item.toItemStack());
        nbtItem.setString("tag-name", tag.getConfigName());
        nbtItem.applyNBT(item.toItemStack());

        return item.toItemStack();
    }
}