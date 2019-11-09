package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Translate;

public class Window extends Group {

	private Rectangle rectangle;
	private Translate pos;
	private boolean highlighted;

	/**
	 * Make a new Rectangle and Translate, add the Translate to the Rectangle, add the Rectagle to the Group
	 * @param i int
	 */
	public Window(int i) {
		pos = new Translate();
		rectangle = new Rectangle();
		highlighted = false;
		rectangle.getTransforms().add(pos);
		if(i==0) {
			rectangle.setFill(Color.GREY);
		}
		else {
			rectangle.setFill(Color.WHITE);
		}
		getChildren().add(rectangle);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * Call the super class method and update the height and the width of the rectangle representing the window
	 * @param width double
	 * @param height double
	 */
	@Override
	public void resize(double width, double height) {
		super.resize(width, height);
		rectangle.setHeight(height);
		rectangle.setWidth(width);
	}

	/**
	 * Call the superclass method and update the relevant transform
	 * @param x double
	 * @param y double
	 */
	@Override
	public void relocate(double x, double y) {
		super.relocate(x, y);
		pos.setX(x);
		pos.setY(y);
	}

	public void highlightWindow(Color color) {
		rectangle.setStrokeType(StrokeType.INSIDE);
		rectangle.setStrokeWidth(4);
		rectangle.setStroke(color);
		if (color == Color.GREEN) {
			highlighted = true;
		}
	}

	public void unhighlight() {
		rectangle.setStroke(null);
		highlighted = false;
	}
}
