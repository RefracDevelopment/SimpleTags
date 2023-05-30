package me.refracdevelopment.example.config;

import me.refracdevelopment.example.manager.ConfigurationManager;
import me.refracdevelopment.example.utilities.chat.Color;

public class Config {

    public static String PREFIX;

    public static void loadConfig() {
        PREFIX = ConfigurationManager.Setting.PREFIX.getString();

        Color.log("&c==========================================");
        Color.log("&aAll files have been loaded correctly!");
        Color.log("&c==========================================");
    }
}