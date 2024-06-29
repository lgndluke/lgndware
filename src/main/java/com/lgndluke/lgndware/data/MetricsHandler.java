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
        return super.getAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initMetricsHandler, 10, TimeUnit.SECONDS);
    }

    @Override
    public boolean terminate() {
        if(this.metrics != null && !super.getAsyncExecutor().isShutdown()) {
            this.metrics.shutdown();
            super.getAsyncExecutor().shutdown();
            return true;
        }
        if(this.metrics != null){
            this.metrics.shutdown();
            return true;
        }
        if(!super.getAsyncExecutor().isShutdown()) {
            super.getAsyncExecutor().shutdown();
            return true;
        }
        return false;
    }

}
