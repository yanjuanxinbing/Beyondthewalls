package com.beyondthewalls.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

public class Plant2 extends Component {
    private PhysicsComponent physics;
    private boolean movetoRight;

    @Override
    public void onAdded() {
        movetoRight = true;

        physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        FixtureDef td = new FixtureDef();
        td.friction(1);
        physics.setFixtureDef(td);

        CollidableComponent collidable = new CollidableComponent(true);

        entity.addComponent(physics);
        entity.addComponent(collidable);
    }

    @Override
    public void onUpdate(double tpf) {
        if (movetoRight) {
            physics.setVelocityX(64);
        } else {
            physics.setVelocityX(-64);
        }
    }

    public void change() {
        movetoRight = !movetoRight;
    }
}
