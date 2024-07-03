package com.lgndluke.lgndware.commands;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.FutureTask;

public abstract class AbstractCommandExecutorUtil extends AbstractCommandExecutor {

    protected AbstractCommandExecutorUtil(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract FutureTask<Boolean> execute();

}
