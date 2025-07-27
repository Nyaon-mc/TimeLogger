package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.configuration.translation.message.MessageTranslation;
import kr.rtuserver.framework.bukkit.api.format.ComponentFormatter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class ResetCommand extends RSCommand<TimeLogger> {

    private final PlayTimeManager playTimeManager;

    public ResetCommand(TimeLogger plugin) {
        super(plugin, "reset");
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

        playTimeManager.resetPlayTime(target.getUniqueId());

        String playerName = provider().getName(target.getUniqueId());
        if (playerName == null || playerName.isEmpty()) {
            String fallbackName = target.getName();
            playerName = fallbackName != null ? fallbackName : "Unknown";
        }

        chat().announce(ComponentFormatter.mini(message().get(player(), "reset").replace("[player]", playerName)));
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        if (data.length(2)) return provider().names();
        return List.of();
    }

}
