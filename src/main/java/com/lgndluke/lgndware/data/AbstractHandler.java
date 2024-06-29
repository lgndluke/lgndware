package com.lgndluke.lgndware.data;

import com.lgndluke.lgndware.concurrent.AsyncExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractHandler {

    private final JavaPlugin plugin;
    private final AsyncExecutor asyncExecutor = new AsyncExecutor();

    protected AbstractHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract boolean initialize();
    public abstract boolean terminate();

    protected JavaPlugin getPlugin() {
        return this.plugin;
    }
    protected AsyncExecutor getAsyncExecutor() {
        return this.asyncExecutor;
    }

}
