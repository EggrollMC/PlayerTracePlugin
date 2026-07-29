package org.Eggroll.playertrace.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class AfkTimeCommand implements CommandExecutor {
    private final PlayerTracePlugin plugin;

    public AfkTimeCommand(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp()) {
            commandSender.sendMessage(TextFormat.RED + "你没有权限执行该命令，需要管理员权限！");
            return true;
        }
        if(strings.length == 0 || strings[0].isEmpty()){
            commandSender.sendMessage(TextFormat.RED + "请输入玩家名！");
            return true;
        }
        Player target = plugin.getServer().getPlayer(strings[0]);
        if(target == null){
            commandSender.sendMessage(TextFormat.RED + "该玩家不在线或不存在！");
            return true;
        }
        int afkTime = plugin.getAfkManager().getAfkTime(target);
        int onlineTime = plugin.getAfkManager().getOnlineTime(target);
        //§e=== 玩家 [Steve] 的状态面板 ===
        //§7  当前状态: §a 正常活动中
        //§7  在线时长: §f1小时 25分钟
        //§7  挂机时长: §f5分钟 12秒
        commandSender.sendMessage("§e=== 玩家 [" + target.getName() + "] 的状态面板 ===");
        if (plugin.getAfkManager().getAfkStatus(target)) {
            commandSender.sendMessage("§7  当前状态: §e挂机中");
        } else {
            commandSender.sendMessage("§7  当前状态: §a正常活动中");
        }
        commandSender.sendMessage("§7  在线时长: §f" + onlineTime / 3600 + "小时 " + (onlineTime % 3600) / 60 + "分钟");
        commandSender.sendMessage("§7  挂机时长: §f" + afkTime / 60 + "分钟 " + afkTime % 60 + "秒");
        return true;
    }
}
