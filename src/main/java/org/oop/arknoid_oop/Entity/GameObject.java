// File: src/main/java/org/oop/arknoid_oop/Models/GameObject.java
package org.oop.arknoid_oop.Entity;

import javafx.geometry.Bounds;
import javafx.scene.Node;

public abstract class GameObject {

    // 'view' là hình ảnh đại diện (Rectangle, Circle, v.v.)
    protected Node view;

    public GameObject(Node view) {
        this.view = view;
    }

    public Node getView() {
        return view;
    }

    // Lấy "hộp" bao quanh đối tượng để xét va chạm
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