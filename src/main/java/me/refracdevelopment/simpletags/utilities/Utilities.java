package me.refracdevelopment.simpletags.utilities;

import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.data.DataType;

import java.util.UUID;

@UtilityClass
public class Utilities {

    public XMaterial getMaterial(String source) {
        XMaterial material;
        try {
            material = XMaterial.matchXMaterial(source).get();
        } catch (Exception e) {
            material = XMaterial.REDSTONE_BLOCK;
        }
        return material;
    }

    public void saveOfflinePlayer(UUID uuid, String tag, String tagPrefix) {
        if (SimpleTags.getInstance().getDataType() == DataType.MYSQL) {
            SimpleTags.getInstance().getMySQLManager().updatePlayerTag(uuid, tag, tagPrefix);
        } else if (SimpleTags.getInstance().getDataType() == DataType.SQLITE) {
            SimpleTags.getInstance().getSqLiteManager().updatePlayerTag(uuid, tag, tagPrefix);
        }
    }

}