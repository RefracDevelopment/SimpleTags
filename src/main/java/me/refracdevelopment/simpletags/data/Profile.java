package me.refracdevelopment.simpletags.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simpletags.SimpleTags;

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