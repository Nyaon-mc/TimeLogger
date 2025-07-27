package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import com.github.nyaon08.rtustudio.timelogger.util.TimeFormatter;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.configuration.translation.message.MessageTranslation;
import kr.rtuserver.framework.bukkit.api.format.ComponentFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class CheckCommand extends RSCommand<TimeLogger> {

    private final PlayTimeManager playTimeManager;

    public CheckCommand(TimeLogger plugin) {
        super(plugin, "check");
        this.playTimeManager = plugin.getPlayTimeManager();
    }

    @Override
    protected boolean execute(RSCommandData data) {
        UUID providerUuid = provider().getUniqueId(data.args(1));
        OfflinePlayer target = providerUuid == null
                ? Bukkit.getOfflinePlayer(data.args(1))
                : Bukkit.getOfflinePlayer(providerUuid);

        if (!target.hasPlayedBefore()) {
            chat().announce(message().getCommon(player(), String.valueOf(MessageTranslation.NOT_FOUND_OFFLINE_PLAYER)));
            return true;
        }

        TimeFormatter.TimeResult result = TimeFormatter.getTotalPlayTime(target.getUniqueId(), playTimeManager);

        String hourMsg = message().get(player(), "format.hour").replace("[hour]", String.valueOf(result.hours()));
        String minuteMsg = message().get(player(), "format.minute").replace("[minute]", String.valueOf(result.minutes()));
        String secondMsg = message().get(player(), "format.second").replace("[second]", String.valueOf(result.seconds()));

        String playerName = provider().getName(target.getUniqueId());
        if (playerName == null || playerName.isEmpty()) {
            String fallbackName = target.getName();
            playerName = fallbackName != null ? fallbackName : "Unknown";
        }

        Component message = ComponentFormatter.mini(
                message().get(player(), "playtime")
                        .replace("[player]", playerName)
                        .replace("[time]", hourMsg + minuteMsg + secondMsg));

        chat().announce(message);
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        if (data.length(2)) return provider().names();
        return List.of();
    }

}
