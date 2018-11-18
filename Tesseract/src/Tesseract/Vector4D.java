package Tesseract;

public class Vector4D {
	
	private double x, y ,z, w;
	
	public Vector4D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	// Returns distance between two 3D points
	public double distance(Vector4D v) {
		return Math.sqrt((x - v.getX()) * (x - v.getX()) + (y - v.getY()) * (y - v.getY()) + (z - v.getZ()) * (z - v.getZ()) + (w - v.getW()) * (w - v.getW()));
	}
	
	public void print() {
		System.out.println(x + " " + y + " " + z + " " + w);
	}
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}
	
	
	
}
