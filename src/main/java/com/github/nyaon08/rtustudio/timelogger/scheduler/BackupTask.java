package com.github.nyaon08.rtustudio.timelogger.scheduler;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.configuration.TimeLoggerConfig;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import com.github.nyaon08.rtustudio.timelogger.util.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class BackupTask extends BukkitRunnable {

    private final TimeLogger plugin;
    private final TimeLoggerConfig timeLoggerConfig;
    private final PlayTimeManager playTimeManager;

//    private final NameProvider nameProvider;

    public BackupTask(TimeLogger plugin) {
        this.plugin = plugin;
        this.timeLoggerConfig = plugin.getTimeLoggerConfig();
        this.playTimeManager = plugin.getPlayTimeManager();
//        this.nameProvider = plugin.getFramework().getProviders().getName();
    }

    @Override
    public void run() {
        Map<String, Object> config = timeLoggerConfig.getBackupConfig();

        if (!(boolean) config.get("enabled")) {
            this.cancel();
            return;
        }

        String dayString = config.get("day-of-week").toString();
        int hour = (int) config.get("hour");
        int minute = (int) config.get("minute");

        DayOfWeek targetDay;
        try {
            targetDay = DayOfWeek.valueOf(dayString.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Invalid day of week: " + dayString);
            return;
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        if (now.getDayOfWeek() == targetDay && now.getHour() == hour && now.getMinute() == minute) {
            plugin.getLogger().info("Starting weekly backup...");

            playTimeManager.save();
            backupPlaytimeData();

            if ((boolean) config.get("restore")) {
                plugin.getLogger().info("Restoring playtime data...");
                playTimeManager.resetAllPlayTime();
            }

            plugin.getLogger().info("Weekly backup completed.");
        }
    }

    private void backupPlaytimeData() {
        try {
            Path backupDir = plugin.getDataFolder().toPath().resolve("weekly_playtime_backups");
            if (!Files.exists(backupDir)) Files.createDirectories(backupDir);

            String filename = LocalDate.now() + ".txt";
            File backupFile = backupDir.resolve(filename).toFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
                for (UUID uuid : playTimeManager.getAllPlayers()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    String name = player.getName() != null ? player.getName() : "Unknown";
                    TimeFormatter.TimeResult formatted = TimeFormatter.getTotalPlayTime(uuid, playTimeManager);
                    writer.write(name + " (" + uuid + "): " + formatted.hours() + "h " + formatted.minutes() + "m " + formatted.seconds() + "s");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to backup playtime data: " + e.getMessage());
        }
    }

}


