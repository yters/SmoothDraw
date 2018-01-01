package smoothdrawtest;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.LinkedList;

import smoothdraw.Point;
import smoothdraw.SmoothDraw;

/**
 * Provides a drawing canvas to experiment with various interpolation methods to
 * find out which one looks the most natural.
 * 
 * @author yters
 *
 */

public class SmoothDrawCanvas extends Canvas implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Buffers the image for {@link SmoothDrawCanvas}.
	 */
	BufferedImage backBuffer;
	
	/**
	 * Image the curved lines are drawn to, held by {@link backBuffer}.
	 */
	Graphics backGraphics;

	/**
	 * Interpolation points collected from user to draw the Bezier curve.
	 */
	LinkedList<Point> points = new LinkedList<Point>();
	
	/**
	 * Number of interpolation points held in {@link points} which is set by the interpolation method in {@link SmoothDraw}. 
	 */
    int pointThreshold = 0;

    int count = 0;
	int countThreshold = 2;

	/**
	 * Initialize a canvas and the interpolation method.
	 * @param width Width of canvas.
	 * @param height Height of canvas.
	 */
	public SmoothDrawCanvas(int width, int height) {
		SmoothDraw.generateBezierCurves(20);
		pointThreshold = SmoothDraw.curveInstances.degree;

		setSize(width, height);

		backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		backGraphics = backBuffer.createGraphics();
		backGraphics.setColor(Color.black);
		backGraphics.fillRect(0, 0, width, height);
		backGraphics.setColor(Color.white);

		addMouseMotionListener(this);
		addMouseListener(this);
	}

	/**
	 * As the mouse is dragged, points from the drag path are added to the
	 * interpolation points. The number of points to trigger an interpolation is
	 * set by {@link pointThreshold}, and the frequency of sampling points from
	 * the mouse drag path is set by {@link countThreshold}.
	 */
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if(count == countThreshold) {
			points.push(new Point(x, y, 8.0));
			while(points.size() > pointThreshold) points.removeLast();
			count = 0;
		} else {
			count++;
		}

		if(points.size() == pointThreshold) {
			Point[] curvePoints = SmoothDraw.smoothDraw(points, 40.0); 
			drawCurve(curvePoints, 4);
		} else {
			backGraphics.fillOval(x-1,y-1,0,0);
		}

		repaint();
		e.consume();
	}

	/** 
	 * Draws the interpolation path onto the widget canvas buffer in {@link backGraphics}.
	 * @param curvePoints Points in the interpolated curve path.
	 * @param lineWidth Size of line.
	 */
	public void drawCurve(Point[] curvePoints, int lineWidth) {
	    int x = 0;
	    int y = 0;
	    for(int i = 0; i < curvePoints.length; i++) {
	    	x = (int) (curvePoints[i].x - (lineWidth/2.0));
	    	y = (int) (curvePoints[i].y - (lineWidth/2.0));
	    	backGraphics.fillOval(x, y, lineWidth, lineWidth);
	    }
	}

	/**
	 * Update the graphics buffer with the latest lines drawn by user. Not seen
	 * by the user until pointed onto the widget by {@link paint}.
	 */
	public void update(Graphics g) {
		g.drawImage(backBuffer, 0, 0, this);
	}

	/**
	 * Update the widget canvas with the graphics buffer.  This is what is actually seen by the user.
	 */
	public void paint(Graphics g) {
		update(g);
	}

	/**
	 * When mouse is released, clear the interpolation points so new drag paths
	 * are not interpolated with old drag paths.
	 */
	public void mouseReleased(MouseEvent e) {
		points.clear();
	}

	public static void main(String[] args) {
		int width = 1024;
		int height = 768;
		SmoothDrawCanvas surface = new SmoothDrawCanvas(width, height);

		JFrame paper = new JFrame("Smooth Draw");
		paper.add(surface);
		paper.setSize(width, height);
		paper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		paper.setVisible(true);
	}
	
	/**
	 * Unused.
	 */
	public void mouseMoved(MouseEvent e) { }
	/**
	 * Unused.
	 */
	public void mouseExited(MouseEvent e) { }
	/**
	 * Unused.
	 */
	public void mouseEntered(MouseEvent e) { }
	/**
	 * Unused.
	 */
	public void mousePressed(MouseEvent e) { }
	/**
	 * Unused.
	 */
	public void mouseClicked(MouseEvent e) { }
}
