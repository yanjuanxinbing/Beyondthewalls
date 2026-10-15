package com.beyondthewalls.components;

import com.almasb.fxgl.audio.Sound;
import com.beyondthewalls.Main;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.SensorCollisionHandler;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.beyondthewalls.utils.Type;
import javafx.util.Duration;

public class End extends Component {
    private final double width, height;
    private boolean canEnd;
    private int arrived;
    private final Sound win = FXGL.getAssetLoader().loadSound("win.wav");

    public End(double width, double height) {
        this.width = width;
        this.height = height;
    }

    private void success() {
        FXGL.inc("level", 1);
        Main.save();
        FXGL.getNotificationService().pushNotification("关卡通关！");
        FXGL.getAudioPlayer().playSound(win);
        FXGL.spawn("loading");
        FXGL.getGameTimer().runOnceAfter(() -> FXGL.runOnce(() -> FXGL.setLevelFromMap(FXGL.gets("nextLevel")), Duration.ZERO), Duration.seconds(3));
    }

    /**
     * 移除指定类型的首个实体。实体不存在时静默跳过——结局演出不应因为
     * 某个可选手动移除的实体缺席而抛出 NoSuchElementException。
     */
    private static void removeFirst(Type type) {
        FXGL.getGameWorld().getEntitiesByType(type).stream()
                .findFirst()
                .ifPresent(Entity::removeFromWorld);
    }

    private void end1() {
        Entity end = FXGL.spawn("end1");
        removeFirst(Type.catJumper);
        Main.newText("end1.txt");
        FXGL.getGameScene().getViewport().bindToEntity(end, 0, 0);
    }

    private void end2() {
        Entity end = FXGL.spawn("end2");
        removeFirst(Type.catJumper);
        Main.newText("end2.txt");
        FXGL.getGameScene().getViewport().bindToEntity(end, 0, 0);
    }

    private void end3() {
        Entity end = FXGL.spawn("end3");
        removeFirst(Type.catJumper);
        Main.newText("end3.txt");
        FXGL.getGameScene().getViewport().bindToEntity(end, 0, 0);
    }

    private void gotoEnd() {
        boolean secret2 = FXGL.getb("secret2");
        boolean secret3 = FXGL.getb("secret3");

        if (!secret2 && !secret3) {
            end1();
        } else if (secret2 && secret3) {
            end3();
        } else {
            end2();
        }
    }

    @Override
    public void onAdded() {
        canEnd = false;
        arrived = 0;

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        physics.addSensor(new HitBox(BoundingShape.box(width, height)),
                new SensorCollisionHandler() {
                    @Override
                    protected void onCollisionBegin(Entity other) {
                        if (other.hasComponent(Player.class)) {
                            arrived++;

                            if (arrived == 2 && canEnd) {
                                canEnd = false;
                                success();
                            }
                        } else if (other.hasComponent(CatJump.class) && canEnd) {
                            gotoEnd();
                        }
                    }

                    @Override
                    protected void onCollisionEnd(Entity other) {
                        if (other.hasComponent(Player.class)) {
                            arrived--;
                        }
                    }
                });

        entity.addComponent(physics);
    }

    public void setCanEnd() {
        canEnd = true;
    }
}
