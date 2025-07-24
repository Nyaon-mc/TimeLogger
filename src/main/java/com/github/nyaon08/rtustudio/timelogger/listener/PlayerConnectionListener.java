package com.github.nyaon08.rtustudio.timelogger.listener;

import com.github.nyaon08.rtustudio.timelogger.TimeLogger;
import com.github.nyaon08.rtustudio.timelogger.manager.PlayTimeManager;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnectionListener extends RSListener<TimeLogger> {

    private final PlayTimeManager playTimeManager;

    public PlayerConnectionListener(TimeLogger plugin) {
        super(plugin);
        this.playTimeManager = plugin.getPlayTimeManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playTimeManager.initPlayer(event.getPlayer().getUniqueId());
        playTimeManager.getLoginTimes().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Long loginTime = playTimeManager.getLoginTimes().remove(uuid);
        if (loginTime != null) {
            long sessionMillis = System.currentTimeMillis() - loginTime;
            long sessionSeconds = sessionMillis / 1000;
            if (sessionSeconds > 0) {
                getPlugin().getPlayTimeManager().addPlayTime(uuid, sessionSeconds);
            }
        }
    }

}
