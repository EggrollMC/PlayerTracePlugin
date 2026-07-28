package org.Eggroll.playertrace;

import cn.nukkit.Player;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import org.Eggroll.playertrace.listenter.AfkListener;
import org.Eggroll.playertrace.command.AfkTimeCommand;
import org.Eggroll.playertrace.listenter.DeathListener;
import org.Eggroll.playertrace.command.BackCommand;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerTracePlugin extends PluginBase {

    //最近一次死亡位置
    private final Map<UUID, Location> deathLocations = new ConcurrentHashMap<>();
    //玩家静止时长
    private final Map<UUID,Integer>  afkSeconds = new ConcurrentHashMap<>();
    private final Map<UUID, Vector3> lastPositions= new ConcurrentHashMap<>();
    //玩家挂机状态
    private final Map<UUID, Boolean> afkStatus = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        // 注册死亡事件监听器
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new AfkListener(this), this);
        // 注册 /back 命令执行器
        PluginCommand<?> backCommand = (PluginCommand<?>) this.getCommand("back");
        backCommand.setExecutor(new BackCommand(this));
        // 注册 /afktime 命令执行器
        PluginCommand<?> afkTimeCommand = (PluginCommand<?>) this.getCommand("afktime");
        afkTimeCommand.setExecutor(new AfkTimeCommand(this));
        // 设置命令参数为目标选择器，客户端输入
        afkTimeCommand.getCommandParameters().clear();
        afkTimeCommand.getCommandParameters().put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET)
        });
        // 每20 tick检测一次玩家挂机状态
        this.getServer().getScheduler().scheduleRepeatingTask(this, this::checkAfkPlayers, 20);
        this.getLogger().info("PlayerTrace 已启用！");
    }

    @Override
    public void onDisable() {
        deathLocations.clear();
        afkSeconds.clear();
        lastPositions.clear();
        afkStatus.clear();
        this.getLogger().info("PlayerTrace 已禁用！");
    }

  //记录玩家死亡位置
    public void recordDeathLocation(Player player, Location location) {
        deathLocations.put(player.getUniqueId(), location);
    }

    public Location getDeathLocation(Player player) {
        return deathLocations.get(player.getUniqueId());
    }

    public void removeDeathLocation(Player player) {
        deathLocations.remove(player.getUniqueId());
    }
    //获取挂机时长
    public int getAfkTime(Player player){
        if(afkStatus.getOrDefault(player.getUniqueId(), false)){
            return afkSeconds.getOrDefault(player.getUniqueId(), 0);
        }
        return 0;
    }
    //获取挂机状态
    public boolean getAfkStatus(Player player){
        return afkStatus.getOrDefault(player.getUniqueId(), false);
    }
    private void checkAfkPlayers() {
        for (Player player : getServer().getOnlinePlayers().values()) {
            UUID uuid = player.getUniqueId();
            Vector3 currentPos = player.getLocation();

            Vector3 lastPos = lastPositions.get(uuid);

            if (lastPos != null) {
                if (currentPos.distance(lastPos) < 0.1) {
                    int seconds = afkSeconds.getOrDefault(uuid, 0) + 1;
                    afkSeconds.put(uuid, seconds);
                    if (seconds == 300) {
                        afkStatus.put(uuid, true);
                        player.sendMessage("§e[提示] 你已经长时间未操作，进入挂机状态。");
                        player.setNameTag("[挂机] " + player.getName());
                    }
                } else {
                    //重置
                    resetAfk(player, currentPos);
                }
            } else {
                // 第一次记录
                lastPositions.put(uuid, currentPos);
                afkSeconds.put(uuid, 0);
            }

        }

    }
    //重置挂机状态
    public void resetAfk(Player player, Vector3 pos) {
        lastPositions.put(player.getUniqueId(), pos);
        afkSeconds.put(player.getUniqueId(), 0);
        afkStatus.put(player.getUniqueId(), false);
    }
}
