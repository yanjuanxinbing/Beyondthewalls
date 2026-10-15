package com.beyondthewalls.components;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.beyondthewalls.utils.JumpStatus;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

public class CatJump extends Component {
    private PhysicsComponent physics;
    private final SimpleObjectProperty<JumpStatus> status = new SimpleObjectProperty<>(JumpStatus.waiting);
    private double powerTime = 0;
    private final int maxTime = 1;
    private static final Sound fail = FXGL.getAssetLoader().loadSound("fail.wav");
    private static final Sound hit =  FXGL.getAssetLoader().loadSound("hit.wav");
    private Entity energy;

    @Override
    public void onAdded() {
        powerTime = 0;
        energy = null;

        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        FixtureDef td = new FixtureDef();
        td.friction(1);
        physics.setFixtureDef(td);

        entity.addComponent(physics);
        entity.addComponent(new CollidableComponent(true));
        entity.setScaleX(-1);
    }

    @Override
    public void onUpdate(double tpf) {
        if (entity.getPosition().getY() > 704) {
            FXGL.getAudioPlayer().playSound(fail);
            translate(new Point2D(192, 576));

            // 本实体已脱离世界，其物理组件随即失效，必须在触碰 physics 前返回
            return;
        }

        if (status.get() == JumpStatus.jumpBegin) {
            if (powerTime < maxTime) {
                powerTime += tpf;
                if (powerTime > maxTime) {
                    powerTime = maxTime;
                }
            }
        } else if (physics.getVelocityY() == 0) {
            if (physics.getVelocityX() == 0) {
                stop();
            } else {
                status.set(JumpStatus.waiting);
            }
        }
    }

    public void begin() {
        status.set(JumpStatus.jumpBegin);

        if (energy == null) {
            energy = FXGL.spawn("energy", entity.getPosition().add(-16, -16));
        }
    }

    public void release() {
        status.set(JumpStatus.jumping);
        physics.setVelocityX(300 * powerTime / maxTime);
        physics.setVelocityY(-300 * powerTime / maxTime);
        powerTime = 0;

        clearEnergy();
    }

    public void stop() {
        physics.setVelocityX(0);
        status.set(JumpStatus.waiting);
    }

    public void hit() {
        status.set(JumpStatus.jumping);
        FXGL.getAudioPlayer().playSound(hit);
        physics.setVelocityX(-150);
        physics.setVelocityY(-150);
        powerTime = 0;

        clearEnergy();
    }

    public void pushX(double power) {
        physics.setVelocityX(power);
    }

    public void pushY(double power) {
        physics.setVelocityY(power);
    }

    /**
     * 传送。调用前必须回收蓄力条，否则在蓄力途中死亡或进入水流时，
     * 那条蓄力条会作为孤儿实体永久留在世界里。
     */
    public void translate(Point2D target) {
        clearEnergy();
        powerTime = 0;
        entity.removeFromWorld();
        FXGL.spawn("catJumper", target);
    }

    private void clearEnergy() {
        if (energy != null) {
            energy.removeFromWorld();
            energy = null;
        }
    }

    public SimpleObjectProperty<JumpStatus> statusProperty() {
        return status;
    }

    public JumpStatus getJumpStatus() {
        return status.get();
    }
}
