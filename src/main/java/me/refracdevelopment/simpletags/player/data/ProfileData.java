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

    private String tag, tagPrefix;

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void load(Player player) {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
                            getPlugin().getMySQLManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                        } else {
                            getPlugin().getMySQLManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                    player.getUniqueId().toString(), player.getName(), "", "");
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, player.getUniqueId().toString());
                break;
            default:
                getPlugin().getSqLiteManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
                            getPlugin().getSqLiteManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                        } else {
                            getPlugin().getSqLiteManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                    player.getUniqueId().toString(), player.getName(), "", "");
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, player.getUniqueId().toString());
                break;
        }

        // Update player tag if any changes were made to it
        if (getPlugin().getTagManager().getCachedTag(tag) == null || tag.isEmpty()) {
            setTag("");
            setTagPrefix("");
        } else
            setTagPrefix(getPlugin().getTagManager().getCachedTag(tag).getTagPrefix());
    }

    public void save(Player player) {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().updatePlayerTag(player.getUniqueId().toString(), getTag(), getTagPrefix());
                break;
            default:
                getPlugin().getSqLiteManager().updatePlayerTag(player.getUniqueId().toString(), getTag(), getTagPrefix());
                break;
        }
    }
}