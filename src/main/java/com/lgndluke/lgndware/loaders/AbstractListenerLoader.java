package com.lgndluke.lgndware.loaders;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractListenerLoader extends AbstractLoader {

    public AbstractListenerLoader(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public abstract void load();

    protected JavaPlugin getPlugin() {
        return super.getPlugin();
    }

}
