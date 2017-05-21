/*
 * Copyright 2016-2017 Peter Franzen. All rights reserved.
 *
 * Licensed under the Apache License v2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
package org.myire.concurrent;

import java.security.Permission;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


/**
 * Base unit tests for {@code DefaultThreadFactory} and subclasses.
 *
 * @author <a href="mailto:peter@myire.org">Peter Franzen</a>
 */
abstract public class AbstractDefaultThreadFactoryTest
{
    /**
     * Create a {@code DefaultThreadFactory} of the correct type to use in the tests.
     *
     * @param pBaseName             The base name to pass to the constructor.
     * @param pAppendThreadNumber   The append flag to pass to the constructor.
     *
     * @return  A new {@code DefaultThreadFactory}.
     */
    abstract protected DefaultThreadFactory createTestInstance(String pBaseName, boolean pAppendThreadNumber);

    /**
     * Create a {@code DefaultThreadFactory} of the correct type to use in the tests.
     *
     * @return  A new {@code DefaultThreadFactory} with a default base name that does not append
     *          thread numbers to the thread names.
     */
    private DefaultThreadFactory createTestInstance()
    {
        return createTestInstance("AnyName", false);
    }


    /**
     * The names of the created threads should be the base name only when the factory is constructed
     * with the append flag set to false.
     */
    @Test
    public void threadNameIsBaseNameWhenAppendIsFalse()
    {
        // Given
        String aBaseName = "name";
        DefaultThreadFactory aFactory = createTestInstance(aBaseName, false);

        // When
        Thread aThread1 = aFactory.newThread(null);
        Thread aThread2 = aFactory.newThread(null);

        // Then
        assertEquals(aBaseName, aThread1.getName());
        assertEquals(aBaseName, aThread2.getName());
    }


    /**
     * The names of the created threads should be the base name with the thread number appended when
     * the factory is constructed with the append flag set to true.
     */
    @Test
    public void threadNameEndsWithNumberWhenAppendIsTrue()
    {
        // Given
        String aBaseName = "MyThread-";
        DefaultThreadFactory aFactory = createTestInstance(aBaseName, true);

        // When
        Thread aThread1 = aFactory.newThread(null);
        Thread aThread2 = aFactory.newThread(null);

        // Then
        assertEquals(aBaseName + "1", aThread1.getName());
        assertEquals(aBaseName + "2", aThread2.getName());
    }


    /**
     * The {@code Runnable} passed to the thread factory should be executed by the created threads.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void targetIsExecutedByThreads() throws InterruptedException
    {
        // Given
        DefaultThreadFactory aFactory = createTestInstance();
        AtomicBoolean aFlag = new AtomicBoolean(false);

        // When
        Thread aThread = aFactory.newThread(() -> aFlag.set(true));
        aThread.start();
        aThread.join();

        // Then
        assertTrue(aFlag.get());
    }


    /**
     * The priority of the created threads should be inherited from the thread that calls the thread
     * factory.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void threadPriorityIsInheritedFromCreatingThread() throws InterruptedException
    {
        threadPriorityIsInheritedFromCreatingThread(-1);
        threadPriorityIsInheritedFromCreatingThread(Thread.MIN_PRIORITY);
        threadPriorityIsInheritedFromCreatingThread(Thread.MAX_PRIORITY);
    }


    /**
     * The priority of the created threads should be inherited from the thread that calls the thread
     * factory.
     *
     * @param pPriority The priority to test. If this values is not greater than 0, the thread's
     *                  default priority is tested.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    private void threadPriorityIsInheritedFromCreatingThread(int pPriority) throws InterruptedException
    {
        // Given
        ThreadCreatingThread aCreatorThread = newThreadCreatingThread();
        if (pPriority > 0)
            aCreatorThread.setPriority(pPriority);
        else
            pPriority = aCreatorThread.getPriority();

        // When
        aCreatorThread.startAndJoin();

        // Then
        assertEquals(pPriority, aCreatorThread.getCreatedThread().getPriority());
    }


    /**
     * The thread group of the created threads should be the same group as the group of the thread
     * that created the thread factory, given that no security manager is in place.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void threadGroupIsInheritedFromFactory() throws InterruptedException
    {
        // Given
        ThreadGroup aGroup = Thread.currentThread().getThreadGroup();
        ThreadCreatingThread aCreatorThread = newThreadCreatingThread();

        // When
        aCreatorThread.startAndJoin();

        // Then
        assertSame(aGroup, aCreatorThread.getCreatedThread().getThreadGroup());
    }


    /**
     * The thread group of the created threads should be the same group as the group of the
     * installed security manager.
     *
     * @throws InterruptedException if the test thread is interrupted.
     */
    @Test
    public void threadGroupIsInheritedFromSecurityManager() throws InterruptedException
    {
        // Given there is an installed security manager.
        ThreadGroup aGroup = new ThreadGroup("dad");
        SecurityManager aSavedSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new ThreadGroupSecurityManager(aGroup));

        try
        {
            // Given
            ThreadCreatingThread aCreatorThread = newThreadCreatingThread();

            // When
            aCreatorThread.startAndJoin();

            // Then
            assertSame(aGroup, aCreatorThread.getCreatedThread().getThreadGroup());
        }
        finally
        {
            // Restore the previous security manager.
            System.setSecurityManager(aSavedSecurityManager);
        }
    }


    /**
     * The method {@code getNumCreatedThreads} should return the number of created threads.
     */
    @Test
    public void getNumCreatedThreadsReturnsCorrectValue()
    {
        // Given
        DefaultThreadFactory aFactory = createTestInstance();

        // Then
        for (int i=0; i<100; i++)
        {
            aFactory.newThread(null);
            assertEquals(i+1, aFactory.getNumCreatedThreads());
        }
    }


    protected ThreadCreatingThread newThreadCreatingThread()
    {
        return new ThreadCreatingThread(createTestInstance());
    }


    /**
     * A thread that calls {@code ThreadFactory::newThread} in its run method. The created thread
     * can be obtained by calling {@link #getCreatedThread()}.
     */
    static class ThreadCreatingThread extends Thread
    {
        private final ThreadFactory fThreadFactory;
        private volatile Thread fCreatedThread;

        ThreadCreatingThread(ThreadFactory pThreadFactory)
        {
            fThreadFactory = pThreadFactory;
        }

        @Override
        public void run()
        {
            fCreatedThread = fThreadFactory.newThread(null);
        }

        void startAndJoin() throws InterruptedException
        {
            start();
            join();
        }

        Thread getCreatedThread()
        {
            return fCreatedThread;
        }
    }


    /**
     * SecurityManager that returns a specific thread group in {@code getThreadGroup()}.
     */
    static private class ThreadGroupSecurityManager extends SecurityManager
    {
        private final ThreadGroup fThreadGroup;

        ThreadGroupSecurityManager(ThreadGroup pThreadGroup) { fThreadGroup = pThreadGroup; }

        @Override
        public ThreadGroup getThreadGroup() { return fThreadGroup; }

        // Must override checkPermission() to allow execution.
        @Override
        public void checkPermission(Permission pPerm, Object pContext) { /* Empty */ }

        @Override
        public void checkPermission(Permission pPerm) { /* Empty */ }
    }
}
