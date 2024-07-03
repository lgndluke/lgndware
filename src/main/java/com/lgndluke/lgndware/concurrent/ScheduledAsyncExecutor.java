package com.lgndluke.lgndware.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduledAsyncExecutor {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private <T> void execute(@NotNull RunnableFuture<T> task, long timeout, TimeUnit unit) {
        scheduledExecutorService.schedule(task, timeout, unit);
    }

    public boolean executeFutureLater(Logger logger, RunnableFuture<Boolean> task, long delay, long additionalTimeout, TimeUnit unit) {
        execute(task, delay, TimeUnit.SECONDS);
        try {
            return task.get(additionalTimeout, unit);
        } catch (TimeoutException te) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Scheduled-Task timed out!", te);
            task.cancel(true);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "[LGNDWARE]: Scheduled-Task was interrupted!", ie);
        } catch (ExecutionException ee) {
            logger.log(Level.SEVERE, "[LGNDWARE]: Scheduled-Task execution failed!", ee);
        }
        return false;
    }

    public boolean isShutdown() {
        return this.scheduledExecutorService.isShutdown();
    }
    public void shutdown() {
        this.scheduledExecutorService.shutdown();
    }

}
