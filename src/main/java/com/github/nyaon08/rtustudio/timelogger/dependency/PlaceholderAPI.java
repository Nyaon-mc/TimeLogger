package com.github.nyaon08.rtustudio.timelogger.dependency;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import com.github.nyaon08.rtustudio.timelogger.util.TimeFormatter;
import kr.rtuserver.framework.bukkit.api.integration.RSPlaceholder;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPI extends RSPlaceholder<TimeLogger> {

    private final PlayTimeManager playTimeManager;

    public PlaceholderAPI(TimeLogger plugin) {
        super(plugin);
        this.playTimeManager = plugin.getPlayTimeManager();
    }

    @Override
    public String request(OfflinePlayer offlinePlayer, String[] params) {
        return switch (params[0]) {
            case "total" -> String.valueOf(playTimeManager.getPlayTime(offlinePlayer.getUniqueId()));
            case "hour" ->
                    String.valueOf(TimeFormatter.getTotalPlayTime(offlinePlayer.getUniqueId(), playTimeManager).hours());
            case "minute" ->
                    String.valueOf(TimeFormatter.getTotalPlayTime(offlinePlayer.getUniqueId(), playTimeManager).minutes());
            case "second" ->
                    String.valueOf(TimeFormatter.getTotalPlayTime(offlinePlayer.getUniqueId(), playTimeManager).seconds());
            default -> String.valueOf(0);
        };
    }

}
