package com.beyondthewalls.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

public class Lock extends Component {
    private boolean lock = true;

    @Override
    public void onAdded() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);
        CollidableComponent collidable = new CollidableComponent(true);

        entity.addComponent(physics);
        entity.addComponent(collidable);
    }

    public void unlock() {
        lock = false;
    }

    public boolean isLock() {
        return lock;
    }
}
