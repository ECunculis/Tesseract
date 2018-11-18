package Tesseract;

import java.awt.Color;
import java.awt.Graphics2D;

public class Tesseract {

	private Vector4D[] TesseractPoint;
	private Vector4D[] RotatedTesseract;
	private Vector4D[] ProjectedTesseractTo3D;
	private Vector4D[] ProjectedTesseractTo2D;
	private double size;
	private double pointSize;

	// xy, xz, xw, yz, yw, zw
	private double xAngle, yAngle, xwAngle, ywAngle;
	private double angle, angleSpeed;
	private double distance;
	private Matrix yRotMatrix, zRotMatrix, xRotMatrix, zwRotMatrix, ywRotMatrix, xwRotMatrix, ProjectionMatrix;

	private Mouse mouse;

	public Tesseract(Mouse mouse) {
		this.mouse = mouse;
		size = 100;
		angleSpeed = 0.01;
		pointSize = 6;
		distance = 420;

		TesseractPoint = new Vector4D[16];
		RotatedTesseract = new Vector4D[16];
		ProjectedTesseractTo3D = new Vector4D[16];
		ProjectedTesseractTo2D = new Vector4D[16];

		TesseractPoint[0] = new Vector4D(size, size, size, size);
		TesseractPoint[1] = new Vector4D(size, -size, size, size);
		TesseractPoint[2] = new Vector4D(-size, -size, size, size);
		TesseractPoint[3] = new Vector4D(-size, size, size, size);
		TesseractPoint[4] = new Vector4D(size, size, -size, size);
		TesseractPoint[5] = new Vector4D(size, -size, -size, size);
		TesseractPoint[6] = new Vector4D(-size, -size, -size, size);
		TesseractPoint[7] = new Vector4D(-size, size, -size, size);

		TesseractPoint[8] = new Vector4D(size, size, size, -size);
		TesseractPoint[9] = new Vector4D(size, -size, size, -size);
		TesseractPoint[10] = new Vector4D(-size, -size, size, -size);
		TesseractPoint[11] = new Vector4D(-size, size, size, -size);
		TesseractPoint[12] = new Vector4D(size, size, -size, -size);
		TesseractPoint[13] = new Vector4D(size, -size, -size, -size);
		TesseractPoint[14] = new Vector4D(-size, -size, -size, -size);
		TesseractPoint[15] = new Vector4D(-size, size, -size, -size);

		update();
	}

	public void update() {
		if (mouse.isLeftButtonPressed()) {
			xAngle = -mouse.getxDelta() / 100;
			yAngle = -mouse.getyDelta() / 100;
		}
		if (mouse.isRightButtonPressed()) {
			xwAngle = mouse.getXwDelta() / 100;
			ywAngle = mouse.getYwDelta() / 100;
		}

		// angle += angleSpeed;
		rotationMatrixUpdate();

		// Rotate the cube
		for (int i = 0; i < 16; i++) {
			RotatedTesseract[i] = yRotMatrix.vector4DMul(TesseractPoint[i]);
			RotatedTesseract[i] = xRotMatrix.vector4DMul(RotatedTesseract[i]);
			RotatedTesseract[i] = xwRotMatrix.vector4DMul(RotatedTesseract[i]);
			RotatedTesseract[i] = ywRotMatrix.vector4DMul(RotatedTesseract[i]);

		}

		// Perspective projection from 4D to 3D
		for (int i = 0; i < 16; i++) {
			double w = (size * 4) / (distance - RotatedTesseract[i].getW());
			double[][] temp = new double[][] {
					{ w, 0, 0, 0 },
					{ 0, w, 0, 0 },
					{ 0, 0, w, 0 },
					{ 0, 0, 0, 0 },
			};
			ProjectionMatrix = new Matrix(temp);
			ProjectedTesseractTo3D[i] = ProjectionMatrix.vector4DMul(RotatedTesseract[i]);

		}

		// Perspective projection from 3D to 2D
		for (int i = 0; i < 16; i++) {
			double z = (size * 4) / (distance - ProjectedTesseractTo3D[i].getZ());
			double[][] temp = new double[][] {
					{ z, 0, 0, 0 },
					{ 0, z, 0, 0 },
					{ 0, 0, 0, 0 },
					{ 0, 0, 0, 0 },
			};
			ProjectionMatrix = new Matrix(temp);
			ProjectedTesseractTo2D[i] = ProjectionMatrix.vector4DMul(ProjectedTesseractTo3D[i]);

		}

	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);

