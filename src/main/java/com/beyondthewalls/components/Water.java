package com.beyondthewalls.components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;

public class Water extends Component {
    private final Point2D target;

    public Water(int tx, int ty) {
        target = new Point2D(tx, ty);
    }

    @Override
    public void onAdded() {
        CollidableComponent collidableComponent = new CollidableComponent(true);
        entity.addComponent(collidableComponent);
    }

    public Point2D getTarget() {
        return target;
    }
}
