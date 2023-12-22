package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.chat.Color;

public class Config {

    public boolean USE_CHAT;
    public String DATA_TYPE;

    public Config() {
        loadConfig();
    }

    public void loadConfig() {
        USE_CHAT = SimpleTags.getInstance().getConfigFile().getBoolean("use-chat");
        DATA_TYPE = SimpleTags.getInstance().getConfigFile().getString("data-type");

        Color.log("&c==========================================");
        Color.log("&eAll files have been loaded correctly!");
        Color.log("&c==========================================");
    }
}