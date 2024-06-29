package com.lgndluke.lgndware.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 *
 * @author lgndluke
 **/
public abstract class AbstractDatabaseHandler extends AbstractHandler {

    private final String dbPath = super.getPlugin().getDataFolder().getAbsolutePath() + "/" + super.getPlugin().getName() + ".db";
    private Connection dbCon;

    protected AbstractDatabaseHandler(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean initialize() {
        FutureTask<Boolean> initAbstractDatabaseHandler = new FutureTask<>(() -> {
            createDatabase();
            connect();
            createTables();
            return true;
        });
        return super.getAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initAbstractDatabaseHandler, 10, TimeUnit.SECONDS);
    }

    @Override
    public boolean terminate() {
        if(dbCon != null && !super.getAsyncExecutor().isShutdown()) {
            try {
                dbCon.close();
                super.getAsyncExecutor().shutdown();
                return true;
            } catch (SQLException se) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Error whilst trying to close database connection!", se);
            }
        }
        if(dbCon != null) {
            try {
                dbCon.close();
                return true;
            } catch (SQLException se) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Error whilst trying to close database connection!", se);
            }
        }
        if(!super.getAsyncExecutor().isShutdown()){
            super.getAsyncExecutor().shutdown();
            return true;
        }
        return false;
    }

    protected abstract void createDatabase();
    protected abstract void createTables();

    protected void connect() {
        String dbURL = "jdbc:sqlite:" + dbPath;
        try {
            this.dbCon = DriverManager.getConnection(dbURL);
            if(this.dbCon != null) {
                super.getPlugin().getLogger().log(Level.INFO, "Successfully connected to Database.");
            }
        } catch (Exception e) {
            super.getPlugin().getLogger().log(Level.SEVERE, "Couldn't connect to Database", e);
        }
    }

    protected String getDbPath() {
        return this.dbPath;
    }
    protected Connection getDbCon() {
        return this.dbCon;
    }

}
