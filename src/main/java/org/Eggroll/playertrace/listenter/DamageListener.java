package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import org.Eggroll.playertrace.PlayerTracePlugin;

import java.util.UUID;

public class DamageListener implements Listener {
    private final PlayerTracePlugin plugin;
    public DamageListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        //伤害监听
        Player player = (Player) event.getEntity();
        plugin.getDeathManager().setLastUseTime(player);
    }
}
