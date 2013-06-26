/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

/**
 * Logger.
 * 
 * @author Hai Bison
 * @since v1.6.3 beta
 */
public class L {

    /**
     * Prints log to standard output console.
     * 
     * @param tag
     *            the tag.
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void out(String tag, String msg, Object... args) {
        System.out.printf("[%s] %s\n", tag, String.format(msg, args));
    }// out()

    /**
     * Prints log to error output console.
     * 
     * @param tag
     *            the tag.
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void err(String tag, String msg, Object... args) {
        System.err.printf("[%s] %s\n", tag, String.format(msg, args));
    }// err()

    /**
     * Prints debug log.
     * 
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void d(String msg, Object... args) {
        out("DEBUG", msg, args);
    }// d()

    /**
     * Prints information log.
     * 
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void i(String msg, Object... args) {
        out("INFO", msg, args);
    }// i()

    /**
     * Prints "verbose" log.
     * 
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void v(String msg, Object... args) {
        out("VERBOSE", msg, args);
    }// v()

    /**
     * Prints error log.
     * 
     * @param msg
     *            the message.
     * @param args
     *            the objects to be formatted with {@code msg}.
     */
    public static void e(String msg, Object... args) {
        err("ERROR", msg, args);
    }// e()
}
