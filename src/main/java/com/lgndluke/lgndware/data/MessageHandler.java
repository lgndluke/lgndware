package com.lgndluke.lgndware.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class handles operations with the "messages.yml" file.
 * @author lgndluke
 **/
public class MessageHandler extends AbstractFileHandler {

    public MessageHandler(JavaPlugin plugin) {
        super(plugin, "messages.yml");
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

}
