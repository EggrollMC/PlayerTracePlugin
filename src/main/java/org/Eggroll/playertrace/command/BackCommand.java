package org.Eggroll.playertrace.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import org.Eggroll.playertrace.PlayerTracePlugin;

/**
 * /back 命令执行器：将玩家传送回最近一次死亡地点
 */
public class BackCommand implements CommandExecutor {

    private final PlayerTracePlugin plugin;

    public BackCommand(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 只允许玩家执行
        if (!(sender instanceof Player player)) {
            sender.sendMessage(TextFormat.RED + "该命令只能由玩家执行！");
            return true;
        }

        Location deathLocation = plugin.getDeathLocation(player);
        if (deathLocation == null) {
            player.sendMessage(TextFormat.RED + "没有找到你的死亡记录！");
            return true;
        }

        // 传送回死亡地点
        player.teleport(deathLocation);
        player.sendMessage(TextFormat.GREEN + "已传送回死亡地点: "
                + TextFormat.AQUA + "X: " + deathLocation.getFloorX()
                + " Y: " + deathLocation.getFloorY()
                + " Z: " + deathLocation.getFloorZ());

        // 传送后移除记录，防止重复使用
        plugin.removeDeathLocation(player);
        return true;
    }
}
