package model.Tools;

import javafx.animation.Transition;
import javafx.scene.Node;

import javafx.util.Duration;

public class FadeTransition extends Transition {

    private Node target;
    private double helper;

    public FadeTransition(Node node, Duration duration){
        this.target = node;
        this.setCycleDuration(duration);
        helper = 1.0;
        this.setCycleCount(1);
    }

    @Override
    protected void interpolate(double a) {
        target.setOpacity(Math.abs(1.0 - helper - ((double) Math.round(a * 100) / 100)));
    }

    public void playReverse(){
        reverse();
        this.play();
        this.setOnFinished(e -> reverse());
    }

    private void reverse(){
        helper = helper == 0.0 ? 1.0 : 0.0;
    }
}
