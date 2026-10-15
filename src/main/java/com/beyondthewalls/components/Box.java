package com.beyondthewalls.components;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

public class Box extends Component {
    private PhysicsComponent physics;
    private final Sound push = FXGL.getAssetLoader().loadSound("push.wav");
    private double playtime;

    @Override
    public void onAdded() {
        playtime = 0;
        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        CollidableComponent collidable = new CollidableComponent(true);

        entity.addComponent(physics);
        entity.addComponent(collidable);
    }

    @Override
    public void onUpdate(double tpf) {
        if (Math.abs(physics.getVelocityX()) > 0) {
            if (playtime == 0) {
                FXGL.getAudioPlayer().playSound(push);
                playtime = 1;
            }
        }

        if (playtime > 0) {
            playtime -= tpf;
            if (playtime < 0) {
                playtime = 0;
            }
        }
    }
}
