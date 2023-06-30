package me.refracdevelopment.simpletags.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.database.DataType;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.entity.Player;

import java.sql.SQLException;
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

    public void load() {
        if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getMySQLManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        setTag(resultSet.getString("tag"));
                        setTagPrefix(resultSet.getString("tagprefix"));
                        plugin.getMySQLManager().execute("UPDATE SimpleTags SET name=? WHERE uuid=?",
                                name, uuid.toString());
                    } else {
                        plugin.getMySQLManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                uuid.toString(), name, "", "");
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            }, uuid.toString());
        } else if (plugin.getDataType() == DataType.FLAT_FILE) {
            plugin.getPlayerMapper().loadPlayerFile(uuid);
        }

        // Update player tag if any changes were made to it
        if (plugin.getTagManager().getCachedTag(tag) != null) {
            setTagPrefix(plugin.getTagManager().getCachedTag(tag).getTagPrefix());
        }
    }

    public void save() {
        if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getMySQLManager().execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?",
                    tag, tagPrefix, uuid.toString());
        } else if (plugin.getDataType() == DataType.FLAT_FILE) {
            plugin.getPlayerMapper().savePlayer(uuid, name, tag, tagPrefix);
        }
    }

    public Player getPlayer() {
        return plugin.getServer().getPlayer(uuid);
    }

}