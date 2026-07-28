package org.Eggroll.playertrace.listenter;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import org.Eggroll.playertrace.PlayerTracePlugin;

/**
 * 玩家死亡事件监听器：记录死亡坐标并在聊天框中提示
 */
public class DeathListener implements Listener {

    private final PlayerTracePlugin plugin;

    public DeathListener(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation().clone();

        // 记录死亡位置
        plugin.recordDeathLocation(player, deathLocation);

        // 在聊天框中提示玩家死亡坐标
        String message = TextFormat.YELLOW + "你死亡了！死亡坐标: "
                + TextFormat.AQUA + "X: " + deathLocation.getFloorX()
                + " Y: " + deathLocation.getFloorY()
                + " Z: " + deathLocation.getFloorZ()
                + TextFormat.YELLOW + " (世界: " + deathLocation.getLevel().getName() + ")"
                + "\n" + TextFormat.GREEN + "输入 /back 可传送回死亡地点。";
        player.sendMessage(message);
    }
}
