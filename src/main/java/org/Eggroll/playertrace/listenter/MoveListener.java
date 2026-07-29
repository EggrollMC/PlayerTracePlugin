package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class MoveListener implements Listener{
    private PlayerTracePlugin plugin;
    public MoveListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){

    }
}
