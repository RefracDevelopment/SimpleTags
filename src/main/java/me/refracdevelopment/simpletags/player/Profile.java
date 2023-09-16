package me.refracdevelopment.simpletags.player;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.player.data.ProfileData;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private SimpleTags plugin = SimpleTags.getInstance();

    private ProfileData data;
    private UUID UUID;
    private String playerName;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new ProfileData(uuid, name);
    }
}