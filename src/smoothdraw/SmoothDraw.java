package smoothdraw;

import java.util.Arrays;
import java.util.LinkedList;

public class SmoothDraw {
    public static CurveCalc crvinst = new Catmull(1000);
    public static BezierCurve[] bzrcrvs = new BezierCurve[20];
	
    public static void genBzrCrvs() {
	for(int i = 0; i < 20; i++) bzrcrvs[i] = new BezierCurve(1000, i);
    }

    public static Point[] crv(Point[] pnts) {
	return crvinst.calc(pnts);
    }

    public static Point[] smoothdraw(LinkedList<Point> stack, double thrshld) {
	return crv(stack.toArray(new Point[1]));
    }

    public static Point[] smoothdraw_test(LinkedList<Point> stack, double thrshld) {
	// NOTE Interpolate between all points
	Point[] pnts = stack.toArray(new Point[1]);
	LinkedList<Point> intrp_pnts_lst = new LinkedList<Point>();
	for(int i=0; i<pnts.length-1; i++) intrp_pnts_lst.addAll(intrp(pnts[i], pnts[i+1], thrshld));
	intrp_pnts_lst.add(pnts[pnts.length-1]); // NOTE Last point must be added since intrp() doesn't add it.

	if(stack.size() != crvinst.polynum) crvinst = bzrcrvs[stack.size()];
		
	// NOTE Draw Bezier curves between all points
	Point[] intrp_pnts = intrp_pnts_lst.toArray(new Point[0]);
	LinkedList<Point> intrp_crvpnts_lst = new LinkedList<Point>();
	int plynm = crvinst.polynum;
	for(int i=0; i<intrp_pnts.length - plynm + 1; i++) {
	    Point[] pntset = Arrays.copyOfRange(intrp_pnts, i, i+plynm);
	    Point[] crvpnts = crv(pntset);
	    intrp_crvpnts_lst.addAll(Arrays.asList(crvpnts));
	}
	return intrp_crvpnts_lst.toArray(new Point[1]);
    }

    public static LinkedList<Point> intrp(Point a, Point b, double thrshld) {
	double dist = Math.hypot(a.x - b.x, a.y - b.y);
	double steps = dist / thrshld;
	double xstep = b.x - a.x;
	double ystep = b.y - a.y;
	LinkedList<Point> pnts = new LinkedList<Point>();
	for(int i=0; i < steps; i++) pnts.add(new Point(a.x + xstep * trigintrp(i, steps), a.y + ystep * trigintrp(i, steps), 1.0)); // NOTE Doesn't include b
	if(pnts.size() == 0) pnts.add(a);
	return pnts;
    }

    public static double trigintrp(double step, double steps) {
	return Math.acos(1.0 - ((double) step/ (double) steps) * 2.0)/Math.PI;
    }

    public static void main(String[] args) {
	LinkedList<Point> pnts = new LinkedList<Point>();
	pnts.push(new Point(0.0, 0.0, 1.0));
	pnts.push(new Point(0.5, 0.0, 1.0));
	pnts.push(new Point(0.0, 0.3, 1.0));
	pnts.push(new Point(1.2, 1.0, 1.0));
	pnts.push(new Point(1.0, 1.3, 1.0));
	pnts.push(new Point(1.2, 1.3, 1.0));

	Point[] crv_pnts = smoothdraw(pnts, 0.2);

	int i;

	System.out.print("x <- c(");
	for(i = 0; i < crv_pnts.length - 1; i++) System.out.print(crv_pnts[i].x + ", ");
	System.out.println(crv_pnts[i].x + ");");

	System.out.print("y <- c(");
	for(i = 0; i < crv_pnts.length - 1; i++) System.out.print(crv_pnts[i].y + ", ");
	System.out.println(crv_pnts[i].y + ");");

	System.out.println("plot.new();");
	System.out.println("points(x,y);");
    }
}

