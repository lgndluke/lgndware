package com.lgndluke.lgndware.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.FutureTask;
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
    }

    /**
     * Creates the file.
     **/
    @Override
    public void initialize() {
        FutureTask<Void> initAbstractFileHandler = new FutureTask<>(() -> {
            createFile();
            this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
            this.fileConfig.options().copyDefaults(true);
            save();
            return null;
        });
        super.getAsyncExecutor().execute(initAbstractFileHandler);
    }

    @Override
    public void terminate() {
        super.getAsyncExecutor().shutdown();
    }

    /**
     * Reloads the file.
     **/
    public void reload() {
        FutureTask<Void> reloadFile = new FutureTask<>(() -> {
            this.fileConfig = YamlConfiguration.loadConfiguration(this.file);
            this.fileConfig.options().copyDefaults(true);
            save();
            return null;
        });
        super.getAsyncExecutor().execute(reloadFile);
    }

    /**
     * Saves the file.
     **/
    public void save() {
        FutureTask<Void> saveFile = new FutureTask<>(() -> {
            try {
                this.fileConfig.save(this.file);
            } catch (IOException e) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Couldn't save data to " + this.file.getName(), e);
            }
            return null;
        });
        super.getAsyncExecutor().execute(saveFile);
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

    private void createFile() {
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
