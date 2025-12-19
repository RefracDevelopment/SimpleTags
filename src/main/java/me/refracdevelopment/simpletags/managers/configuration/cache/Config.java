package me.refracdevelopment.simpletags.managers.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;

public class Config {

    public boolean USE_CHAT, REQUIRE_PERMISSION, CHECK_FOR_UPDATES;
    public String DATA_TYPE;

    public Config() {
        loadConfig();
    }

    public void loadConfig() {
        REQUIRE_PERMISSION = SimpleTags.getInstance().getConfigFile().getBoolean("require-permission");
        USE_CHAT = SimpleTags.getInstance().getConfigFile().getBoolean("use-chat");
        DATA_TYPE = SimpleTags.getInstance().getConfigFile().getString("data-type");
        CHECK_FOR_UPDATES = SimpleTags.getInstance().getConfigFile().getBoolean("check-for-updates");
    }
}