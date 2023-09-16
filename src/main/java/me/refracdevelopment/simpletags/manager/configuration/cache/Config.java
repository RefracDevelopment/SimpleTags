package me.refracdevelopment.simpletags.manager.configuration.cache;

import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.manager.configuration.ConfigurationManager;
import me.refracdevelopment.simpletags.manager.configuration.LocaleManager;
import me.refracdevelopment.simpletags.utilities.chat.Color;

public class Config {

    public static boolean USE_CHAT;
    public static String DATA_TYPE;
    public static String KICK_MESSAGES_ERROR;

    public static void loadConfig() {
        final LocaleManager localeManager = SimpleTags.getInstance().getManager(LocaleManager.class);

        USE_CHAT = ConfigurationManager.Setting.USE_CHAT.getBoolean();
        DATA_TYPE = ConfigurationManager.Setting.DATA_TYPE.getString();
        KICK_MESSAGES_ERROR = localeManager.getLocaleMessage("kick-messages-error");

        Color.log("&c==========================================");
        Color.log("&eAll files have been loaded correctly!");
        Color.log("&c==========================================");
    }
}