package org.Eggroll.playertrace.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import org.Eggroll.playertrace.PlayerTracePlugin;

public class ptraceCommand implements CommandExecutor {
    private final PlayerTracePlugin plugin;

    public ptraceCommand(PlayerTracePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("playertrace") || label.equalsIgnoreCase("pt") || label.equalsIgnoreCase("ptrace")) {

            // 直接给出提示
            if (args.length == 0) {
                sender.sendMessage("§e=== PlayerTrace 帮助菜单 ===");
                sender.sendMessage("§7/pt reload §f- 重载配置文件");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                // 权限检查
                if (!sender.hasPermission("playertrace.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }

                // 重载配置
                plugin.reloadConfig();
                sender.sendMessage("§a[PlayerTrace] 配置文件已成功重载！");
                return true;
            }

            // 如果输入了子参数 config，输出当前配置信息
            if (args[0].equalsIgnoreCase("status")) {
                // 权限检查
                if (!sender.hasPermission("playertrace.admin")) {
                    sender.sendMessage("§c你没有权限执行此命令！");
                    return true;
                }

                // 配置项及对应说明，新增配置时在此追加即可
                String[][] configEntries = {
                        {"afk-time", "配置挂机判定时长（秒）"},
                        {"welcome-display", "是否开启玩家加入欢迎语"},
                        {"welcome-message", "玩家加入时的欢迎语内容"},
                        {"name-tag", "挂机时是否修改玩家头顶名称"},
                        {"name-tag-prefix", "挂机玩家头顶名称的前缀"},
                        {"back-battle","战斗时禁用/back指令"}
                };

                sender.sendMessage("§6§l╔═════════[ §ePlayerTrace 配置 §6§l]═════════╗");
                sender.sendMessage("§a[PlayerTrace] 插件已启用，版本 " + plugin.getDescription().getVersion());
                for (String[] entry : configEntries) {
                    Object value = plugin.getConfig().get(entry[0]);
                    sender.sendMessage("§6║ §b" + entry[0] + "§7: §a" + value);
                    sender.sendMessage("§6║   §7└ " + entry[1]);
                }
                sender.sendMessage("§6║§4§l注意：本插件暂不支持指令修改配置文件!");
                sender.sendMessage("§6§l╚═══════════════════════╝");
                return true;
            }


            sender.sendMessage("§c未知指令，请输入 /pt 查看帮助。");
            return true;
        }
        return true;
    }

}
