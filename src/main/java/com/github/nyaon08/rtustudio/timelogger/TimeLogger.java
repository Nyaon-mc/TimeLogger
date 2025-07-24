package com.github.nyaon08.rtustudio.timelogger;

import com.github.nyaon08.rtustudio.timelogger.command.MainCommand;
import com.github.nyaon08.rtustudio.timelogger.configuration.TimeLoggerConfig;
import com.github.nyaon08.rtustudio.timelogger.dependency.PlaceholderAPI;
import com.github.nyaon08.rtustudio.timelogger.listener.PlayerConnectionListener;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import lombok.Getter;

public class TimeLogger extends RSPlugin {

    @Getter
    private static TimeLogger instance;

    @Getter
    private TimeLoggerConfig timeLoggerConfig;

    @Getter
    private PlayTimeManager playTimeManager;

    private PlaceholderAPI placeholder;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        getConfigurations().getStorage().init("playtime");

        timeLoggerConfig = new TimeLoggerConfig(this);
        playTimeManager = new PlayTimeManager(this);

        playTimeManager.startAutoSaveTask();

        registerEvent(new PlayerConnectionListener(this));
        registerCommand(new MainCommand(this), true);

        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder = new PlaceholderAPI(this);
            placeholder.register();
        }
    }

    @Override
    public void disable() {
        playTimeManager.save();
        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder.unregister();
        }
    }

}
