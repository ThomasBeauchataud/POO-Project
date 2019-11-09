package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handle the window management
 */
public interface WindowInterface {

    /**
     * Resize the window if the scene size is modified
     * @param width double
     * @param height double
     */
    void resize(double width, double height);

    /**
     * Relocate the window if the scene size is modified
     * @param x double
     * @param y double
     */
    void relocate(double x, double y);

    /**
     * Return if the window is highlighted or not
     * @return boolean
     */
    boolean isHighlighted();

    /**
     * Highlight a window with a color
     * @param color Color
     */
    void highlightWindow(Color color);

    /**
     * Unhighlight a window
     */
    void unhighlight();

    /**
     * Return the rectangle (the case view) of the window
     * @return Rectangle
     */
    Rectangle getRectangle();

}
