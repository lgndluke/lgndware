package com.lgndluke.lgndware.concurrent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

import static junit.framework.TestCase.*;

public class AsyncExecutorTest {

    private AsyncExecutor asyncExecutor;
    private FutureTask<String> stringTask;
    private Logger logger;

    @Before
    public void setUp() {
        this.asyncExecutor = new AsyncExecutor();
        this.stringTask = new FutureTask<>(() -> {
            Thread.sleep(100);
            return "Task Completed";
        });
        this.logger = Logger.getLogger(AsyncExecutorTest.class.getName());
    }

    @Test
    public void execute() {
        asyncExecutor.execute(stringTask);
        try {
            assertEquals("Task Completed", stringTask.get(1, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            fail("Task execution failed: " + e.getMessage());
        }
    }

    @Test
    public void fetchExecutionResultAsList() {
        RunnableFuture<List<String>> task = new FutureTask<>(() -> {
            List<String> result = new ArrayList<>();
            result.add("Task Completed");
            return result;
        });

        List<String> result = asyncExecutor.fetchExecutionResultAsList(logger, task, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task Completed", result.get(0));
    }

    @Test
    public void fetchExecutionResult() {
        RunnableFuture<String> task = new FutureTask<>(() -> {
            Thread.sleep(100);
            return "Task Completed";
        });

        String result = asyncExecutor.fetchExecutionResult(logger, task, 1, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals("Task Completed", result);
    }

    @Test
    public void fetchExecutionResult_Timeout() {
        RunnableFuture<String> task = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return "Task Completed";
        });

        String result = asyncExecutor.fetchExecutionResult(logger, task, 1, TimeUnit.SECONDS);
        assertNull(result);
    }

    @Test
    public void fetchExecutionResult_Interrupted() {
        RunnableFuture<String> task = new FutureTask<>(() -> {
            Thread.sleep(1000);
            return "Task Completed";
        });

        Thread taskThread = new Thread(task);
        taskThread.start();

        taskThread.interrupt();

        try {
            taskThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String result = asyncExecutor.fetchExecutionResult(logger, task, 1, TimeUnit.SECONDS);
        assertNull(result);
    }

    @Test
    public void shutdown() {
        asyncExecutor.shutdown();
    }
}