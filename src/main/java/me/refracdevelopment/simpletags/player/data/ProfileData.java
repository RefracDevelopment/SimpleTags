package me.refracdevelopment.simpletags.player.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import org.bukkit.Bukkit;
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
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
                            getPlugin().getMySQLManager().updatePlayerName(getUuid(), getName());
                        } else {
                            getPlugin().getMySQLManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                    getUuid().toString(), name, "", "");
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, getUuid().toString());
                break;
            default:
                getPlugin().getSqLiteManager().select("SELECT * FROM SimpleTags WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            setTag(resultSet.getString("tag"));
                            setTagPrefix(resultSet.getString("tagPrefix"));
                            getPlugin().getSqLiteManager().updatePlayerName(getUuid(), getName());
                        } else {
                            getPlugin().getSqLiteManager().execute("INSERT INTO SimpleTags (uuid, name, tag, tagPrefix) VALUES (?,?,?,?)",
                                    getUuid().toString(), name, "", "");
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                }, getUuid().toString());
                break;
        }

        // Update player tag if any changes were made to it
        if (getPlugin().getTagManager().getCachedTag(tag) == null) {
            setTag("");
            setTagPrefix("");
            return;
        }

        setTagPrefix(getPlugin().getTagManager().getCachedTag(tag).getTagPrefix());
    }

    public void save() {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?",
                        getTag(), getTagPrefix(), getUuid().toString());
                break;
            default:
                getPlugin().getSqLiteManager().execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?",
                        getTag(), getTagPrefix(), getUuid().toString());
                break;
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}