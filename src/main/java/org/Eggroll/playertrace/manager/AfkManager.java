package org.Eggroll.playertrace.manager;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import org.Eggroll.playertrace.PlayerTracePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//挂机检测管理
public class AfkManager {

    private final PlayerTracePlugin plugin;
    //玩家静止时长
    private final Map<UUID, Integer> afkSeconds = new HashMap<>();
    //玩家上一次视角（yaw/pitch），用于判定是否挂机
    private final Map<UUID, Location> lastLook = new HashMap<>();
    //玩家加入时间
    private final Map<UUID, Long> onlineSeconds = new HashMap<>();
    //玩家挂机状态
    private final Map<UUID, Boolean> afkStatus = new HashMap<>();


    public AfkManager(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    //获取挂机时长
    public int getAfkTime(Player player) {
        if (afkStatus.getOrDefault(player.getUniqueId(), false)) {
            return afkSeconds.getOrDefault(player.getUniqueId(), 0);
        }
        return 0;
    }

    //获取挂机状态
    public boolean getAfkStatus(Player player) {
        return afkStatus.getOrDefault(player.getUniqueId(), false);
    }

    public void checkAfkPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers().values()) {
            UUID uuid = player.getUniqueId();
            Location current = player.getLocation();

            Location last = lastLook.get(uuid);

            if (last != null) {
                // 只比较视角（yaw / pitch）是否变化，不受位置移动影响
                if (Math.abs(current.getYaw() - last.getYaw()) < 0.01
                        && Math.abs(current.getPitch() - last.getPitch()) < 0.01) {
                    // 视角没动，秒数 +1
                    int seconds = afkSeconds.getOrDefault(uuid, 0) + 1;
                    afkSeconds.put(uuid, seconds);
                    //超过阈值判定为挂机，只在状态翻转的瞬间发送提示并修改nameTag
                    if (seconds >= plugin.getConfig().getInt("afk-time", 300)
                            && !afkStatus.getOrDefault(uuid, false)) {
                        afkStatus.put(uuid, true);
                        player.sendMessage("§e[提示] 你已经长时间未操作，进入挂机状态。");
                        if (plugin.getConfig().getBoolean("name-tag", true)) {
                            player.setNameTag(plugin.getConfig().getString("name-tag-prefix", "[挂机]") + player.getName());
                        }
                    }
                } else {
                    //重置，并发送解除提示
                    if (afkStatus.getOrDefault(uuid, false)) {
                        player.sendMessage("§a[提示] 你已解除挂机状态。");
                    }
                    resetAfk(player);
                }
            } else {
                // 第一次记录
                lastLook.put(uuid, current.clone());
                afkSeconds.put(uuid, 0);
            }

        }

    }

    //重置挂机状态（记录当前视角，并清零计时）
    public void resetAfk(Player player) {
        lastLook.put(player.getUniqueId(), player.getLocation().clone());
        afkSeconds.put(player.getUniqueId(), 0);
        afkStatus.put(player.getUniqueId(), false);
    }

    public void resetOnlineTime(Player player, long time) {
        onlineSeconds.put(player.getUniqueId(), time);
    }

    public int getOnlineTime(Player player) {
        return (int) ((System.currentTimeMillis() - onlineSeconds.getOrDefault(player.getUniqueId(), 0L)) / 1000);
    }

    //重置在线时长
    public void resetOnlineTime(Player player) {
        onlineSeconds.put(player.getUniqueId(), 0L);
    }

    //清空所有记录
    public void clear() {
        afkSeconds.clear();
        lastLook.clear();
        afkStatus.clear();
        onlineSeconds.clear();
    }
}
