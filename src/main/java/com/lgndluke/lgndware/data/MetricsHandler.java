package com.lgndluke.lgndware.data;

import com.lgndluke.lgndware.metrics.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class MetricsHandler extends AbstractHandler {

    private final int pluginID;
    private Metrics metrics;

    public MetricsHandler(JavaPlugin plugin, int pluginID) {
        super(plugin);
        this.pluginID = pluginID;
    }

    @Override
    public boolean initialize() {
        FutureTask<Boolean> initMetricsHandler = new FutureTask<>(() -> {
            this.metrics = new Metrics(super.getPlugin(), pluginID);
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initMetricsHandler, 10, TimeUnit.SECONDS);
    }

    @Override
    public boolean terminate() {
        if(this.metrics != null) {
            this.metrics.shutdown();
        }
        if(!super.getDefaultAsyncExecutor().isShutdown()) {
            super.getDefaultAsyncExecutor().shutdown();
        }
        if(!super.getScheduledAsyncExecutor().isShutdown()) {
            super.getScheduledAsyncExecutor().shutdown();
        }
        return true;
    }

}
