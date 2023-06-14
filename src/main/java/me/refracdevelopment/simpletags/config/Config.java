package me.refracdevelopment.simpletags.config;

import me.refracdevelopment.simpletags.manager.ConfigurationManager;
import me.refracdevelopment.simpletags.utilities.chat.Color;

public class Config {

    public static boolean DEBUG;
    public static boolean USE_CHAT;
    public static String DATA_TYPE;

    public static void loadConfig() {
        DEBUG = ConfigurationManager.Setting.DEBUG.getBoolean();
        USE_CHAT = ConfigurationManager.Setting.USE_CHAT.getBoolean();
        DATA_TYPE = ConfigurationManager.Setting.DATA_TYPE.getString();

        Color.log("&c==========================================");
        Color.log("&eAll files have been loaded correctly!");
        Color.log("&c==========================================");
    }
}