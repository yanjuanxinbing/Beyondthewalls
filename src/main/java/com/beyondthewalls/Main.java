package com.beyondthewalls;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.cutscene.Cutscene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.beyondthewalls.components.*;
import com.beyondthewalls.data.Save;
import com.beyondthewalls.factories.ItemFactory;
import com.beyondthewalls.factories.JumpFactory;
import com.beyondthewalls.factories.PlayerFactory;
import com.beyondthewalls.utils.JumpStatus;
import com.beyondthewalls.utils.Type;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import com.beyondthewalls.factories.UIFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main extends GameApplication {
    private static final Gson gson = new Gson();

    public static void newText(String text) {
        List<String> lines = FXGL.getAssetLoader().loadText(text);
        Cutscene cutscene = new Cutscene(lines);
        FXGL.getCutsceneService().startCutscene(cutscene);
    }

    /**
     * 存档路径。放在用户主目录而非工作目录，避免从快捷方式/安装目录启动时
     * 因权限不足而写入失败或污染系统目录。
     */
    private static Path getSaveFile() {
        return Paths.get(System.getProperty("user.home"), ".beyondthewalls", "save.json");
    }

    public static void save() {
        writeSave(new Save(
                FXGL.geti("level"),
                FXGL.getb("secret1"),
                FXGL.getb("secret2"),
                FXGL.getb("secret3")
        ));
    }

    /**
     * 写盘失败不应中断游戏流程，仅记录日志。
     */
    private static void writeSave(Save save) {
        Path file = getSaveFile();

        try {
            Files.createDirectories(file.getParent());
            Files.write(file, gson.toJson(save).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("[BeyondtheWalls] 存档写入失败: " + e.getMessage());
        }
    }

    /**
     * 读取存档。存档缺失、损坏或无法解析时一律回退到新档，保证游戏始终能启动。
     */
    private static Save loadSave() {
        Path file = getSaveFile();
        Save fresh = new Save(1, false, false, false);

        if (!Files.exists(file)) {
            writeSave(fresh);
            return fresh;
        }

        try {
            Save loaded = gson.fromJson(Files.readString(file, StandardCharsets.UTF_8), Save.class);
            return loaded != null ? loaded : fresh;
        } catch (IOException | JsonParseException e) {
            System.err.println("[BeyondtheWalls] 存档读取失败，使用新档: " + e.getMessage());
            return fresh;
        }
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setProfilingEnabled(true);

        gameSettings.setGameMenuEnabled(false);
        gameSettings.setTitle("Beyond the Walls");
        gameSettings.setWidth(960);
        gameSettings.setHeight(640);
        gameSettings.setVersion("");
        gameSettings.setAppIcon("icon.png");
    }

    private void setCanEnd() {
        getEntity(Type.end)
                .flatMap(e -> e.getComponentOptional(End.class))
                .ifPresent(End::setCanEnd);
    }

    private void setUnLock() {
        getEntity(Type.lock)
                .flatMap(e -> e.getComponentOptional(Lock.class))
                .ifPresent(Lock::unlock);
    }

    private static Optional<Entity> getEntity(Type type) {
        return FXGL.getGameWorld()
                .getEntitiesByType(type)
                .stream()
                .findFirst();
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("GL") {
            @Override
            protected void onAction() {
                getEntity(Type.girl).ifPresent(entity ->
                        entity.getComponent(Player.class).tryLeft()
                );
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.girl).ifPresent(entity ->
                        entity.getComponent(Player.class).stop()
                );
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("GR") {
            @Override
            protected void onAction() {
                getEntity(Type.girl).ifPresent(entity ->
                        entity.getComponent(Player.class).tryRight()
                );
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.girl).ifPresent(entity ->
                        entity.getComponent(Player.class).stop()
                );
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("GJ") {
            @Override
            protected void onActionBegin() {
                getEntity(Type.girl).ifPresent(entity ->
                        entity.getComponent(Player.class).tryJump()
                );
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("CL") {
            @Override
            protected void onAction() {
                getEntity(Type.cat).ifPresent(entity ->
                        entity.getComponent(Player.class).tryLeft()
                );
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.cat).ifPresent(entity ->
                        entity.getComponent(Player.class).stop()
                );
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("CR") {
            @Override
            protected void onAction() {
                getEntity(Type.cat).ifPresent(entity ->
                        entity.getComponent(Player.class).tryRight()
                );
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.cat).ifPresent(entity ->
                        entity.getComponent(Player.class).stop()
                );
            }
        }, KeyCode.RIGHT);

        FXGL.getInput().addAction(new UserAction("CJ") {
            @Override
            protected void onActionBegin() {
                getEntity(Type.cat).ifPresent(entity ->
                        entity.getComponent(Player.class).tryJump()
                );
            }
        }, KeyCode.UP);

        FXGL.getInput().addAction(new UserAction("JumpGame") {
            @Override
            protected void onActionBegin() {
                getEntity(Type.catJumper).ifPresent(entity -> {
                    CatJump cj = entity.getComponent(CatJump.class);
                    if (cj.getJumpStatus() == JumpStatus.waiting) {
                        cj.begin();
                    }
                });
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.catJumper).ifPresent(entity -> {
                    CatJump cj = entity.getComponent(CatJump.class);
                    if (cj.getJumpStatus() == JumpStatus.jumpBegin) {
                        cj.release();
                    }
                });
            }
        }, MouseButton.PRIMARY);

        FXGL.getInput().addAction(new UserAction("JumpGame2") {
            @Override
            protected void onActionBegin() {
                getEntity(Type.catJumper).ifPresent(entity -> {
                    CatJump cj = entity.getComponent(CatJump.class);
                    if (cj.getJumpStatus() == JumpStatus.waiting) {
                        cj.begin();
                    }
                });
            }

            @Override
            protected void onActionEnd() {
                getEntity(Type.catJumper).ifPresent(entity -> {
                    CatJump cj = entity.getComponent(CatJump.class);
                    if (cj.getJumpStatus() == JumpStatus.jumpBegin) {
                        cj.release();
                    }
                });
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("CHEAT") {
            @Override
            protected void onActionBegin() {
                getEntity(Type.catJumper).ifPresent(entity -> {
                    CatJump cj = entity.getComponent(CatJump.class);
                    setCanEnd();
                    cj.translate(new Point2D(9350, 592));
                });
            }
        }, KeyCode.C);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.girl, Type.bed) {
                    @Override
                    protected void onCollisionBegin(Entity girl, Entity bed) {
                        girl.getComponent(Player.class).setOnBed();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.bed) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity wall) {
                        cat.getComponent(Player.class).setOnBed();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.hairpin) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity hairpin) {
                        newText("hairpin.txt");
                        hairpin.removeFromWorld();
                        setCanEnd();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.girl, Type.treasure) {
                    @Override
                    protected void onCollisionBegin(Entity girl, Entity treasure) {
                        treasure.removeFromWorld();
                        setCanEnd();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.treasure) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity treasure) {
                        treasure.removeFromWorld();
                        setCanEnd();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.catJumper, Type.hairpin) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity hairpin) {
                        hairpin.removeFromWorld();
                        setCanEnd();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.key) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity key) {
                        key.removeFromWorld();
                        setUnLock();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.girl, Type.key) {
                    @Override
                    protected void onCollisionBegin(Entity girl, Entity key) {
                        key.removeFromWorld();
                        setUnLock();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.lock) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity lock) {
                        if (!lock.getComponent(Lock.class).isLock()) {
                            lock.removeFromWorld();
                        }
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.girl, Type.secret) {
                    @Override
                    protected void onCollisionBegin(Entity girl, Entity secret) {
                        int id = secret.getComponent(Secret.class).getId();

                        switch (id) {
                            case 1:
                                FXGL.set("secret1", true);
                                break;
                            case 2:
                                FXGL.set("secret2", true);
                                newText("music.txt");
                                break;
                            case 3:
                                FXGL.set("secret3", true);
                                newText("bear.txt");
                                break;
                        }

                        secret.removeFromWorld();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.secret) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity secret) {
                        int id =  secret.getComponent(Secret.class).getId();

                        switch (id) {
                            case 1:
                                FXGL.set("secret1", true);
                                break;
                            case 2:
                                FXGL.set("secret2", true);
                                newText("music.txt");
                                break;
                            case 3:
                                FXGL.set("secret3", true);
                                newText("bear.txt");
                                break;
                        }

                        secret.removeFromWorld();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.catJumper, Type.hazard) {
                    @Override
                    protected void onCollisionBegin(Entity cat2, Entity spike) {
                        cat2.getComponent(CatJump.class).hit();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.catJumper, Type.lift) {
                    @Override
                    protected void onCollisionBegin(Entity cat2, Entity lift) {
                        cat2.getComponent(CatJump.class).hit();
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.girl, Type.water) {
                    @Override
                    protected void onCollisionBegin(Entity girl, Entity water) {
                        girl.getComponent(Player.class).translate(
                                water.getComponent(Water.class).getTarget()
                        );
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.cat, Type.water) {
                    @Override
                    protected void onCollisionBegin(Entity cat, Entity water) {
                        cat.getComponent(Player.class).translate(
                                water.getComponent(Water.class).getTarget()
                        );
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.catJumper, Type.water) {
                    @Override
                    protected void onCollisionBegin(Entity cat2, Entity water) {
                        cat2.getComponent(CatJump.class).translate(
                                water.getComponent(Water.class).getTarget()
                        );
                    }
                }
        );

        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(Type.patrolPlatform, Type.wall) {
                    @Override
                    protected void onCollisionBegin(Entity plant2, Entity wall) {
                        plant2.getComponent(Plant2.class).change();
                    }
                }
        );
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        Save save = loadSave();

        vars.put("level", save.level());
        vars.put("secret1", save.secret1());
        vars.put("secret2", save.secret2());
        vars.put("secret3", save.secret3());
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new UIFactory());
        FXGL.getGameWorld().addEntityFactory(new ItemFactory());
        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        FXGL.getGameWorld().addEntityFactory(new JumpFactory());
        FXGL.getGameScene().setBackgroundColor(Color.rgb(167, 236, 255));
        FXGL.setLevelFromMap("ui.tmx");
        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("TownTheme.mp3"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}