package com.beyondthewalls.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

public class Barrier extends Component {
    @Override
    public void onAdded() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        FixtureDef td = new FixtureDef();
        td.friction(0);
        physics.setFixtureDef(td);

        CollidableComponent collidable = new CollidableComponent(true);

        entity.addComponent(physics);
        entity.addComponent(collidable);
    }
}