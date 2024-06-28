package com.lgndluke.lgndware.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncExecutor implements Executor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void execute(@NotNull Runnable command) {
        executorService.execute(command);
    }

    public <T> List<T> fetchExecutionResultAsList(Logger logger, RunnableFuture<List<T>> task, long timeout, TimeUnit unit) {
        execute(task);
        try {
            return task.get(timeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "Task timed out.", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Task was interrupted", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "Task execution failed", ee);
        }
        return null;
    }

    public <T> T fetchExecutionResult(Logger logger, RunnableFuture<T> task, long timeout, TimeUnit unit) {
        execute(task);
        try {
            return task.get(timeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "Task timed out.", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Task was interrupted", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "Task execution failed", ee);
        }
        return null;
    }

    public void shutdown() {
        executorService.shutdown();
    }

}
