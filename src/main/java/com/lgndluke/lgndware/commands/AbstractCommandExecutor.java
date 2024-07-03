package com.lgndluke.lgndware.commands;

import com.lgndluke.lgndware.concurrent.DefaultAsyncExecutor;
import com.lgndluke.lgndware.concurrent.ScheduledAsyncExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommandExecutor {

    private final JavaPlugin plugin;
    private final DefaultAsyncExecutor defaultAsyncExecutor = new DefaultAsyncExecutor();
    private final ScheduledAsyncExecutor scheduledAsyncExecutor = new ScheduledAsyncExecutor();

    protected AbstractCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected boolean terminate() {
        if(!defaultAsyncExecutor.isShutdown()) {
            defaultAsyncExecutor.shutdown();
        }
        if(!scheduledAsyncExecutor.isShutdown()) {
            scheduledAsyncExecutor.shutdown();
        }
        return true;
    }

    protected JavaPlugin getPlugin() {
        return this.plugin;
    }
    protected DefaultAsyncExecutor getAsyncExecutor() {
        return this.defaultAsyncExecutor;
    }

}
