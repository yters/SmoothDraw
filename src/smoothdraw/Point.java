package smoothdraw;

/**
 * Simple class to contain coordinate points. It is used for both the
 * interpolation points and to store the interpolated path. {@link r} is only
 * used in the latter case, to set the line width.
 * 
 * @author yters
 *
 */

public class Point {
	public double x;
	public double y;
	public double r;

	Point() {
	}

	/**
	 * The constructor just populates the object fields.
	 * 
	 * @param aX
	 *            X coordinate.
	 * @param aY
	 *            Y coordinate.
	 * @param aR
	 *            Radius of point.
	 */
	public Point(double aX, double aY, double aR) {
		x = aX;
		y = aY;
		r = aR;
	}
}
