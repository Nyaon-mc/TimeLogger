package com.github.nyaon08.rtustudio.timelogger.configuration;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import lombok.Getter;

@Getter
public class TimeLoggerConfig extends RSConfiguration<TimeLogger> {

    private int saveDBPeriod = 10;

    public TimeLoggerConfig(TimeLogger plugin) {
        super(plugin, "config.yml", 1);
        setup(this);
    }

    private void init() {
        saveDBPeriod = getInt("save-db-period", saveDBPeriod, """
                데이터 자동 저장 시간 (분)""");
    }

}
