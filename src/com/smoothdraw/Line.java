package com.smoothdraw;

/*
 * Draws a line between two points without interpolation.
 */

import static java.lang.Math.sqrt;

public class Line extends CurveCalc {

	/**
	 * Amount of spacing between plotted points.
	 */
	double spacing;
	
	public Line() {
		degree = 2;
		spacing = 2;
	}

	@Override
	public Point[] createInterpolationPath(Point[] points) {
		Point s = points[0];
		Point f = points[1];
		
		double dx = f.x-s.x;
		double dy = f.y-s.y;
		double d = sqrt(dx*dx + dy*dy);
		
		int steps = (int) (d/spacing);
		
		Point[] result = new Point[steps]; 
		for(int i = 0; i < steps; i++) {
			result[i] = new Point();
			result[i].x = s.x + i*(dx/steps);
			result[i].y = s.y + i*(dy/steps);
		}
		
		return result;
	}

}
