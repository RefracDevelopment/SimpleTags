package me.refracdevelopment.simpletags.utilities;

import com.cryptomorin.xseries.XMaterial;
import lombok.experimental.UtilityClass;
import me.refracdevelopment.simpletags.SimpleTags;

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
        switch (SimpleTags.getInstance().getDataType()) {
            case MYSQL:
                SimpleTags.getInstance().getMySQLManager().updatePlayerTag(uuid, tag, tagPrefix);
                break;
            default:
                SimpleTags.getInstance().getSqLiteManager().updatePlayerTag(uuid, tag, tagPrefix);
                break;
        }
    }

}