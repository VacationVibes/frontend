package com.github.sviatoslavslysh.vacationvibes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public static String getTimeAgo(String createdAt) {
        SimpleDateFormat sdf;

        if (createdAt.endsWith("Z")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.US);
        }

        try {
            Date commentDate = sdf.parse(createdAt);
            long diffInMillis = System.currentTimeMillis() - commentDate.getTime();

            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            if (diffInMinutes < 1) {
                return "Just now";
            } else if (diffInHours < 1) {
                return diffInMinutes + " minutes ago";
            } else if (diffInDays < 1) {
                return diffInHours + " hours ago";
            } else {
                return diffInDays + " days ago";
            }
        } catch (ParseException e) {
            try {
                SimpleDateFormat alternativeSdf;
                if (createdAt.endsWith("Z")) {
                    alternativeSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    alternativeSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                } else {
                    alternativeSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
                }

                Date commentDate = alternativeSdf.parse(createdAt);
                long diffInMillis = System.currentTimeMillis() - commentDate.getTime();

                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                if (diffInMinutes < 1) {
                    return "Just now";
                } else if (diffInHours < 1) {
                    return diffInMinutes + " minutes ago";
                } else if (diffInDays < 1) {
                    return diffInHours + " hours ago";
                } else {
                    return diffInDays + " days ago";
                }
            } catch (ParseException e2) {
                e2.printStackTrace();
                return "Invalid date format";
            }
        }
    }
}