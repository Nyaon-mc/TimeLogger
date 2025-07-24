package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.format.ComponentFormatter;

import java.util.List;

public class ResetAllCommand extends RSCommand<TimeLogger> {

    private final PlayTimeManager playTimeManager;

    public ResetAllCommand(TimeLogger plugin) {
        super(plugin, "resetall");
        this.playTimeManager = plugin.getPlayTimeManager();
    }

    @Override
    protected boolean execute(RSCommandData data) {
        playTimeManager.resetAllPlayTime();
        chat().announce(ComponentFormatter.mini(message().get(player(), "resetall")));
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        return List.of();
    }

}
