// File: src/main/java/org/oop/arknoid_oop/Models/GameObject.java
package org.oop.arknoid_oop.Entity;

import javafx.geometry.Bounds;
import javafx.scene.Node;

public abstract class GameObject {

    protected Node view;

    public GameObject(Node view) {
        this.view = view;
    }

    public Node getView() {
        return view;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    public double getX() {
        return view.getLayoutX();
    }

    public double getY() {
        return view.getLayoutY();
    }
}