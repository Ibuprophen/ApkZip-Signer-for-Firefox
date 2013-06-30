/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.services;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The background service manager.
 * 
 * @author Hai Bison
 * @since v1.6.9 beta
 */
public class ServiceManager {

    /**
     * This holds all active threads mapping to their notifications.
     */
    private static final Map<BaseThread, INotification> THREADS = new LinkedHashMap<BaseThread, INotification>();

    /**
     * Register a thread.
     * 
     * @param thread
     *            the thread to register.
     */
    public static void registerThread(final BaseThread thread) {
        INotification notification = new INotification() {

            @Override
            public boolean onMessage(Message msg) {
                if (msg.id == BaseThread.MSG_DONE)
                    THREADS.remove(thread);
                return false;
            }// onMessage()
        };
        thread.addNotification(notification);

        THREADS.put(thread, notification);
    }// registerThread()

    /**
     * Unregister a thread.
     * 
     * @param thread
     *            the thread to unregister.
     * @return {@code true} if the {@code thread} existed and has been
     *         unregistered. {@code false} otherwise.
     */
    public static synchronized boolean unregisterThread(BaseThread thread) {
        INotification notification = THREADS.remove(thread);
        if (notification != null)
            thread.removeNotification(notification);
        return notification != null;
    }// unregisterThread()

    /**
     * Gets the <i>snapshot</i> list of active threads. The order of threads are
     * kept as-is like when they were registered.
     * 
     * @return the <i>snapshot</i> list of active threads.
     */
    public static List<BaseThread> getActiveThreads() {
        return Arrays.asList(THREADS.keySet().toArray(new BaseThread[0]));
    }// getActiveThreads()
}
