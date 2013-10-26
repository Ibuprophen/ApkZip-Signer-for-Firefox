/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.services;

import group.pals.desktop.app.apksigner.services.INotification.Message;
import group.pals.desktop.app.apksigner.utils.Texts;

import java.util.ArrayList;
import java.util.List;

/**
 * The base thread.
 * 
 * @author Hai Bison
 * @since v1.6.9 beta
 */
public class BaseThread extends Thread {

    /**
     * The thread has done.
     */
    public static final int MSG_DONE = 0;

    /**
     * There is an information.
     */
    public static final int MSG_INFO = -1;

    /**
     * A warning.
     */
    public static final int MSG_WARNING = -2;

    /**
     * An error.
     */
    public static final int MSG_ERROR = -3;

    /**
     * All client notifications.
     */
    private final List<INotification> mNotifications = new ArrayList<INotification>();

    /**
     * Adds new notification.
     * 
     * @param notification
     *            the notification.
     * @return the instance of this thread, to allow chaining multiple calls
     *         into a single statement.
     */
    public BaseThread addNotification(INotification notification) {
        mNotifications.add(notification);
        return this;
    }// addNotification()

    /**
     * Removes a notification.
     * 
     * @param notification
     *            the notification to remove.
     * @return {@code true} if removal succeeded, {@code false} otherwise
     *         (probably the notification does not exist).
     */
    public boolean removeNotification(INotification notification) {
        return mNotifications.remove(notification);
    }// removeNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @param obj
     *            your arbitrary object.
     * @param shortMsg
     *            the short message.
     * @param detailedMsg
     *            the detailed message.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId, Object obj, String shortMsg,
            String detailedMsg) {
        if (mNotifications.isEmpty())
            return false;

        Message msg = new Message();
        msg.id = msgId;
        msg.obj = obj;
        msg.shortMessage = shortMsg;
        msg.detailedMessage = detailedMsg;

        for (INotification notification : mNotifications)
            if (notification.onMessage(msg))
                return true;

        return false;
    }// sendNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @param obj
     *            your arbitrary object.
     * @param shortMsg
     *            the short message.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId, Object obj, String shortMsg) {
        return sendNotification(msgId, obj, shortMsg, Texts.NULL);
    }// sendNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @param obj
     *            your arbitrary object.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId, Object obj) {
        return sendNotification(msgId, obj, Texts.NULL);
    }// sendNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @param shortMsg
     *            the short message.
     * @param detailedMsg
     *            the detailed message.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId, String shortMsg,
            String detailedMsg) {
        return sendNotification(msgId, null, shortMsg, detailedMsg);
    }// sendNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @param shortMsg
     *            the short message.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId, String shortMsg) {
        return sendNotification(msgId, null, shortMsg, Texts.NULL);
    }// sendNotification()

    /**
     * Sends notification to all listeners.
     * 
     * @param msgId
     *            the message ID.
     * @return {@code true} if any of the listeners handled the message,
     *         {@code false} otherwise.
     */
    protected boolean sendNotification(int msgId) {
        return sendNotification(msgId, Texts.NULL);
    }// sendNotification()

}
