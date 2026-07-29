package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class QuiteListener implements Listener {
    private PlayerTracePlugin plugin;
    public QuiteListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getAfkManager().resetOnlineTime(player);//重置玩家在线时长
    }
}
