package com.smoothdraw;

/**
 * Simple class to contain coordinate points. It is used for both the
 * interpolation points and to store the interpolated path.
 * 
 * @author Eric Holloway
 *
 */

public class Point {
	/**
	 * X coordinate.
	 */
	public double x;
	/**
	 * Y coordinate.
	 */
	public double y;

	Point() {
		x = 0.0;
		y = 0.0;
	}

	/**
	 * The constructor just populates the object fields.
	 * 
	 * @param aX
	 *            X coordinate.
	 * @param aY
	 *            Y coordinate.
	 */
	public Point(double aX, double aY) {
		x = aX;
		y = aY;
	}
}
