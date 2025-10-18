package me.refracdevelopment.simpletags.manager.configuration;

import lombok.Getter;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@Getter
public class ConfigFile {

    private File configFile;
    private FileConfiguration config;

    public ConfigFile(String name) {
        try {
            configFile = new File(SimpleTags.getInstance().getDataFolder(), name);

            if (!configFile.exists())
                SimpleTags.getInstance().saveResource(name, false);

            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            RyMessageUtils.sendConsole(true, "&cFailed to load " + name + " file! The plugin will now shutdown.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(SimpleTags.getInstance());
        }
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getInt(String path) {
        return config.getInt(path, 0);
    }

    public double getDouble(String path) {
        return config.getDouble(path, 0.0);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    public String getString(String path, boolean check) {
        return config.getString(path, null);
    }

    public String getString(String path) {
        if (config.contains(path)) {
            return config.getString(path, "String at path '" + path + "' not found.").replace("|", "\u2503");
        }

        return null;
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public List<String> getStringList(String path, boolean check) {
        if (!config.contains(path)) return null;
        return getStringList(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void remove(String path) {
        config.set(path, null);
    }
}