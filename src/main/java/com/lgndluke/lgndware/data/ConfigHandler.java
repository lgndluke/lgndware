package com.lgndluke.lgndware.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * This is a config related utility Class.
 * @author lgndluke
 **/
public class ConfigHandler extends AbstractFileHandler {

    /**
     * @param plugin — the JavaPlugin that registers the ConfigHandler.
     **/
    public ConfigHandler(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    /**
     * Initializes the 'config.yml' file on server startup.
     **/
    @Override
    public void initialize() {
        FutureTask<Void> initConfigHandler = new FutureTask<>(() -> {
            super.getPlugin().getConfig().options().copyDefaults(true);
            super.getPlugin().saveDefaultConfig();
            return null;
        });
        super.getAsyncExecutor().execute(initConfigHandler);
    }

    /**
     * Reloads the 'config.yml' file.
     **/
    @Override
    public void reload() {
        FutureTask<Void> reloadConfig = new FutureTask<>(() -> {
            super.getPlugin().reloadConfig();
            super.getPlugin().getConfig().options().copyDefaults(true);
            super.getPlugin().saveConfig();
            return null;
        });
        super.getAsyncExecutor().execute(reloadConfig);
    }

    @Override
    public void save() {
        FutureTask<Void> saveConfig = new FutureTask<>(() -> {
            super.getPlugin().saveConfig();
            return null;
        });
        super.getAsyncExecutor().execute(saveConfig);
    }

    /** Provides easy access to objects from the Plugins config.
     * @param value — has to be set inside the 'config.yml' file.
     * @return requested Object from 'config.yml' file.
     * @throws NullPointerException if value isn't set inside 'config.yml'
     **/
    public Object get(String value) throws NullPointerException {
        FutureTask<Object> getConfigValue = new FutureTask<>(() -> super.getPlugin().getConfig().get(value));
        return super.getAsyncExecutor().fetchExecutionResult(super.getPlugin().getLogger(), getConfigValue, 10, TimeUnit.SECONDS);
    }

}
