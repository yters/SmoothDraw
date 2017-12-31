package smoothdraw;

import java.util.Arrays;

public class RDP {
    static public Point[] RDPalg(Point[] pnts, double eps) {
	Point[] result = null;
		
	double dmax = 0.0;
	int index = 0;
		
	double d = 0.0;
		
	for(int i = 1; i < pnts.length - 1; i++) {
	    d = perpdist(pnts[i], line(pnts[0], pnts[pnts.length - 1]));
	    if(d > dmax) {
		index = i;
		dmax = d;
	    }
	}
		
	Point[] result1;
	Point[] result2;
		
	if(dmax >= eps) {
	    result1 = RDPalg(Arrays.copyOfRange(pnts, 0, index), eps);
	    result2 = RDPalg(Arrays.copyOfRange(pnts, index, pnts.length), eps);
			
	    result = new Point[result1.length + result2.length];
	    System.arraycopy(result1, 0, result, 0, result1.length);
	    System.arraycopy(result2, 0, result, result1.length, result2.length);
	} else {
	    result = Arrays.asList(pnts[0], pnts[pnts.length - 1]).toArray(new Point[1]);
	}
		
	return result;
    }

    static public double perpdist(Point pnt, double[] line) {
	return Math.abs(line[0] * pnt.x - pnt.y + line[1]) / Math.sqrt(Math.pow(line[0], 2.0) + 1.0);
    }
	
    static public double[] line(Point a, Point b) {
	double[] result = new double[2];
		
	result[0] = (b.y - a.y) / (b.x - a.x);
	result[1] = a.y - result[0] * a.x;
		
	return result;
    }
}
