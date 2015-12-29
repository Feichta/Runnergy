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
     * converts time (in milliseconds) to human-readable format
     * "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String convertDurationToString(int durationInSeconds) {
        StringBuffer res = new StringBuffer();
        long duration = durationInSeconds * 1000;
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append("d")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }
            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append("h")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }
            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append("min").append(duration >= ONE_SECOND ? ", " : "");
            }
            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append(temp).append("sec");
            }
            return res.toString();
        } else {
            return "0sec";
        }
    }
}