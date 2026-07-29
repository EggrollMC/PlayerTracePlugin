# PlayerTrace

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)

**PlayerTrace** 是一个轻量级的 **Nukkit-MOT** 服务器插件，专注于玩家行为足迹追踪与服务器轻量管理。

> 记录玩家的每一次"足迹"——从死亡坐标、挂机状态，到加入欢迎，让服务器更贴心、更好管。

## ✨ 功能特性

### 🎯 死亡坐标回溯
- 玩家死亡时**自动记录**精确坐标（含世界名）并私聊提示
- 使用 `/back` 一键传送回死亡点，传送后记录自动清除，防止重复使用
- 支持 `back-battle` 配置：**战斗中禁用** `/back` 指令

### 💤 视角驱动挂机检测
- 采用 **yaw/pitch 视角变化** 检测挂机，**不受位置移动影响**（挂机中仍跑动不算解除）
- 达到 `afk-time`（默认 300 秒）自动标记挂机，给玩家发送提示并修改头顶名称前缀
- 视角再次变化时**自动解除**挂机状态（状态翻转瞬间仅提示一次，不会刷屏）
- 管理员可用 `/afktime <玩家>` 查看该玩家的**在线时长 + 挂机时长 + 当前状态**

### 👋 加入欢迎语
- 自定义加入欢迎消息，支持 `welcome-display` 开关
- **自动屏蔽** Nukkit 默认的 `xxx joined the game` 提示，保持聊天区整洁
- 消息内容可在 `config.yml` 中自由编辑

### 🛠 插件总管理指令 `/ptrace`（别名 `/pt`）
- `/pt reload` 重载配置文件，无需重启服务器
- `/pt status` 查看插件版本 + 所有配置项当前值与中文说明
- 支持客户端**子指令自动补全**

## 📋 指令与权限

| 指令 | 说明 | 权限节点 | 默认 |
|---|---|---|---|
| `/back` | 传送回最近一次死亡地点 | `playertrace.back` | ✔️ 普通玩家可用 |
| `/afktime <玩家>` | 查看指定玩家的状态面板（在线/挂机/时长） | `afk.check` / op | ⛔ 仅管理员 |
| `/pt reload` | 重载配置文件 | `playertrace.admin` | ⛔ 仅管理员 |
| `/pt status` | 查看插件状态与配置一览 | `playertrace.admin` | ⛔ 仅管理员 |

别名：`/afk` = `/afktime`，`/pt` = `/ptrace`。

## ⚙️ 配置文件（`config.yml`）

| 键 | 默认值 | 说明 |
|---|---|---|
| `afk-time` | `300` | 判定挂机的时长，单位：秒 |
| `welcome-display` | `true` | 是否启用自定义欢迎语（同时屏蔽默认加入提示） |
| `welcome-message` | `欢迎来到服务器！` | 欢迎消息内容，会拼接在玩家名后 |
| `name-tag` | `true` | 挂机时是否修改玩家头顶名称 |
| `name-tag-prefix` | `[§i挂机]` | 挂机玩家头顶前缀，支持 Minecraft 颜色符 § |
| `back-battle` | `true` | 战斗状态下是否禁用 `/back` 指令 |

> ⚠️ 本插件**暂不支持通过指令修改配置文件**，请手动编辑 `plugins/PlayerTrace/config.yml` 后用 `/pt reload` 重载。

## 📦 安装

1. 下载编译好的 `PlayerTrace-<version>.jar`
2. 放入服务器 `plugins` 目录
3. 启动 / 重启服务器
4. 编辑生成的 `plugins/PlayerTrace/config.yml` 并按需调整

## 🔧 从源码构建

```bash
mvn clean package
```

构建产物输出至 `target/`（也可在 `pom.xml` 中配置直接输出到服务器插件目录）。

## 🖥 开发环境

- **服务端核心**：Nukkit-MOT（API `1.0.13`）
- **语言**：Java 17
- **构建工具**：Maven
- **项目结构**：
  ```
  org.Eggroll.playertrace
  ├── command/    # 命令执行器：BackCommand、AfkTimeCommand、ptraceCommand
  ├── listenter/  # 事件监听器：DeathListener、JoinListener、MoveListener
  └── manager/    # 管理类：BackManager、AfkManager
  ```

## 📜 开源协议

本项目采用 **MIT License** 开源。
