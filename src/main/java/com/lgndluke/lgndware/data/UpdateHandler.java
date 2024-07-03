package com.lgndluke.lgndware.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateHandler extends AbstractHandler {

    private final int resourceID;

    public UpdateHandler(JavaPlugin plugin, int resourceID) {
        super(plugin);
        this.resourceID = resourceID;
    }

    /**
     * Asynchronously checks for updates.
     **/
    @Override
    public boolean initialize() {
        FutureTask<Boolean> initUpdateHandler = new FutureTask<>(() -> {
            String versionString = super.getPlugin().getPluginMeta().getVersion();
            checkForUpdates(version -> {
                if(!versionString.equals(version)) {
                    super.getPlugin().getLogger().log(Level.WARNING, "A new Version of " + super.getPlugin().getName() + " is available. Consider updating!");
                }
            });
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initUpdateHandler, 10, TimeUnit.SECONDS);
    }

    @Override
    public boolean terminate() {
        if(!super.getDefaultAsyncExecutor().isShutdown()) {
            super.getDefaultAsyncExecutor().shutdown();
        }
        if(!super.getScheduledAsyncExecutor().isShutdown()) {
            super.getScheduledAsyncExecutor().shutdown();
        }
        return true;
    }

    /**
     * Fetches the current version String from SpigotMC's API.
     **/
    private void checkForUpdates(Consumer<String> consumer) {
        super.getPlugin().getServer().getScheduler().runTaskAsynchronously(super.getPlugin(), () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID + "/~").openStream();
                 Scanner scan = new Scanner(inputStream)) {
                if(scan.hasNext()) {
                    consumer.accept(scan.next());
                }
            } catch (IOException io) {
                super.getPlugin().getLogger().log(Level.WARNING, "Checking for updates failed!", io.getMessage());
            }
        });
    }

}
