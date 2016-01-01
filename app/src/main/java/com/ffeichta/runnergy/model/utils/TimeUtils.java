package com.ffeichta.runnergy.model.utils;

/**
 * Created by Fabian on 29.12.2015.
 */
public class TimeUtils {
    public final static long ONE_SECOND = 1000;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;

    /**
     * Converts a duration in seconds to a human-readable format
     * For example: convertDurationToString(661) --> "11min, 1sec"
     */
    public static String convertDurationToString(int durationInSeconds) {
        StringBuffer ret = new StringBuffer();
        long duration = durationInSeconds * 1000;
        long temp;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                ret.append(temp).append("d");
                if (duration >= ONE_MINUTE) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                ret.append(temp).append("h");
                if (duration >= ONE_MINUTE) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                ret.append(temp).append("min");
                if (duration >= ONE_SECOND) {
                    ret.append(", ");
                }
            }
            temp = duration / ONE_SECOND;
            if (temp > 0) {
                ret.append(temp).append("sec");
            }
        } else {
            ret.append("0sec");
        }
        return ret.toString();
    }
}