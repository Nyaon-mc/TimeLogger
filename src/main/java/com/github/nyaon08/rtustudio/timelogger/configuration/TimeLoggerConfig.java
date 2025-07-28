package com.github.nyaon08.rtustudio.timelogger.configuration;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import kr.rtuserver.framework.yaml.configuration.ConfigurationSection;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TimeLoggerConfig extends RSConfiguration<TimeLogger> {

    private int saveDBPeriod = 10;
    private List<String> playtimeCheckList = List.of("player1", "player2", "player3");
    private final Map<String, Object> backupConfig = new HashMap<>();

    public TimeLoggerConfig(TimeLogger plugin) {
        super(plugin, "config.yml", 1);
        setup(this);
    }

    private void init() {
        saveDBPeriod = getInt("save-db-period", saveDBPeriod, """
                데이터 자동 저장 시간 (분)""");

        playtimeCheckList = getList("playtime-check-list", playtimeCheckList, """
                플레이타임 목록을 출력할 플레이어 리스트""");

        ConfigurationSection section = getConfigurationSection("backup");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                backupConfig.put(key, section.get(key));
            }
        }
    }

}
