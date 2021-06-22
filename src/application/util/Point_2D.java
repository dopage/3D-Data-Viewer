package application.util;

public class Point_2D {

	private double x;
	private double y;
	
	public Point_2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point_2D)) return false;
        Point_2D other = (Point_2D) obj;
        if (!(x == other.x)) return false;
        else if (y != other.y) return false;
        return true;
	}
}
