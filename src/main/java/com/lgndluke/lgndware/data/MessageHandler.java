package com.lgndluke.lgndware.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * This class handles operations with the "messages.yml" file.
 * @author lgndluke
 **/
public class MessageHandler extends AbstractFileHandler {

    public MessageHandler(JavaPlugin plugin) {
        super(plugin, "messages.yml");
    }

    @Override
    public boolean initialize() {
        FutureTask<Boolean> initAbstractFileHandler = new FutureTask<>(() -> {
            createFile();
            super.getFileConfig().load(super.getFile());;
            super.getFileConfig().options().copyDefaults(true);
            save();
            return true;
        });
        return super.getAsyncExecutor().executeFuture(super.getPlugin().getLogger(), initAbstractFileHandler, 10, TimeUnit.SECONDS);
    }

    /**
     * Asynchronous getter for String value from 'messages.yml' file.
     * @param value — holding the message requested from 'message.yml' file.
     * @return requested message value as String.
     **/
    public String getMessageAsString(String value) {
        RunnableFuture<String> getMsgAsString = new FutureTask<>(() -> PlainTextComponentSerializer.plainText().serialize(getMessageAsComponent(value)));
        return super.getAsyncExecutor().fetchExecutionResult(super.getPlugin().getLogger(), getMsgAsString, 10, TimeUnit.SECONDS);
    }

    /**
     * Asynchronous getter for String value from 'messages.yml' file as Component.
     * @param value — holding the message requested from 'message.yml' file.
     * @return requested message value as Component.
     **/
    public Component getMessageAsComponent(String value) {
        RunnableFuture<Component> getMsgAsComponent = new FutureTask<>(() -> MiniMessage.miniMessage().deserialize(Objects.requireNonNull(super.getFileConfig().getString(value))));
        return super.getAsyncExecutor().fetchExecutionResult(super.getPlugin().getLogger(), getMsgAsComponent, 10, TimeUnit.SECONDS);
    }

    /**
     * Asynchronous getter for String values from 'messages.yml' file that are stored as lists.
     * @param value — holding the message requested from 'message.yml' file.
     * @return requested message value as Component array list.
     **/
    public List<Component> getMessagesAsComponentList(String value) {
        RunnableFuture<List<Component>> getMsgAsComponentList = new FutureTask<>(() -> {
            List<Component> results = new ArrayList<>();
            for(String listVal : super.getFileConfig().getStringList(value)) {
                results.add(MiniMessage.miniMessage().deserialize(listVal));
            }
            return results;
        });
        return super.getAsyncExecutor().fetchExecutionResultAsList(super.getPlugin().getLogger(), getMsgAsComponentList, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void createFile() {
        if(!super.getFile().exists()) {
            try {
                Files.copy(Objects.requireNonNull(super.getPlugin().getResource("messages.yml")), super.getFile().toPath());
                super.getPlugin().getLogger().log(Level.INFO, "Successfully created " + super.getFile().getName() + " file.");
            } catch (IOException io) {
                super.getPlugin().getLogger().log(Level.SEVERE, "Copying defaults into 'messages.yml' failed!", io);
            }
        }
    }

}
