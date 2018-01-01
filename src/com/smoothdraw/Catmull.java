package com.smoothdraw;

import java.util.Arrays;

/**
 * Interpolation using the Catmull-Rom spline using a cubic polynomial.
 * 
 * @author yters
 *
 */

public class Catmull extends CurveCalc {
	public double[][] coefficients;
	public int steps;

	public double[][] m = { { 0, 2, 0, 0 }, { -1, 0, 1, 0 }, { 2, -5, 4, -1 }, { -1, 3, -3, 1 } };

	/**
	 * Precompute the coefficients of the Catmull-Rom spline.
	 * 
	 * @param aSteps Granularity of interpolation.
	 */
	public Catmull(int aSteps) {
		steps = aSteps;
		degree = 4;
		coefficients = new double[steps][degree];
		double increment = 1.0 / (steps - 1);
		double t = 0.0;
		for (int i = 0; i < steps; i++) {
			t = increment * i;
			double[] ts = { 1.0, t, Math.pow(t, 2.0), Math.pow(t, 3.0) };
			for (int j = 0; j < degree; j++) {
				coefficients[i][j] = 0.0;
				for (int k = 0; k < degree; k++) {
					coefficients[i][j] += 0.5 * ts[k] * m[k][j];
				}
			}
		}
	}

	/**
	 * Calculate the interpolation path of the Catmull-Rom spline.
	 */
	public Point[] createInterpolationPath(Point[] pnts) {
		Point[] crvpnts = new Point[steps];
		for (int i = 0; i < steps; i++) {
			crvpnts[i] = new Point();
			crvpnts[i].x = 0.0;
			crvpnts[i].y = 0.0;

			for (int j = 0; j < degree; j++) {
				crvpnts[i].x += pnts[j].x * coefficients[i][j];
				crvpnts[i].y += pnts[j].y * coefficients[i][j];
			}
		}
		return Arrays.copyOfRange(crvpnts, 0, crvpnts.length);
	}
}
