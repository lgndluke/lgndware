package com.lgndluke.lgndware.loaders;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommandLoader extends AbstractLoader {

    public AbstractCommandLoader(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public abstract void load();

    protected JavaPlugin getPlugin() {
        return super.getPlugin();
    }

}
