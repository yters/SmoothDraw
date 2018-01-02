package com.smoothdraw;

import java.util.LinkedList;

/**
 * Abstract class to interface with various interpolation methods.
 * @author Eric Holloway
 *
 */

public abstract class CurveCalc {
	/**
	 * Degree of the polynomial used for interpolation. The degree is based on
	 * the number of points used.
	 */
	public int degree;

	/**
	 * Create the interpolation path using the specified interpolation method.
	 * 
	 * @param points
	 *            Points used to calculate the path.
	 * @return A new array of points that describe the interpolated path.
	 */
	abstract public Point[] createInterpolationPath(Point[] points);
	
	/**
	 * Handle input for {@createInterpolationPath(Path[] points)} as LinkedList.
	 */
	public Point[] createInterpolationPath(LinkedList<Point> stack) {
		return createInterpolationPath(stack.toArray(new Point[1]));
	}
}
