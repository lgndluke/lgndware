package com.lgndluke.lgndware.data;

import com.lgndluke.lgndware.concurrent.DefaultAsyncExecutor;
import com.lgndluke.lgndware.concurrent.ScheduledAsyncExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractHandler {

    private final JavaPlugin plugin;
    private final DefaultAsyncExecutor defaultAsyncExecutor = new DefaultAsyncExecutor();
    private final ScheduledAsyncExecutor scheduledAsyncExecutor = new ScheduledAsyncExecutor();

    protected AbstractHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract boolean initialize();
    public abstract boolean terminate();

    protected JavaPlugin getPlugin() {
        return this.plugin;
    }
    protected DefaultAsyncExecutor getDefaultAsyncExecutor() {
        return this.defaultAsyncExecutor;
    }
    protected ScheduledAsyncExecutor getScheduledAsyncExecutor() {
        return this.scheduledAsyncExecutor;
    }

}
