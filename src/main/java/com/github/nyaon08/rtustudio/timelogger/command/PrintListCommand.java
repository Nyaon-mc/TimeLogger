package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.configuration.TimeLoggerConfig;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import com.github.nyaon08.rtustudio.timelogger.util.TimeFormatter;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PrintListCommand extends RSCommand<TimeLogger> {

    private final TimeLoggerConfig timeLoggerConfig;
    private final PlayTimeManager playTimeManager;

    public PrintListCommand(TimeLogger plugin) {
        super(plugin, "all", PermissionDefault.OP);
        this.timeLoggerConfig = plugin.getTimeLoggerConfig();
        this.playTimeManager = plugin.getPlayTimeManager();
    }

    @Override
    protected boolean execute(RSCommandData data) {
        List<Map.Entry<UUID, Long>> sorted = playTimeManager.getAllPlayers()
                .stream()
                .map(uuid -> Map.entry(uuid, playTimeManager.getPlayTime(uuid)))
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .toList();

        Set<String> playtimeCheckSet = timeLoggerConfig.getPlaytimeCheckList().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        chat().announce(message().get(player(), "rank"));

        int rank = 1;
        for (Map.Entry<UUID, Long> entry : sorted) {
            UUID uuid = entry.getKey();

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            String name = offlinePlayer.getName();
            if (name == null || !playtimeCheckSet.contains(name.toLowerCase())) {
                continue;
            }

            TimeFormatter.TimeResult result = TimeFormatter.getTotalPlayTime(uuid, playTimeManager);

            String hourMsg = message().get(player(), "format.hour").replace("[hour]", String.valueOf(result.hours()));
            String minuteMsg = message().get(player(), "format.minute").replace("[minute]", String.valueOf(result.minutes()));
            String secondMsg = message().get(player(), "format.second").replace("[second]", String.valueOf(result.seconds()));

            chat().announce(rank + ". " + name + " : " + hourMsg + minuteMsg + secondMsg);
            rank++;
        }
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        return List.of();
    }

}
