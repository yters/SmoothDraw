package smoothdraw;

import java.util.Arrays;

public class BezierCurve extends CurveCalc {
    public double[][] coefs;
    public int steps;

    int nCk(int n, int k) {
	int r = 1;
	for (int i = k+1; i <= n; i++) {
	    r *= i;
	    r /= i-k;
	}
	return r;
    }
    
    public BezierCurve(int f_steps, int f_polynum) {
	steps = f_steps;
	polynum = f_polynum;
	coefs = new double[steps][polynum];
	double increment = 1.0/steps;
	double t = 0.0;
	int n = polynum - 1;
	for(int i = 0; i < steps; i++) {
	    t = increment * i;		
	    for(int j = 0; j <= n; j++) {
		double t1 = Math.pow(1-t, n - j);
		double t2 = Math.pow(t, j);
		double coef = t1 * t2 * nCk(n, j);
		coefs[i][j] = coef;
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

