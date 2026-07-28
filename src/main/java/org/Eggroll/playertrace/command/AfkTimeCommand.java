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
        int afkTime = plugin.getAfkTime(target);
        if(afkTime == 0){
            commandSender.sendMessage(TextFormat.RED + "该玩家没有挂机记录！");
            return true;
        }
        commandSender.sendMessage(TextFormat.GREEN + "该玩家的挂机时长为：" + (afkTime / 60) + "分钟");
        return true;
    }
}
