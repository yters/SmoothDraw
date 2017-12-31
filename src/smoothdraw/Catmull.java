package smoothdraw;

import java.util.Arrays;

public class Catmull extends CurveCalc {
    public double[][] coefs;
    public int steps;

    public double[][] m = {{ 0, 2, 0, 0},
			   {-1, 0, 1, 0},
			   { 2,-5, 4,-1},
			   {-1, 3,-3, 1}};
	
    public Catmull(int f_steps) {
	steps = f_steps;
	polynum = 4;
	coefs = new double[steps][polynum];
	double increment = 1.0/(steps - 1);
	double t = 0.0;
	for(int i = 0; i < steps; i++) {
	    t = increment * i;
	    double[] ts = {1.0, t, Math.pow(t,2.0), Math.pow(t,3.0)}; 
	    for(int j = 0; j < polynum; j++) {
		coefs[i][j] = 0.0;
		for(int k = 0; k < polynum; k++) {
		    coefs[i][j] += 0.5 * ts[k] * m[k][j];
		}
	    }
	}
    }

    public Point[] calc(Point[] pnts) {
	Point[] crvpnts = new Point[steps];
	for(int i = 0; i < steps; i++) {
	    crvpnts[i] = new Point();
	    crvpnts[i].x = 0.0;
	    crvpnts[i].y = 0.0;
			
	    for(int j = 0; j < polynum; j++) {
		crvpnts[i].x += pnts[j].x * coefs[i][j];
		crvpnts[i].y += pnts[j].y * coefs[i][j];
	    }
	}
	return Arrays.copyOfRange(crvpnts, 0, crvpnts.length);
    }
}

