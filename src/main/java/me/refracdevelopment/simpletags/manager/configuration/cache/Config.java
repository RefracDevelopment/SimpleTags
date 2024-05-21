package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;

public class Config {

    public boolean USE_CHAT, REQUIRE_PERMISSION;
    public String DATA_TYPE;

    public Config() {
        loadConfig();
    }

    public void loadConfig() {
        REQUIRE_PERMISSION = SimpleTags.getInstance().getConfigFile().getBoolean("require-permission");
        USE_CHAT = SimpleTags.getInstance().getConfigFile().getBoolean("use-chat");
        DATA_TYPE = SimpleTags.getInstance().getConfigFile().getString("data-type");
    }
}