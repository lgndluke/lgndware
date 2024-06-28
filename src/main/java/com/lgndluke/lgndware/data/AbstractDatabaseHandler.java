package com.lgndluke.lgndware.data;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.FutureTask;
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
    public void initialize() {
        FutureTask<Void> initAbstractDatabaseHandler = new FutureTask<>(() -> {
            createDatabase();
            connect();
            createTables();
            return null;
        });
        super.getAsyncExecutor().execute(initAbstractDatabaseHandler);
    }

    @Override
    public void terminate() {
        if(dbCon != null) {
            try {
                dbCon.close();
            } catch (SQLException se) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Error whilst trying to close database connection!", se);
            }
        }
        super.getAsyncExecutor().shutdown();
    }

    protected abstract void createDatabase();
    protected abstract void createTables();

    protected void connect() {
        FutureTask<Void> createDBConnection = new FutureTask<>(() -> {
            String dbURL = "jdbc:sqlite:" + dbPath;
            try {
                dbCon = DriverManager.getConnection(dbURL);
                if(dbCon != null) {
                    super.getPlugin().getLogger().log(Level.INFO, "Successfully connected to Database.");
                }
            } catch (Exception e) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Couldn't connect to Database", e);
            }
            return null;
        });
        super.getAsyncExecutor().execute(createDBConnection);
    }

    protected String getDbPath() {
        return this.dbPath;
    }
    protected Connection getDbCon() {
        return this.dbCon;
    }

}
