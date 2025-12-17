package me.refracdevelopment.simpletags.managers.configuration;

import me.refracdevelopment.simpletags.SimpleTags;

import java.io.File;

public class Locale {

    private enum LocaleType {
        en_US,
        zh_CN
    }

    public Locale() {
        load();
    }

    public void load() {
        SimpleTags instance = SimpleTags.getInstance();

        for (LocaleType locale : LocaleType.values()) {
            File file = new File(instance.getDataFolder(), "locale/" + locale.toString() + ".yml");

            if (!file.exists()) {
                instance.saveResource("locale/" + locale + ".yml", false);
            }
        }

        instance.setLocaleFile(new ConfigFile(instance, "locale/" + instance.getConfigFile().getString("locale", "en_US") + ".yml"));
    }

    public void reload() {
        SimpleTags instance = SimpleTags.getInstance();

        instance.setLocaleFile(new ConfigFile(instance, "locale/" + instance.getConfigFile().getString("locale", "en_US") + ".yml"));
    }
}
