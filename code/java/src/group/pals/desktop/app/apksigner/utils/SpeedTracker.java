/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.util.ArrayList;

/**
 * A tracker of speed.
 * <p>
 * <b>Notes:</b> Use the speeds in your own unit (bytes/ seconds, bytes/
 * nanosecond...)
 * </p>
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class SpeedTracker {

    // private static final String CLASSNAME = SpeedTracker.class.getName();

    /**
     * The check-point.
     * 
     * @author Hai Bison
     * @since v1.6 beta
     */
    public static class CheckPoint {

        private final double tick;
        private final double speed;

        /**
         * Creates new instance.
         * 
         * @param speed
         *            the instantaneous speed, in <i>your unit</i> (e.g bytes/
         *            second or bytes/ nanosecond).
         */
        public CheckPoint(double speed) {
            this(System.nanoTime(), speed);
        }// CheckPoint()

        /**
         * Creates new instance.
         * 
         * @param tick
         *            current time in nanoseconds.
         * @param speed
         *            the instantaneous speed, in <i>your unit</i> (e.g bytes/
         *            second or bytes/ nanosecond).
         */
        public CheckPoint(double tick, double speed) {
            this.tick = tick;
            this.speed = speed;
        }// CheckPoint()
    }// CheckPoint

    /**
     * Max check-points to be stored.
     */
    private int mMaxCheckPoints = 500;

    /**
     * Max period time for calculating speed, in nanoseconds.
     */
    private double mMaxPeriod = 5e9;

    private final ArrayList<CheckPoint> mCheckPoints = new ArrayList<SpeedTracker.CheckPoint>();

    /**
     * Creates new instance.
     */
    public SpeedTracker() {
        //
    }// SpeedTracker()

    /**
     * Adds new instantaneous speed.
     * 
     * @param speed
     *            the instantaneous speed.
     */
    public void add(double speed) {
        mCheckPoints.add(new CheckPoint(speed));
        if (mCheckPoints.size() > mMaxCheckPoints)
            mCheckPoints.remove(0);
    }// add()

    /**
     * Clears all data.
     */
    public void clear() {
        mCheckPoints.clear();
    }// clear()

    /**
     * Calculates current instantaneous speed, also removes all of old data (old
     * check-points).
     * 
     * @return the current instantaneous speed.
     */
    public synchronized double calcInstantaneousSpeed() {
        try {
            final double tick = System.nanoTime();
            CheckPoint cp;
            double totalSpeed = 0;
            for (int i = mCheckPoints.size() - 1; i >= 0; i--) {
                cp = mCheckPoints.get(i);
                if (tick - cp.tick <= mMaxPeriod) {
                    totalSpeed += cp.speed;
                } else {
                    mCheckPoints.subList(0, i + 1).clear();
                    break;
                }
            }

            return mCheckPoints.isEmpty() ? 0 : totalSpeed
                    / mCheckPoints.size();
        } catch (Throwable t) {
            return 0;
        }
    }// calcInstantaneousSpeed()
}
