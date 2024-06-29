package com.lgndluke.lgndware.loaders;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractLoader {

    private final JavaPlugin plugin;

    protected AbstractLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void load();

    protected JavaPlugin getPlugin() {
        return this.plugin;
    }

}
