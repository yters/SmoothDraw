package com.smoothdraw;

import java.util.Arrays;

/**
 * Precomputes the coefficients for a Bezier curve of specified degree and granularity.
 * @author yters
 *
 */
public class BezierCurve extends CurveCalc {
	/**
	 * The coefficients of the Bezier path equation.
	 */
	public double[][] coefficients;
	
	/**
	 * Granularity of the calculated Bezier path.
	 */
	public int steps;

	/**
	 * Calculates binomial coefficient.
	 * @param n Number of options.
	 * @param k How many options chosen.
	 * @return {@link n} choose {@link k}.
	 */
	int nCk(int n, int k) {
		int r = 1;
		for (int i = k+1; i <= n; i++) {
			r *= i;
			r /= i-k;
		}
		return r;
	}

	/**
	 * Creates a Bezier curve interpolation equation with precomputed coefficients.
	 * @param aSteps Granularity of interpolation: many steps for high granularity, few steps for low granularity.
	 * @param aDegree The number of points used for interpolation sets the degree of the polynomial. 
	 */
	public BezierCurve(int aSteps, int aDegree) {
		steps = aSteps;
		degree = aDegree;
		coefficients = new double[steps][degree];

		double inc = 1.0/steps;
		double t = 0.0;
		int n = degree - 1;
		double t1;
		double t2;
		double coef;
		for(int i = 0; i < steps; i++) {
			t = inc * i;		
			for(int j = 0; j <= n; j++) {
				t1 = Math.pow(1-t, n - j);
				t2 = Math.pow(t, j);
				coef = t1 * t2 * nCk(n, j);
				coefficients[i][j] = coef;
			}
		}
	}

	/**
	 * Bezier curve interpolation is calculated using an Nth degree polynomial with N points.
	 */
	public Point[] createInterpolationPath(Point[] pnts) {
		Point[] curvePoints = new Point[steps];
		for(int i = 0; i < steps; i++) {
			curvePoints[i] = new Point();
			curvePoints[i].x = 0.0;
			curvePoints[i].y = 0.0;

			for(int j = 0; j < degree; j++) {
				curvePoints[i].x += pnts[j].x * coefficients[i][j];
				curvePoints[i].y += pnts[j].y * coefficients[i][j];
			}
		}
		return Arrays.copyOfRange(curvePoints, 0, curvePoints.length);
	}
}

