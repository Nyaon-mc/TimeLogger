package com.github.nyaon08.rtustudio.timelogger.manager;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import kr.rtuserver.framework.bukkit.api.platform.JSON;
import kr.rtuserver.framework.bukkit.api.storage.Storage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayTimeManager {

    private final TimeLogger plugin;

    @Getter
    private final Map<UUID, Long> loginTimes = new HashMap<>();

    public void startAutoSaveTask() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        long saveDBPeriod = plugin.getTimeLoggerConfig().getSaveDBPeriod() * 60 * 20L;
        scheduler.runTaskTimerAsynchronously(plugin, this::save, saveDBPeriod, saveDBPeriod);
    }

    public void save() {
        Storage storage = plugin.getStorage();
        for (UUID uuid : loginTimes.keySet()) {
            storage.set("playtime", JSON.of("uuid", uuid.toString()), JSON.of("time", loginTimes.get(uuid)));
        }
    }

    public void initPlayer(UUID uuid) {
        Storage storage = plugin.getStorage();
        storage.get("playtime", JSON.of("uuid", uuid.toString())).thenAccept(result -> {
            if (result.isEmpty() || result.getFirst().isJsonNull()) {
                storage.add("playtime", JSON.of("uuid", uuid.toString()).append("time", 0L));
            }
        });
    }

    public void addPlayTime(UUID uuid, long playTime) {
        Storage storage = plugin.getStorage();
        storage.get("playtime", JSON.of("uuid", uuid.toString())).thenAccept(result -> {
            long totalTime = playTime + (result.isEmpty() || result.getFirst().isJsonNull() ? 0 : result.getFirst().get("time").getAsLong());
            storage.set("playtime", JSON.of("uuid", uuid.toString()), JSON.of("time", totalTime)).join();
        });
    }

    public long getPlayTime(UUID uuid) {
        Storage storage = plugin.getStorage();
        return storage.get("playtime", JSON.of("uuid", uuid.toString())).thenApply(result -> {
            if (result.isEmpty() || result.getFirst().isJsonNull()) return 0L;
            return result.getFirst().get("time").getAsLong();
        }).join();
    }

    public void resetPlayTime(UUID uuid) {
        Storage storage = plugin.getStorage();
        storage.set("playtime", JSON.of("uuid", uuid.toString()), JSON.of("time", 0L)).join();
        loginTimes.put(uuid, System.currentTimeMillis());
    }

    public void resetAllPlayTime() {
        Storage storage = plugin.getStorage();
        storage.set("playtime", JSON.of(), JSON.of()).join();
        loginTimes.replaceAll((u, v) -> System.currentTimeMillis());
    }

    public List<UUID> getAllPlayers() {
        Storage storage = plugin.getStorage();
        return storage.get("playtime", JSON.of()).thenApply(result -> {
            if (result.isEmpty() || result.getFirst().isJsonNull()) return loginTimes.keySet().stream().toList();
            return result.stream().map(obj -> UUID.fromString(obj.get("uuid").getAsString())).toList();
        }).join();
    }

}
