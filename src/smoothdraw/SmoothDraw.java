package smoothdraw;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * A facade class to calculate the interpolated path from a set of points. The
 * interpolation method and parameters can be varied to experiment with
 * different interpolation methods.
 * 
 * @author yters
 *
 */

public class SmoothDraw {
	/**
	 * The default interpolation method is the Catmull-Rom spline.
	 */
	public static CurveCalc curveInstances = new Catmull(1000);
	/**
	 * Bezier curves up to degree 20 are provided.
	 */
	public static CurveCalc[] bezierCurves = new BezierCurve[20];

	/**
	 * Generate the Bezier curves.
	 * @param numberOfInterpolationPoints Maximum degree of Bezier curves to be pre-computed.
	 */
	public static void generateBezierCurves(int numberOfInterpolationPoints) {
		for(int i = 0; i < numberOfInterpolationPoints; i++) {
			bezierCurves[i] = new BezierCurve(1000, i);
		}
	}

	/**
	 * Calculate the interpolation path from the provided points using interpolation method in {@link curveInstances}.
	 * @param points  Points used to calculate the interpolation path.
	 * @return
	 */
	public static Point[] getCurve(Point[] points) {
		return curveInstances.createInterpolationPath(points);
	}

	public static Point[] smoothDrawTest(LinkedList<Point> stack, double thrshld) {
		// Interpolate between all points
		Point[] points = stack.toArray(new Point[1]);
		LinkedList<Point> interpolationPoints = new LinkedList<Point>();
		for (int i = 0; i < points.length-1; i++) {
			interpolationPoints.addAll(interpolate(points[i], points[i+1], thrshld));
		}
		interpolationPoints.add(points[points.length-1]); // Last point must be added since intrp() doesn't add it.

		// Default to a Bezier curve if the number of received interpolation points does not match the requirement of the current interpolation method. 
		if(stack.size() != curveInstances.degree) curveInstances = bezierCurves[stack.size()];

		// NOTE Draw Bezier curves between all points
		Point[] interpolationPointArray = interpolationPoints.toArray(new Point[0]);
		LinkedList<Point> interpolationCurvePoints = new LinkedList<Point>();
		int polynomialDegree = curveInstances.degree;
		for (int i = 0; i < interpolationPointArray.length - polynomialDegree + 1; i++) {
			Point[] pointSet = Arrays.copyOfRange(interpolationPointArray, i, i+polynomialDegree);
			Point[] curvePoints = getCurve(pointSet);
			interpolationCurvePoints.addAll(Arrays.asList(curvePoints));
		}
		
		return interpolationCurvePoints.toArray(new Point[1]);
	}

	public static LinkedList<Point> interpolate(Point a, Point b, double threshold) {
		double dist = Math.hypot(a.x - b.x, a.y - b.y);
		double steps = dist / threshold;
		double xStep = b.x - a.x;
		double yStep = b.y - a.y;
		LinkedList<Point> points = new LinkedList<Point>();
		
		double ti;
		double x;
		double y;
		for(int i=0; i < steps; i++) {
			ti = trigonometricInterpolation(i, steps);
			x = a.x + xStep * ti;
			y = a.y + yStep * ti;
			points.add(new Point(x, y, 1.0)); // NOTE Doesn't include b
		}
		
		if(points.size() == 0) {
			points.add(a);
		}
		
		return points;
	}

	public static double trigonometricInterpolation(double step, double steps) {
		return Math.acos(1.0 - ((double) step/ (double) steps) * 2.0)/Math.PI;
	}

	/**
	 * Helper function for the visual test in {@link main}.
	 * @param stack List of points for interpolation, stored as a stack.
	 * @param threshold Stub variable so looks the same as {@link smoothDrawTest}.
	 * @return
	 */
	public static Point[] smoothDraw(LinkedList<Point> stack, double threshold) {
		return getCurve(stack.toArray(new Point[1]));
	}

	/**
	 * Simple visual test of interpolation.  Outputs an R script to draw the path. 
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedList<Point> points = new LinkedList<Point>();
		points.push(new Point(0.0, 0.0, 1.0));
		points.push(new Point(0.5, 0.0, 1.0));
		points.push(new Point(0.0, 0.3, 1.0));
		points.push(new Point(1.2, 1.0, 1.0));
		points.push(new Point(1.0, 1.3, 1.0));
		points.push(new Point(1.2, 1.3, 1.0));

		Point[] curvePoints = smoothDraw(points, 0.2);

		int i;

		System.out.print("x <- c(");
		for(i = 0; i < curvePoints.length - 1; i++) System.out.print(curvePoints[i].x + ", ");
		System.out.println(curvePoints[i].x + ");");

		System.out.print("y <- c(");
		for(i = 0; i < curvePoints.length - 1; i++) System.out.print(curvePoints[i].y + ", ");
		System.out.println(curvePoints[i].y + ");");

		System.out.println("plot(x,y);");
	}
}

