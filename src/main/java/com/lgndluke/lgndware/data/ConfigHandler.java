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
    public boolean initialize() {
        FutureTask<Boolean> initConfigHandler = new FutureTask<>(() -> {
            super.getPlugin().getConfig().options().copyDefaults(true);
            super.getPlugin().saveDefaultConfig();
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initConfigHandler, 10, TimeUnit.SECONDS);
    }

    /**
     * Reloads the 'config.yml' file.
     **/
    @Override
    public boolean reload() {
        FutureTask<Boolean> reloadConfig = new FutureTask<>(() -> {
            super.getPlugin().reloadConfig();
            super.getPlugin().getConfig().options().copyDefaults(true);
            super.getPlugin().saveConfig();
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), reloadConfig, 10, TimeUnit.SECONDS);
    }

    @Override
    public boolean save() {
        FutureTask<Boolean> saveConfig = new FutureTask<>(() -> {
            super.getPlugin().saveConfig();
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), saveConfig, 10, TimeUnit.SECONDS);
    }

    /** Provides easy access to objects from the Plugins config.
     * @param value — has to be set inside the 'config.yml' file.
     * @return requested Object from 'config.yml' file.
     * @throws NullPointerException if value isn't set inside 'config.yml'
     **/
    public Object get(String value) throws NullPointerException {
        FutureTask<Object> getConfigValue = new FutureTask<>(() -> super.getPlugin().getConfig().get(value));
        return super.getDefaultAsyncExecutor().fetchExecutionResult(super.getPlugin().getLogger(), getConfigValue, 10, TimeUnit.SECONDS);
    }

}
