package me.refracdevelopment.simpletags.player.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
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

    private String tag, tagPrefix = "";

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void load() {
        switch (plugin.getDataType()) {
            case MYSQL:
                plugin.getMySQLManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
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
                break;
            default:
                plugin.getSqLiteManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
                            plugin.getSqLiteManager().execute("UPDATE SimpleTags SET name=? WHERE uuid=?",
                                    name, uuid.toString());
                        } else {
                            plugin.getSqLiteManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                    uuid.toString(), name, "", "");
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, uuid.toString());
                break;
        }

        // Update player tag if any changes were made to it
        if (plugin.getTagManager().getCachedTag(tag) != null) {
            setTagPrefix(plugin.getTagManager().getCachedTag(tag).getTagPrefix());
        }
    }

    public void save() {
        switch (plugin.getDataType()) {
            case MYSQL:
                plugin.getMySQLManager().execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?",
                        tag, tagPrefix, uuid.toString());
                break;
            default:
                plugin.getSqLiteManager().execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?",
                        tag, tagPrefix, uuid.toString());
                break;
        }
    }

    public Player getPlayer() {
        return plugin.getServer().getPlayer(uuid);
    }

}