package org.Eggroll.playertrace.manager;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//死亡位置管理
public class BackManager {

    //最近一次死亡位置
    private final Map<UUID, Location> deathLocations = new HashMap<>();
    private final Map<UUID, Long> lastUseTime = new HashMap<>();
    private final Map<UUID, Long> damageTimes = new HashMap<>();

    //记录玩家死亡位置
    public void recordDeathLocation(Player player, Location location) {
        deathLocations.put(player.getUniqueId(), location);
    }

    //获取玩家最近一次死亡位置，没有记录时返回 null
    public Location getDeathLocation(Player player) {
        return deathLocations.get(player.getUniqueId());
    }

    // 移除玩家的死亡位置记录
    public void removeDeathLocation(Player player) {
        deathLocations.remove(player.getUniqueId());
    }

    //清空所有记录
    public void clear() {

        deathLocations.clear();
        lastUseTime.clear();
        damageTimes.clear();
    }
    //记录玩家上一次受伤的时间
    public void setLastUseTime(Player player) {
        lastUseTime.put(player.getUniqueId(), System.currentTimeMillis());
    }
    //获取玩家上一次受伤时间
    public long getLastUseTime(Player player) {
        return lastUseTime.getOrDefault(player.getUniqueId(), 0L);
    }
}
