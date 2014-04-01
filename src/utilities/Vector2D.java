package utilities;

// mutable 2D vectors
public final class Vector2D {

	// fields
	public double x, y; 

	// construct a null vector
	public Vector2D(){
		x = 0; y = 0;
	} 

	// construct a vector with given coordinates
	public Vector2D(double x, double y){
		this.x = x; this.y = y;
	}

	// construct a vector that is a copy of the argument
	public Vector2D(Vector2D v){
		x = v.x; y =v.y;
	}

	// set coordinates 
	public void set (double x, double y) {
		this.x = x; this.y = y;
	} 

	// set coordinates to argument vector coordinates
	public void set (Vector2D v) {
		this.x = v.x; this.y = v.y;
	}

	// compare for equality (needs to allow for Object type argument...) 
	public boolean equals(Object o) {
		Vector2D v = (Vector2D) o;
		return x == v.x && y == v.y;
	}

	//  magnitude (= "length") of this vector 
	public double mag() {
		return Math.sqrt(x*x + y*y);
	}

	// angle between vector and horizontal axis in radians
	public double theta() {
		return Math.atan2(y, x);
	}

	// String for displaying vector as text 
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	// add argument vector 
	public void add(Vector2D v) {
		x+=v.x;
		y+=v.y;
	}

	// add coordinate values 
	public void add(double x, double y) {
		this.x+=x;
		this.y+=y;
	}

	// weighted add - frequently useful 
	public void add(Vector2D v, double fac) {
		x+=v.x*fac;
		y+=v.y*fac;
	}

	// multiply with factor 
	public void mult(double fac) {
		x*=fac;
		y*=fac;
	}

	// "wrap" vector with respect to given positive values w and h
	// method assumes that x >= -w and y >= -h
	public void wrap(double w, double h) {
		if (x < 0) {
			x += w;
		}
		else if (x > w) {
			x -= w;
		}
		if (y < 0) {
			y += h;
		}
		else if (y > h) {
			y -= h;
		}
	}

	// rotate by angle given in radians 
	public void rotate(double theta) {
		double xn = x*Math.cos(theta)-y*Math.sin(theta);
		double yn = x*Math.sin(theta)+y*Math.cos(theta);
		x = xn; y = yn;
	}

	// scalar product with argument vector 
	public double scalarProduct(Vector2D v) {
		return (x*v.x) + (y*v.y);
	}

	// distance to argument vector 
	public double dist(Vector2D v) {
		double xn = x-v.x;
		double yn = y-v.y;
		return Math.sqrt(xn*xn+yn*yn);
		
	}

	// normalise vector so that mag becomes 1
	// direction is unchanged  
	public void normalise() {
		double magn = mag();
		x = x/magn;
		y = y/magn;
	}
	}
