package com.smoothdraw;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import java.util.LinkedList;

/**
 * Provides a drawing canvas to experiment with various interpolation methods.
 * 
 * @author Eric Holloway
 *
 */

public class SmoothDrawWidget extends Canvas implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Buffers the image for {@link SmoothDrawWidget}.
	 */
	BufferedImage backBuffer;
	
	/**
	 * Image the curved lines are drawn to, held by {@link backBuffer}.
	 */
	Graphics backGraphics;

	/** 
	 * Holds the curve calculation algorithm.
	 */
	CurveCalc smoothDraw;
	
	/**
	 * Interpolation points collected from user to draw the curve.
	 */
	LinkedList<Point> points = new LinkedList<Point>();

	/**
	 * Initialize a canvas and the interpolation method.
	 * @param width Width of canvas.
	 * @param height Height of canvas.
	 */
	public SmoothDrawWidget(int width, int height) {
		// Can also be BezierCurve(1000, 4) or Line().
		// Compare the difference between Line and the interpolation methods.
		smoothDraw = new Catmull(1000);

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
	 * set by the degree of the interpolation method.
	 */
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		points.push(new Point(x, y));
		
		if (points.size() > smoothDraw.degree) {
			points.removeLast();
		}

		if(points.size() == smoothDraw.degree) {
			Point[] curvePoints = smoothDraw.createInterpolationPath(points); 
			drawCurve(curvePoints, 4);
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
		SmoothDrawWidget surface = new SmoothDrawWidget(width, height);

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
