package me.refracdevelopment.simpletags;

import com.cryptomorin.xseries.ReflectionUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import me.refracdevelopment.simpletags.commands.*;
import me.refracdevelopment.simpletags.listeners.MenuListener;
import me.refracdevelopment.simpletags.listeners.PlayerListener;
import me.refracdevelopment.simpletags.manager.CommandManager;
import me.refracdevelopment.simpletags.manager.MenuManager;
import me.refracdevelopment.simpletags.manager.ProfileManager;
import me.refracdevelopment.simpletags.manager.TagManager;
import me.refracdevelopment.simpletags.manager.configuration.ConfigFile;
import me.refracdevelopment.simpletags.manager.configuration.cache.Commands;
import me.refracdevelopment.simpletags.manager.configuration.cache.Config;
import me.refracdevelopment.simpletags.manager.configuration.cache.Menus;
import me.refracdevelopment.simpletags.manager.configuration.cache.Tags;
import me.refracdevelopment.simpletags.manager.data.DataType;
import me.refracdevelopment.simpletags.manager.data.MySQLManager;
import me.refracdevelopment.simpletags.manager.data.SQLiteManager;
import me.refracdevelopment.simpletags.menu.TagsMenu;
import me.refracdevelopment.simpletags.utilities.DownloadUtil;
import me.refracdevelopment.simpletags.utilities.chat.Color;
import me.refracdevelopment.simpletags.utilities.chat.PAPIExpansion;
import me.refracdevelopment.simpletags.utilities.command.SubCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public final class SimpleTags extends JavaPlugin {

    @Getter
    private static SimpleTags instance;

    // Managers
    private DataType dataType;
    private MySQLManager mySQLManager;
    private SQLiteManager sqLiteManager;
    private ProfileManager profileManager;
    private TagManager tagManager;
    private MenuManager menuManager;
    private CommandManager commandManager;

    // Files
    private ConfigFile configFile;
    private ConfigFile tagsFile;
    private ConfigFile menusFile;
    private ConfigFile commandsFile;
    private ConfigFile localeFile;

    // Cache
    private Config settings;
    private Tags tags;
    private Menus menus;
    private Commands commands;

    // Utilities
    private FoliaLib foliaLib;
    private final List<SubCommand> subCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        long startTiming = System.currentTimeMillis();
        PluginManager pluginManager = this.getServer().getPluginManager();

        foliaLib = new FoliaLib(this);

        DownloadUtil.downloadAndEnable();

        new Metrics(this, 13205);

        loadFiles();

        // Check if the server is on 1.7
        if (ReflectionUtils.MINOR_NUMBER <= 7) {
            Color.log("&c" + getDescription().getName() + " 1.7 is in legacy mode, please update to 1.8+");
            pluginManager.disablePlugin(this);
            return;
        }

        // Check if the server is on Folia
        if (getFoliaLib().isFolia()) {
            Color.log("&cSupport for Folia has not been tested and is only for experimental purposes.");
        }

        // Make sure the server has PlaceholderAPI
        if (!pluginManager.isPluginEnabled("PlaceholderAPI")) {
            Color.log("&cPlease install PlaceholderAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        // Make sure the server has NBTAPI
        if (!pluginManager.isPluginEnabled("NBTAPI")) {
            Color.log("&cPlease install NBTAPI onto your server to use this plugin.");
            pluginManager.disablePlugin(this);
            return;
        }

        if (pluginManager.isPluginEnabled("Skulls")) {
            Color.log("&eSkulls Detected!");
        }

        if (pluginManager.isPluginEnabled("HeadDatabase")) {
            Color.log("&eHeadDatabase Detected!");
        }

        loadManagers();
        loadCommands();
        loadListeners();

        // Loads all available tags
        getTagManager().loadTags();

        new PAPIExpansion().register();

        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");
        Color.log("&e" + getDescription().getName() + " has been enabled. (took " + (System.currentTimeMillis() - startTiming) + "ms)");
        Color.log(" &f[*] &6Version&f: &b" + getDescription().getVersion());
        Color.log(" &f[*] &6Name&f: &b" + getDescription().getName());
        Color.log(" &f[*] &6Author&f: &b" + getDescription().getAuthors().get(0));
        Color.log("&8&m==&c&m=====&f&m======================&c&m=====&8&m==");

        updateCheck(Bukkit.getConsoleSender(), true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        switch (dataType) {
            case MYSQL:
                getMySQLManager().shutdown();
                break;
            default:
                getSqLiteManager().shutdown();
                break;
        }
        getServer().getScheduler().cancelTasks(this);
    }

    public void loadFiles() {
        // Files
        configFile = new ConfigFile("config.yml");
        tagsFile = new ConfigFile("tags.yml");
        menusFile = new ConfigFile("menus.yml");
        commandsFile = new ConfigFile("commands/tags.yml");
        localeFile = new ConfigFile("locale/" + getConfigFile().getString("locale") + ".yml");

        // Cache
        settings = new Config();
        tags = new Tags();
        menus = new Menus();
        commands = new Commands();
    }

    public void reloadFiles() {
        getConfigFile().reload();
        getTagsFile().reload();
        getMenusFile().reload();
        getLocaleFile().reload();
        getCommandsFile().reload();

        getSettings().loadConfig();
        getTags().loadConfig();
        getMenus().loadConfig();
        getCommands().loadConfig();
    }

    private void loadManagers() {
        switch (getSettings().DATA_TYPE.toUpperCase()) {
            case "MARIADB":
            case "MYSQL":
                dataType = DataType.MYSQL;
                mySQLManager = new MySQLManager();
                getMySQLManager().connect();
                getMySQLManager().createT();
                break;
            default:
                dataType = DataType.SQLITE;
                sqLiteManager = new SQLiteManager();
                getSqLiteManager().connect(getDataFolder().getAbsolutePath() + File.separator + "tags.db");
                getSqLiteManager().createT();
                break;
        }

        profileManager = new ProfileManager();
        tagManager = new TagManager();
        menuManager = new MenuManager();
        commandManager = new CommandManager();
        Color.log("&aLoaded managers.");
    }

    private void loadCommands() {
        try {
            getCommandManager().createCoreCommand(this, getCommands().TAGS_COMMAND_NAME,
                    getLocaleFile().getString("command-tags-description"),
                    "/" + getCommands().TAGS_COMMAND_NAME, (commandSender, list) -> {
                        // Make sure the sender is a player.
                        if (!(commandSender instanceof Player)) {
                            Color.sendMessage(commandSender, "no-console");
                            return;
                        }

                        Player player = (Player) commandSender;

                        new TagsMenu(SimpleTags.getInstance().getMenuManager().getPlayerMenuUtility(player)).open();
                    }, getCommands().TAGS_COMMAND_ALIASES,
                    CreateCommand.class,
                    DeleteCommand.class,
                    EditCommand.class,
                    HelpCommand.class,
                    ListCommand.class,
                    ReloadCommand.class,
                    SetCommand.class,
                    VersionCommand.class
            );

            getSubCommands().addAll(Arrays.asList(
                    new CreateCommand(),
                    new DeleteCommand(),
                    new EditCommand(),
                    new HelpCommand(),
                    new ListCommand(),
                    new ReloadCommand(),
                    new SetCommand(),
                    new VersionCommand()
            ));

            Color.log("&aLoaded commands.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Color.log("&aFailed to load commands.");
            e.printStackTrace();
            return;
        }
    }

    private void loadListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new MenuListener(), this);
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
                JsonObject info = plugins.get(getDescription().getName()).getAsJsonObject();
                String version = info.get("version").getAsString();
                if (version.equals(getDescription().getVersion())) {
                    if (console) {
                        sender.sendMessage(Color.translate("&a" + getDescription().getName() + " is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour " + getDescription().getName() + " version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + getDescription().getVersion()));
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
