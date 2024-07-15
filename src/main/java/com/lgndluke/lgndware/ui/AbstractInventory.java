package com.lgndluke.lgndware.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract base class for Inventory generation.
 * @author lgndluke
 **/
public abstract class AbstractInventory {

    private final JavaPlugin plugin;
    private final Inventory inventory;

    /**
     * @param plugin The JavaPlugin instance associated with this AbstractInventory.
     * @param size The size of the inventory.
     * @param title The title of the inventory.
     **/
    protected AbstractInventory(JavaPlugin plugin, int size, Component title) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(null, size, title);
    }

    /**
     * Abstract method to be implemented by subclasses for initialization logic.
     * @return True, if the initialization was successful. Otherwise, false.
     **/
    protected abstract boolean initialize();

    /**
     * @return The Inventory instance.
     **/
    public Inventory getInventory() {
        return this.inventory;
    }

}
