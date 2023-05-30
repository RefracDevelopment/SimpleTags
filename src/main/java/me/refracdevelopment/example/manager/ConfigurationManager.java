package me.refracdevelopment.example.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import me.refracdevelopment.example.ExamplePlugin;

public class ConfigurationManager extends AbstractConfigurationManager {

    public enum Setting implements RoseSetting {
        // Config Settings
        PREFIX("prefix", "<g:#8A2387:#E94057:#F27121>ExamplePlugin &8| &f")
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
            return ExamplePlugin.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{
                "___________                             __          ",
                "\\_   _____/__  _______    _____ ______ |  |   ____  ",
                " |    __)_\\  \\/  /__  \\  /     \\\\____ \\|  | _/ __ \\ ",
                " |        \\\\    / / __ \\_  | |  \\  |_\\ \\  |__  ___/_",
                "/_______  /__/\\_ \\____  /__|_|  /   ___/____/\\___  /",
                "        \\/      \\/    \\/      \\/|__|             \\/ ",
        };
    }

}