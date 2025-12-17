package me.refracdevelopment.simpletags.player.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private final SimpleTags plugin = SimpleTags.getInstance();
    private final String name;
    private final UUID uuid;

    private String tag, tagPrefix;

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void load(Player player) {
        switch (plugin.getDataType()) {
            case MYSQL:
                plugin.getMySQLManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    if (resultSet.next()) {
                        setTag(resultSet.getString("tag"));
                        setTagPrefix(resultSet.getString("tagPrefix"));
                        plugin.getMySQLManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                    } else {
                        plugin.getMySQLManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                player.getUniqueId().toString(), player.getName(), "", "");
                    }
                }, player.getUniqueId().toString());
                break;
            default:
                plugin.getSqLiteManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    if (resultSet.next()) {
                        setTag(resultSet.getString("tag"));
                        setTagPrefix(resultSet.getString("tagPrefix"));
                        plugin.getSqLiteManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                    } else {
                        plugin.getSqLiteManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                player.getUniqueId().toString(), player.getName(), "", "");
                    }
                }, player.getUniqueId().toString());
                break;
        }

        // Update player tag if any changes were made to it
        if (plugin.getTagManager().getCachedTag(tag) == null) {
            setTag("");
            setTagPrefix("");
        } else
            setTagPrefix(plugin.getTagManager().getCachedTag(tag).getTagPrefix());
    }

    public void save(Player player) {
        switch (plugin.getDataType()) {
            case MYSQL:
                plugin.getMySQLManager().updatePlayerTag(player.getUniqueId().toString(), getTag(), getTagPrefix());
                break;
            default:
                plugin.getSqLiteManager().updatePlayerTag(player.getUniqueId().toString(), getTag(), getTagPrefix());
                break;
        }
    }
}