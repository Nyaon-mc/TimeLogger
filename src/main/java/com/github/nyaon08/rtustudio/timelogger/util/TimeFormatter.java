package com.github.nyaon08.rtustudio.timelogger.util;

import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;

import java.util.UUID;

public class TimeFormatter {

    public static TimeResult getTotalPlayTime(UUID uuid, PlayTimeManager playTimeManager) {
        long total = playTimeManager.getPlayTime(uuid);
        Long loginTime = playTimeManager.getLoginTimes().get(uuid);

        if (loginTime != null) {
            long now = System.currentTimeMillis();
            total += (now - loginTime) / 1000;
        }

        long hours = total / 3600;
        long minutes = (total % 3600) / 60;
        long seconds = total % 60;

        return new TimeResult(hours, minutes, seconds);
    }

    public record TimeResult(long hours, long minutes, long seconds) {
    }
}