package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class AfkListener implements Listener{
    private PlayerTracePlugin plugin;
    public AfkListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(plugin.getAfkStatus(player)){
            player.sendMessage("§a[提示] 你已解除挂机状态。");
        }
        plugin.resetAfk(player, player.getLocation());
    }
}
