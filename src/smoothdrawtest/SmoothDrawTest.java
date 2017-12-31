package smoothdrawtest;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.LinkedList;

import smoothdraw.Point;
import smoothdraw.SmoothDraw;

public class SmoothDrawTest {
    public static void main(String[] args) {
	JFrame paper = new JFrame("Smooth Draw");
	SmoothDrawCanvas surface = new SmoothDrawCanvas();
	paper.add(surface);
	paper.setSize(surface.width, surface.height);
	paper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	paper.setVisible(true);
    }
}

class SmoothDrawCanvas extends Canvas implements MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    int width, height;
    BufferedImage backbuffer;
    Graphics backg;

    LinkedList<Point> pnts = new LinkedList<Point>();
    int pntthrshld = 0;
    int cnt = 0;
    int cntthrshld = 2;

    public SmoothDrawCanvas() {
	SmoothDraw.genBzrCrvs();
	pntthrshld = SmoothDraw.crvinst.polynum;

	setSize(1024,768);
		
	width = getSize().width;
	height = getSize().height;

	backbuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	backg = backbuffer.createGraphics();
	backg.setColor( Color.black );
	backg.fillRect( 0, 0, width, height );
	backg.setColor( Color.white );

	addMouseMotionListener( this );
	addMouseListener( this );
    }

    public void mouseDragged( MouseEvent e ) {
	int x = e.getX();
	int y = e.getY();

	if(cnt == cntthrshld) {
	    pnts.push(new Point(x, y, 8.0));
	    while(pnts.size() > pntthrshld) pnts.removeLast();
	    cnt = 0;
	} else {
	    cnt++;
	}

	if(pnts.size() == pntthrshld) {
	    Point[] crvpnts = SmoothDraw.smoothdraw(pnts, 40.0); 
	    draw_crv(crvpnts, 4.0);
	} else {
	    backg.fillOval(x-1,y-1,0,0);
	}
	
	repaint();
	e.consume();
    }

    public void draw_crv(Point[] crvpnts, double r) {
	int i = 0;
	int x = (int) crvpnts[i].x;
	int y = (int) crvpnts[i].y;
	backg.fillOval((int) Math.floor(x - (r/2.0)), (int) Math.floor(y - (r/2.0)), (int) Math.ceil(r), (int) Math.ceil(r));
	for(i = 1; i < crvpnts.length - 1; i++) {
	    x = (int) crvpnts[i].x;
	    y = (int) crvpnts[i].y;
	    backg.fillOval((int) Math.floor(x - (r/2.0)), (int) Math.floor(y - (r/2.0)), (int) Math.ceil(r), (int) Math.ceil(r));
	}
	x = (int) crvpnts[i].x;
	y = (int) crvpnts[i].y;
	backg.fillOval((int) Math.floor(x - (r/2.0)), (int) Math.floor(y - (r/2.0)), (int) Math.ceil(r), (int) Math.ceil(r));
    }

    public void update( Graphics g ) {
	g.drawImage( backbuffer, 0, 0, this );
    }

    public void paint( Graphics g ) {
	update( g );
    }

    public void mouseReleased(MouseEvent e ) {
	pnts.clear();
    }

    public void mouseMoved( MouseEvent e ) { }
    public void mouseExited( MouseEvent e ) { }
    public void mouseEntered( MouseEvent e ) { }
    public void mousePressed( MouseEvent e ) { }
    public void mouseClicked( MouseEvent e ) { }
}
