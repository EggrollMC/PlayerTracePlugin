package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class JoinListener implements Listener {
    private final PlayerTracePlugin plugin;
    public JoinListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(plugin.getConfig().getBoolean("welcome-display")) {
            event.setJoinMessage("");//去除原本的提示
            Player player = event.getPlayer();
            plugin.getServer().broadcastMessage(player.getName() + plugin.getConfig().getString("welcome-message","欢迎来到服务器！"));
        }
        //记录加入的时间
        plugin.getAfkManager().resetOnlineTime(event.getPlayer(), System.currentTimeMillis());
    }
}
