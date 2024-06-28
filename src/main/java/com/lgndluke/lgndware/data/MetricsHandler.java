package com.lgndluke.lgndware.data;

import com.lgndluke.lgndware.metrics.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.FutureTask;

public class MetricsHandler extends AbstractHandler {

    private final int pluginID;
    private Metrics metrics;

    public MetricsHandler(JavaPlugin plugin, int pluginID) {
        super(plugin);
        this.pluginID = pluginID;
    }

    @Override
    public void initialize() {
        FutureTask<Void> initMetricsHandler = new FutureTask<>(() -> {
            this.metrics = new Metrics(super.getPlugin(), pluginID);
            return null;
        });
        super.getAsyncExecutor().execute(initMetricsHandler);
    }

    @Override
    public void terminate() {
        if(this.metrics != null) {
            this.metrics.shutdown();
        }
        super.getAsyncExecutor().shutdown();
    }

}
