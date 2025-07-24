package com.github.nyaon08.rtustudio.timelogger.command;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.configuration.TimeLoggerConfig;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class MainCommand extends RSCommand<TimeLogger> {

    private final TimeLoggerConfig timeLoggerConfig;

    public MainCommand(TimeLogger plugin) {
        super(plugin, "playtime", PermissionDefault.OP);
        this.timeLoggerConfig = plugin.getTimeLoggerConfig();

        registerCommand(new CheckCommand(plugin));
        registerCommand(new ResetCommand(plugin));
        registerCommand(new ResetAllCommand(plugin));
    }

    @Override
    protected boolean execute(RSCommandData data) {
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        return List.of();
    }

    @Override
    protected void reload(RSCommandData data) {
        timeLoggerConfig.reload();
    }

}
