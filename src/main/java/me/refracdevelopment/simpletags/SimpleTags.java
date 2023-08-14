package me.refracdevelopment.simpletags;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import me.refracdevelopment.simpletags.config.*;
import me.refracdevelopment.simpletags.data.ProfileManager;
import me.refracdevelopment.simpletags.database.DataType;
import me.refracdevelopment.simpletags.database.MySQLManager;
import me.refracdevelopment.simpletags.listeners.ChatListener;
import me.refracdevelopment.simpletags.listeners.MenuListener;
import me.refracdevelopment.simpletags.listeners.PlayerListener;
import me.refracdevelopment.simpletags.manager.*;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import me.refracdevelopment.simpletags.utilities.chat.PAPIExpansion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Getter
public final class SimpleTags extends RosePlugin {

    @Getter
    private static SimpleTags instance;

    private MySQLManager mySQLManager;
    private DataType dataType;
    private ProfileManager profileManager;
    private TagManager tagManager;
    private MenuManager menuManager;

    private ConfigFile tagsFile;
    private ConfigFile menusFile;
    private PlayerMapper playerMapper;

    public SimpleTags() {
        super(-1, 13205, ConfigurationManager.class, null, LocaleManager.class, CommandManager.class);
        instance = this;
    }

    @Override
    public void enable() {
        // Plugin startup logic
        long startTiming = System.currentTimeMillis();
        PluginManager pluginManager = this.getServer().getPluginManager();

        // Check if the server is on 1.7
        if (NMSUtil.getVersionNumber() <= 7) {
            Color.log("&cSimpleTags 1.7 is in legacy mode, please update to 1.8+");
            pluginManager.disablePlugin(this);
            return;
        }

        // Make sure the server has NBTAPI
        if (pluginManager.getPlugin("NBTAPI") == null) {
            Color.log("&cPlease install NBTAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        if (pluginManager.getPlugin("Skulls") != null) {
            Color.log("&eSkulls Detected!");
        }

        if (pluginManager.getPlugin("HeadDatabase") != null) {
            Color.log("&eHeadDatabase Detected!");
        }

        loadFiles();

        loadManagers();
        Color.log("&aLoaded commands.");
        loadListeners();

        // Loads all available tags
        Tasks.runAsync(this, () -> tagManager.loadTags());

        new PAPIExpansion().register();

        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Color.log("&e" + this.getDescription().getName() + " has been enabled. (" + (System.currentTimeMillis() - startTiming) + "ms)");
        Color.log(" &f[*] &6Version&f: &b" + this.getDescription().getVersion());
        Color.log(" &f[*] &6Name&f: &b" + this.getDescription().getName());
        Color.log(" &f[*] &6Author&f: &b" + this.getDescription().getAuthors().get(0));
        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");

        updateCheck(Bukkit.getConsoleSender(), true);
    }

    @Override
    public void disable() {
        // unused
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return Collections.emptyList();
    }

    public void loadFiles() {
        tagsFile = new ConfigFile(this, "tags.yml");
        menusFile = new ConfigFile(this, "menus.yml");
        tagsFile.load();
        menusFile.load();
        Config.loadConfig();
        Tags.loadConfig();
        Menus.loadConfig();
    }

    private void loadManagers() {
        if (Config.DATA_TYPE.equalsIgnoreCase("MYSQL")) {
            dataType = DataType.MYSQL;
            mySQLManager = new MySQLManager(this);
            getMySQLManager().connect();
            getMySQLManager().createT();
            Color.log("&aEnabled MySQL support!");
        } else {
            dataType = DataType.FLAT_FILE;
            playerMapper = new PlayerMapper(getDataFolder().getAbsolutePath() + File.separator + "playerdata");
            Color.log("&aEnabled Flat File support!");
        }

        profileManager = new ProfileManager();
        tagManager = new TagManager();
        menuManager = new MenuManager();
        Color.log("&aLoaded managers.");
    }

    private void loadListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new MenuListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        Color.log("&aLoaded listeners.");
    }

    public void updateCheck(CommandSender sender, boolean console) {
        try {
            String urlString = "https://refracdev-updatecheck.refracdev.workers.dev/";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer response = new StringBuffer();
            while ((input = reader.readLine()) != null) {
                response.append(input);
            }
            reader.close();
            JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();

            if (object.has("plugins")) {
                JsonObject plugins = object.get("plugins").getAsJsonObject();
                JsonObject info = plugins.get(this.getDescription().getName()).getAsJsonObject();
                String version = info.get("version").getAsString();
                if (version.equals(this.getDescription().getVersion())) {
                    if (console) {
                        sender.sendMessage(Color.translate("&a" + this.getDescription().getName() + " is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour " + this.getDescription().getName() + " version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + this.getDescription().getVersion()));
                    sender.sendMessage(Color.translate("&aNewest Version: &e" + version));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    return;
                }
            } else {
                sender.sendMessage(Color.translate("&cWrong response from update API, contact plugin developer!"));
            }
        } catch (
                Exception ex) {
            sender.sendMessage(Color.translate("&cFailed to get updater check. (" + ex.getMessage() + ")"));
        }
    }
}
