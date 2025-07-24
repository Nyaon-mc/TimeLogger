package com.github.nyaon08.rtustudio.timelogger.configuration;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import kr.rtuserver.framework.yaml.configuration.ConfigurationSection;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TimeLoggerConfig extends RSConfiguration<TimeLogger> {

    private int saveDBPeriod = 10;
    private Map<String, Object> backupConfig = new HashMap<>();

    public TimeLoggerConfig(TimeLogger plugin) {
        super(plugin, "config.yml", 1);
        setup(this);
    }

    private void init() {
        saveDBPeriod = getInt("save-db-period", saveDBPeriod, """
                데이터 자동 저장 시간 (분)""");

        ConfigurationSection section = getConfigurationSection("backup");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                backupConfig.put(key, section.get(key));
            }
        }
    }

}
