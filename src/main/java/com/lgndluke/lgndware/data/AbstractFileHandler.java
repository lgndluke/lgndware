package com.lgndluke.lgndware.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * This abstract class provides functionality for file handler classes.
 * @author lgndluke
 **/
public abstract class AbstractFileHandler extends AbstractHandler {

    private final File file;
    private FileConfiguration fileConfig;

    /**
     * @param plugin the plugin the file belongs to.
     * @param fileName the file name including the file type.
     **/
    protected AbstractFileHandler(JavaPlugin plugin, String fileName) {
        super(plugin);
        createDataFolder();
        this.file = new File(super.getPlugin().getDataFolder().getAbsoluteFile(), fileName);
        this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Creates the file.
     **/
    @Override
    public boolean initialize() {
        FutureTask<Boolean> initAbstractFileHandler = new FutureTask<>(() -> {
            createFile();
            this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
            this.fileConfig.options().copyDefaults(true);
            save();
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initAbstractFileHandler, 10, TimeUnit.SECONDS);
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
     * Reloads the file.
     **/
    public boolean reload() {
        FutureTask<Boolean> reloadFile = new FutureTask<>(() -> {
            this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
            this.fileConfig.options().copyDefaults(true);
            save();
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), reloadFile, 10, TimeUnit.SECONDS);
    }

    /**
     * Saves the file.
     **/
    public boolean save() {
        FutureTask<Boolean> saveFile = new FutureTask<>(() -> {
            try {
                this.fileConfig.save(this.file);
            } catch (IOException e) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Couldn't save data to " + this.file.getName(), e);
            }
            return true;
        });
        return super.getDefaultAsyncExecutor().executeFuture(super.getPlugin().getLogger(), saveFile, 10, TimeUnit.SECONDS);
    }

    protected FileConfiguration getFileConfig() {
        return this.fileConfig;
    }

    protected File getFile() {
        return this.file;
    }

    private void createDataFolder() {
        if(!super.getPlugin().getDataFolder().exists()) {
            boolean isCreated = super.getPlugin().getDataFolder().mkdir();
            if(isCreated) {
                super.getPlugin().getLogger().log(Level.INFO, "Successfully created " + super.getPlugin().getName() + " folder inside plugins folder.");
            }
        }
    }

    protected void createFile() {
        if(!this.file.exists()) {
            try {
                boolean fileCreated = this.file.createNewFile();
                if(fileCreated) {
                    super.getPlugin().getLogger().log(Level.INFO, "Successfully created " + this.file.getName() + " file.");
                }
            } catch (IOException e) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Could not create " + this.file.getName() + " file!", e);
            }
        }
    }

}
