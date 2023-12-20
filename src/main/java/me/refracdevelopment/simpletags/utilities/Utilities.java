package me.refracdevelopment.simpletags.utilities;

import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.data.DataType;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@UtilityClass
public class Utilities {

    public static final UUID getDevUUID = UUID.fromString("d9c670ed-d7d5-45fb-a144-8b8be86c4a2d");
    public static final UUID getDevUUID2 = UUID.fromString("ab898e40-9088-45eb-9d69-e0b78e872627");

    public XMaterial getMaterial(String source) {
        XMaterial material;
        try {
            material = XMaterial.matchXMaterial(source).get();
        } catch (Exception e) {
            material = XMaterial.REDSTONE_BLOCK;
        }
        return material;
    }

    public static void sendDevMessage(Player player) {

        player.sendMessage("");
        Color.sendCustomMessage(player, "&aWelcome " + SimpleTags.getInstance().getDescription().getName() + " Developer!");
        Color.sendCustomMessage(player, "&aThis server is currently running " + SimpleTags.getInstance().getDescription().getName() + " &bv" + SimpleTags.getInstance().getDescription().getVersion() + "&a.");
        Color.sendCustomMessage(player, "&aPlugin name&7: &f" + SimpleTags.getInstance().getDescription().getName());
        player.sendMessage("");
        Color.sendCustomMessage(player, "&aServer version&7: &f" + Bukkit.getVersion());
        player.sendMessage("");
    }

    public static void saveOfflinePlayer(UUID uuid, String tag, String tagPrefix) {
        if (SimpleTags.getInstance().getDataType() == DataType.MYSQL) {
            SimpleTags.getInstance().getMySQLManager().updatePlayerTag(uuid, tag, tagPrefix);
        } else if (SimpleTags.getInstance().getDataType() == DataType.SQLITE) {
            SimpleTags.getInstance().getSqLiteManager().updatePlayerTag(uuid, tag, tagPrefix);
        }
    }

}