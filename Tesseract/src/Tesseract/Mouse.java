package Tesseract;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {

	private boolean leftButtonPressed;
	private boolean rightButtonPressed;
	private double xDelta, yDelta, xwDelta, ywDelta;
	private double xStart, yStart, xwStart, ywStart;
	private double xLast, yLast, xwLast, ywLast;

	public Mouse() {
		xLast = 0;
		yLast = 0;
		xwLast = 0;
		ywLast = 0;
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftButtonPressed = true;
			xStart = e.getX();
			yStart = e.getY();
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightButtonPressed = true;
			xwStart = e.getX();
			ywStart = e.getY();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftButtonPressed = false;
			xLast = xDelta;
			yLast = yDelta;
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			rightButtonPressed = false;
			xwLast = xwDelta;
			ywLast = ywDelta;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (leftButtonPressed) {
			xDelta = e.getX() - xStart + xLast;
			yDelta = e.getY() - yStart + yLast;
		}
		if (rightButtonPressed) {
			xwDelta = e.getX() - xwStart + xwLast;
			ywDelta = e.getY() - ywStart + ywLast;
		}
	}

	public void mouseMoved(MouseEvent e) {

	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public void setLeftButtonPressed(boolean leftButtonPressed) {
		this.leftButtonPressed = leftButtonPressed;
	}

	public double getxDelta() {
		return xDelta;
	}

	public void setxDelta(double xDelta) {
		this.xDelta = xDelta;
	}

	public double getyDelta() {
		return yDelta;
	}

	public void setyDelta(double yDelta) {
		this.yDelta = yDelta;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}

	public double getXwDelta() {
		return xwDelta;
	}

	public double getYwDelta() {
		return ywDelta;
	}

}
