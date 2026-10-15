package com.beyondthewalls.components;

import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class Bat extends Component {
    @Override
    public void onAdded() {
        LiftComponent liftComponent = new LiftComponent();
        liftComponent.setGoingUp(true);
        liftComponent.yAxisSpeedDistance(100, 160);

        CollidableComponent collidableComponent = new CollidableComponent(true);

        entity.addComponent(collidableComponent);
        entity.addComponent(liftComponent);
    }
}
