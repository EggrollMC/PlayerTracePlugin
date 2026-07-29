package org.Eggroll.playertrace;

import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.PluginBase;
import org.Eggroll.playertrace.listenter.JoinListener;
import org.Eggroll.playertrace.listenter.MoveListener;
import org.Eggroll.playertrace.command.AfkTimeCommand;
import org.Eggroll.playertrace.listenter.DeathListener;
import org.Eggroll.playertrace.command.BackCommand;
import org.Eggroll.playertrace.command.ptraceCommand;
import org.Eggroll.playertrace.manager.AfkManager;
import org.Eggroll.playertrace.manager.BackManager;

public class PlayerTracePlugin extends PluginBase {

    //死亡位置管理
    private BackManager backManager;
    //挂机检测管理
    private AfkManager afkManager;

    @Override
    public void onEnable() {
        // 初始化管理类
        this.backManager = new BackManager();
        this.afkManager = new AfkManager(this);
        // 注册事件监听器
        this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        // 注册 /back 命令执行器
        PluginCommand<?> backCommand = (PluginCommand<?>) this.getCommand("back");
        backCommand.setExecutor(new BackCommand(this));
        // 注册 /afktime 命令执行器
        PluginCommand<?> afkTimeCommand = (PluginCommand<?>) this.getCommand("afktime");
        afkTimeCommand.setExecutor(new AfkTimeCommand(this));
        // 设置命令参数为目标选择器，客户端输入 /afktime 后会自动补全在线玩家名
        afkTimeCommand.getCommandParameters().clear();
        afkTimeCommand.getCommandParameters().put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET)
        });
        // 注册 /ptrace 命令执行器
        PluginCommand<?> ptraceCommand = (PluginCommand<?>) this.getCommand("ptrace");
        ptraceCommand.setExecutor(new ptraceCommand(this));
        ptraceCommand.getCommandParameters().clear();
        ptraceCommand.getCommandParameters().put("default", new CommandParameter[]{
                CommandParameter.newEnum("子指令", true, new String[]{"reload","status"})
        });
                
        //20 tick检测一次玩家挂机状态
        this.getServer().getScheduler().scheduleRepeatingTask(this, afkManager::checkAfkPlayers, 20);
        this.saveDefaultConfig();//保存配置文件
        this.getLogger().info("PlayerTrace 已启用！");
    }

    @Override
    public void onDisable() {
        backManager.clear();
        afkManager.clear();
        this.getLogger().info("PlayerTrace 已禁用！");
    }

    //获取死亡位置管理类
    public BackManager getDeathManager() {
        return backManager;
    }

    //获取挂机检测管理类
    public AfkManager getAfkManager() {
        return afkManager;
    }
}
