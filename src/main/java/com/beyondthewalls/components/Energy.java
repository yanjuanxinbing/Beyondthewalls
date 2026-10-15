package com.beyondthewalls.components;

import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.shape.Rectangle;

public class Energy extends Component {
    private final SimpleDoubleProperty energy = new SimpleDoubleProperty();

    private Texture createEnergyTexture() {
        int width = 64;
        int height = 10;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Stop[] stops = new Stop[] {
                new Stop(0, Color.GREEN),
                new Stop(0.5, Color.YELLOW),
                new Stop(1, Color.RED)
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, width, height);

        WritableImage image = new WritableImage(width, height);
        canvas.snapshot(null, image);

        return new Texture(image);
    }

    @Override
    public void onAdded() {
        Texture texture = createEnergyTexture();

        Rectangle rectangle = new Rectangle(64, 16);
        rectangle.setTranslateX(-64);

        texture.setClip(rectangle);
        entity.getViewComponent().addChild(texture);

        energy.addListener((o, p, q) -> rectangle.setTranslateX(-64 + 64 * Math.min(1, energy.getValue())));
    }

    @Override
    public void onUpdate(double tpf) {
        energy.set(energy.get() + tpf);
    }
}