		g2d.drawString("Left button + moving up and down: yz rotation", 20, 30);
		g2d.drawString("Left button + moving left and right: xz rotation", 20, 50);
		g2d.drawString("Right button + moving up and down: yw rotation", 20, 70);
		g2d.drawString("Right button + moving up and down: xw rotation", 20, 90);
		g2d.translate(Window.WIDTH / 2, Window.HEIGHT / 2);

		for (int i = 0; i < ProjectedTesseractTo2D.length; i++) {
			g2d.fillOval((int) (ProjectedTesseractTo2D[i].getX() - pointSize / 2), (int) (ProjectedTesseractTo2D[i].getY() - pointSize / 2), (int) pointSize, (int) pointSize);
		}

		// Draw lines
		for (int i = 0; i < RotatedTesseract.length; i++) {
			for (int j = 0; j < RotatedTesseract.length; j++) {
				if (Math.round(RotatedTesseract[i].distance(RotatedTesseract[j])) == size * 2) {
					g2d.drawLine((int) ProjectedTesseractTo2D[i].getX(), (int) ProjectedTesseractTo2D[i].getY(), (int) ProjectedTesseractTo2D[j].getX(), (int) ProjectedTesseractTo2D[j].getY());
				}
			}
		}

	}

	public void rotationMatrixUpdate() {

		// X rotation matrix (yz)
		double[][] temp = new double[][] {
				{ 1, 0, 0, 0 },
				{ 0, Math.cos(yAngle), -Math.sin(yAngle), 0 },
				{ 0, Math.sin(yAngle), Math.cos(yAngle), 0 },
				{ 0, 0, 0, 1 }
		};
		xRotMatrix = new Matrix(temp);

		// Y rotation matrix (xz)
		temp = new double[][] {
				{ Math.cos(xAngle), 0, -Math.sin(xAngle), 0 },
				{ 0, 1, 0, 0 },
				{ Math.sin(xAngle), 0, Math.cos(xAngle), 0 },
				{ 0, 0, 0, 1 }
		};

		yRotMatrix = new Matrix(temp);

		// Z rotation matrix (xy)
		temp = new double[][] {
				{ Math.cos(angle), -Math.sin(angle), 0, 0 },
				{ Math.sin(angle), Math.cos(angle), 0, 0 },
				{ 0, 0, 1, 0 },
				{ 0, 0, 0, 1 }
		};

		zRotMatrix = new Matrix(temp);

		// XW rotation matrix
		temp = new double[][] {
				{ Math.cos(xwAngle), 0, 0, Math.sin(xwAngle) },
				{ 0, 1, 0, 0 },
				{ 0, 0, 1, 0 },
				{ -Math.sin(xwAngle), 0, 0, Math.cos(xwAngle) },
		};

		xwRotMatrix = new Matrix(temp);

		// YW rotation matrix
		temp = new double[][] {
				{ 1, 0, 0, 0 },
				{ 0, Math.cos(ywAngle), 0, -Math.sin(ywAngle) },
				{ 0, 0, 1, 0 },
				{ 0, Math.sin(ywAngle), 0, Math.cos(ywAngle) },
		};

		ywRotMatrix = new Matrix(temp);

		// ZW rotation matrix
		temp = new double[][] {
				{ 1, 0, 0, 0 },
				{ 0, 1, 0, 0 },
				{ 0, 0, Math.cos(angle), -Math.sin(angle) },
				{ 0, 0, Math.sin(angle), Math.cos(angle) },
		};

		zwRotMatrix = new Matrix(temp);
	}

}
