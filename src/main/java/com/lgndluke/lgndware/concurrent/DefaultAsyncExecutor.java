package com.lgndluke.lgndware.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultAsyncExecutor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private <T> void execute(@NotNull RunnableFuture<T> task) {
        executorService.execute(task);
    }

    public boolean executeFuture(Logger logger, RunnableFuture<Boolean> task, long timeout, TimeUnit unit) {
        execute(task);
        try {
            return task.get(timeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task timed out.", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "[LGNDWARE]: Task was interrupted", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task execution failed", ee);
        }
        return false;
    }

    public <T> List<T> fetchExecutionResultAsList(Logger logger, RunnableFuture<List<T>> task, long timeout, TimeUnit unit) {
        execute(task);
        try {
            return task.get(timeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task timed out.", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "[LGNDWARE]: Task was interrupted", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task execution failed", ee);
        }
        return null;
    }

    public <T> T fetchExecutionResult(Logger logger, RunnableFuture<T> task, long timeout, TimeUnit unit) {
        execute(task);
        try {
            return task.get(timeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task timed out.", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "[LGNDWARE]: Task was interrupted", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Task execution failed", ee);
        }
        return null;
    }

    public boolean isShutdown() {
        return this.executorService.isTerminated();
    }
    public void shutdown() {
        this.executorService.shutdown();
    }

}
