package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.configuration.translation.message.MessageTranslation;
import kr.rtuserver.framework.bukkit.api.format.ComponentFormatter;
import org.bukkit.entity.Player;

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
        Player target = provider().getPlayer(data.args(1));
        if (target == null) {
            chat().announce(message().getCommon(player(), String.valueOf(MessageTranslation.NOT_FOUND_ONLINE_PLAYER)));
            return true;
        }
        UUID uuid = target.getUniqueId();

        playTimeManager.resetPlayTime(uuid);

        chat().announce(ComponentFormatter.mini(message().get(player(), "reset").replace("[player]", provider().getName(target))));
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        if (data.length(2)) return provider().getNames();
        return List.of();
    }

}
