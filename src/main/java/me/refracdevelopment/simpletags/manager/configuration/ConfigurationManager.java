package me.refracdevelopment.simpletags.manager.configuration;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import me.refracdevelopment.simpletags.SimpleTags;

public class ConfigurationManager extends AbstractConfigurationManager {

    public enum Setting implements RoseSetting {
        // Config Settings
        USE_CHAT("use-chat", false, "Automatically add tags to chat behind a player's name."),
        DATA_TYPE("data-type", "FLAT_FILE", "Choose your data saving type:", "MYSQL - Database saving", "FLAT_FILE - Local Json file saving in the playerdata/player.json file"),
        MYSQL_HOST("mysql.host", "127.0.0.1"),
        MYSQL_PORT("mysql.port", "3306"),
        MYSQL_DATABASE("mysql.database", "SimpleTags"),
        MYSQL_USERNAME("mysql.username", ""),
        MYSQL_PASSWORD("mysql.password", ""),
        ;

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return SimpleTags.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{
                "  ___________                __         ___________                      ",
                " /   _____/__| _____ ______ |  |   ____ \\__    ___/____     ____   ______",
                " \\_____  \\|  |/     \\\\____ \\|  | _/ __ \\  |    |  \\__  \\   / ___\\ /  ___/",
                " /        \\  |  | |  \\  |_\\ \\  |__  ___/_ |    |   / __ \\_/ /_/  \\\\___ \\ ",
                "/_______  /__|__|_|  /   ___/____/\\___  / |____|  (____  /\\___  //____  ",
                "        \\/         \\/|__|             \\/               \\//_____/      \\/ ",
        };
    }

}