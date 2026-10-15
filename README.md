# Beyond the Walls · 墙之外

一款以「双角色协作 + 拾忆」为主题的单机解谜游戏。

一个女孩逃离了打卡机、报表和永不停息的消息提示音，回到那座门锁生锈、无人守候的老屋。
她要带着捡回来的小猫，扫掉灰尘，拾起碎掉的旧时光，一路走到天台。

**5 个关卡 · 4 种结局 · 无战斗、无计时**

---

## 目录

- [核心玩法](#核心玩法)
- [操作](#操作)
- [机制](#机制)
- [关卡](#关卡)
- [结局](#结局)
- [运行环境与构建](#运行环境与构建)
- [项目结构](#项目结构)
- [关卡编辑](#关卡编辑)
- [存档](#存档)
- [团队](#团队)

---

## 核心玩法

前四关是**同屏双人协作**。你同时操控女孩和小猫，两套按键互不干扰：

- 女孩跳得更高但走得更慢（水平 96 / 垂直 -300）；
- 小猫跑得更快但跳得更低（水平 144 / 垂直 -250）。

一个擅长攀越，一个擅长赶路。多数关卡里，「女孩过不去的地方猫能过、猫赶不及的地方女孩已经就位」正是解法的由来。

**只有当两个角色同时抵达终点，关卡才算通过。** 一个人的路线规划经常会让另一个人的路线变得截然不同——这通常是关卡设计的核心张力。

第五关换了一套操作范式：小猫变成一枚需要**按住蓄力、松开起跳**的跳跳猫，关卡由升降板和密集地刺构成。

沿途散落着几处**旧物**（Secret），分别勾起她关于妈妈、童年、和父亲的记忆。是否找到它们，会改变你抵达终点时看到的结局。

---

## 操作

| 按键 | 作用 |
| :--- | :--- |
| `A` / `D` | 女孩 左移 / 右移 |
| `W` | 女孩 跳跃 |
| `←` / `→` | 小猫 左移 / 右移 |
| `↑` | 小猫 跳跃 |
| `鼠标左键` 按住 | 第五关：蓄力（松开起跳，蓄力越久跳得越远） |
| `空格` 按住 | 同上，键盘替代方案 |
| `C` | ⚠️ 调试用作弊键（解锁当前关卡并传送到终点，遗留功能，计划移除） |

---

## 机制

| 元素 | 行为 |
| :--- | :--- |
| **床** | 高弹性表面，落地回弹并短暂获得空中转向权 |
| **墙 / 屏障** | 常规碰撞体；屏障摩擦力为 0，贴墙手感不同 |
| **箱子** | 可推动，配合压力板解谜 |
| **压力板 → 平台** | 踩上压力板触发对应平台往返移动，两者通过关卡里配置的 `id` 配对 |
| **钥匙 / 锁** | 拾取钥匙后，对应的门锁才可穿过 |
| **气流区** | 有方向性的持续推力，可吹飞角色 |
| **水流** | 触碰后传送角色到指定坐标 |
| **升降板** | 匀速上下移动的平台 |
| **移动平台** | 撞到墙壁自动掉头 |
| **地刺** | 静态障碍，碰到即被弹开 |
| **蓄力条** | 长按过程中在猫上方实时生成的充能指示器（运行时用 Canvas 绘制渐变纹理） |
| **旧物** | 隐藏收集品，共 3 处，决定结局走向 |

---

## 关卡

| 关卡 | 新增机制 |
| :--- | :--- |
| 1 · 老屋 | 双人协作、推箱子、床垫弹跳、发卡 |
| 2 · 抽屉 | 钥匙与门锁、宝藏、旧物「八音盒」 |
| 3 · 风 | 气流区，暗藏结局「旧物」 |
| 4 · 潮 | 水流传送，场景重置成为解谜手段 |
| 5 · 天台 | 操作范式切换：蓄力跳跃 + 升降板 + 地刺阵 |

关卡地图为 Tiled 的 `.tmx` 格式，见 [关卡编辑](#关卡编辑)。

---

## 结局

结局由收集到的旧物决定。**注意 `hideEnd` 与其余三者相互独立**，它只看 `secret1`。

| 结局 | 触发条件 | 所在关卡 |
| :--- | :--- | :--- |
| **end1** | `secret2`、`secret3` 均未收集 | 全部通关 |
| **end2** | 仅收集到 `secret2`（八音盒） | level 2 |
| **end3** | 同时收集到 `secret2` 与 `secret3`（八音盒 + 小熊） | ⚠️ 见下方说明 |
| **hideEnd** | 收集到 `secret1`（暗藏的结局） | level 3 |

> ⚠️ **当前版本 `end3` 不可达**：全部关卡中只摆放了 2 处旧物（`secret2` 在 level 2、`secret1` 在 level 3），`secret3` 从未被放置，因此玩家最多只能收集到其中 1 处。`bear.txt`（小熊）这段剧情目前是打不出来的。补一个 `secret3` 实体即可打通。

---

## 运行环境与构建

### 环境要求

| | 版本 |
| :--- | :--- |
| JDK | 21+ |
| Maven | 3.8+ |
| FXGL | 21.1（由 Maven 自动拉取） |
| 编辑关卡（可选） | [Tiled](https://www.mapeditor.org/) |

### 开发运行

```bash
mvn javafx:run
```

### 打包自包含运行时

生成一个自带 JRE 的启动器，目标机器无需安装 Java：

```bash
mvn clean package
mvn javafx:jlink
```

产物：`target/BeyondtheWalls/bin/BeyondtheWalls.bat`

> 注：`mvn package` 产出的 `BeyondtheWalls-1.0-SNAPSHOT.jar` 不含依赖，也未设置主清单，**不能**用 `java -jar` 直接运行，请使用上面的 `jlink` 产物。

### 制作原生安装包

```bash
mvn javafx:jpackage
```

> Windows 上需要预先安装 [WiX Toolset](https://wixtoolset.org/)。

---

## 项目结构

```
src/main/java/com/beyondthewalls/
├── Main.java              游戏主类：输入注册、碰撞规则、存档读写、关卡变量
├── components/            实体组件（每个组件承载一类实体的行为）
│   ├── Player.java        女孩 / 小猫的通用移动组件
│   ├── CatJump.java       第五关的蓄力跳跃组件
│   ├── Plant.java         压力板联动的往返平台
│   ├── Press.java         压力板
│   ├── Air.java           气流区
│   ├── Water.java         水流传送
│   ├── Bat.java           升降板
│   ├── End.java           终点判定与结局分支
│   └── ...
├── factories/             实体工厂：把 Tiled 的 object class 映射为实体
│   ├── ItemFactory.java
│   ├── PlayerFactory.java
│   ├── JumpFactory.java
│   └── UIFactory.java     标题界面 / 加载 / 设置等 UI
├── data/Save.java         存档数据结构
└── utils/                 枚举：Type / Status / Direction / JumpStatus

src/main/resources/assets/
├── levels/*.tmx           Tiled 关卡地图
├── textures/              精灵图
├── sounds/  music/        音效与 BGM
└── text/*.txt             对话脚本（FXGL Cutscene 格式）
```

架构基于 FXGL 的 ECS：`Component` 负责行为，`EntityFactory` 负责装配，碰撞规则集中注册在 `Main` 中。

---

## 关卡编辑

用 Tiled 打开 `src/main/resources/assets/levels/` 下的 `.tmx` 文件。

Tiled 中每个 object 的 **`class` 属性**对应代码里 `@Spawns` 的名字，即：

| class | 用途 | 必需属性 |
| :--- | :--- | :--- |
| `wall` / `barrier` / `bed` / `box` | 基础地形与推箱 | `width` `height` |
| `hairpin` / `treasure` / `key` / `lock` | 收集品与机关 | `width` `height` |
| `pressurePlate` | 压力板 | `width` `height` **`id`** |
| `pressPlatform` | 受压力板驱动的往返平台 | **`id`** `len` `tx` `ty` |
| `air` | 气流区 | `width` `height` **`direction`**（1 上 / 2 下 / 3 左 / 4 右） |
| `water` | 水流传送 | `width` `height` `tx` `ty` |
| `secret` | 旧物 | `width` `height` **`id`**（1/2/3） |
| `lift` / `patrolPlatform` / `patrolPlatformVisible` / `spike` | 第五关要素 | `width` `height` |
| `cat` / `catJumper` | 小猫（普通 / 蓄力跳） | — |
| `girlNoPin` / `girlWithPin` | 女孩（**拾取发卡前 / 后**） | — |
| `end` | 终点判定区 | `width` `height` |
| `restart` | 重开按钮 | `level` `nextLevel` |

> **配对提示**：`pressurePlate` 和 `pressPlatform` 通过 `id` 一一对应，同一关卡内 `id` 唯一即可，压力板会在碰撞时驱动对应平台。系统按当前场景实时查找，因此 id 在各关卡间可以重复使用。

> **命名说明**：`girlNoPin` 与 `girlWithPin` 是同一个角色的两个外观变体——第 1 关尚未拾取蝴蝶发卡，第 2 关起女孩会把发卡别回发间。两者玩法完全一致，共用同一个实体类型。

---

## 存档

游戏会在通关时自动写入存档：

```
~/.beyondthewalls/save.json
```

内容为当前关卡进度与旧物收集状态：

```json
{"level":3,"secret1":false,"secret2":true,"secret3":false}
```

标题界面的「继续」按钮会读取该文件。若文件缺失或损坏，游戏会自动回退到新档并重新生成，不会崩溃。

---

## 团队

陈池 · 何宇航 · 林良耀 · 姜秋蕾 · 朱雅婷

---

## 许可

**待定。** 推送至 GitHub 前需要在 5 位成员之间确认授权协议，并添加对应的 `LICENSE` 文件。可选：MIT（宽松，允许商用与二次分发）／ Apache-2.0（额外授予专利权）／ GPL-3.0（衍生作品必须开源）。

素材方面需额外确认音频与精灵图的来源是否允许再分发。

---

## 已知问题

- `end3` 结局不可达：`secret3` 未在任何关卡中放置（详见 [结局](#结局)）
- 正式版仍保留作弊键 `C` 与常驻的性能监视窗口（`setProfilingEnabled(true)`）
- 无暂停菜单与退出入口，只能关闭游戏窗口
- 关卡的死亡判定线、重生点等坐标硬编码在代码中，调整地图尺寸需同步修改
- 暂无自动化测试与 CI
