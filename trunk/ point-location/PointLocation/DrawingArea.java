import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
class DrawingArea extends Canvas
    implements MouseListener, MouseMotionListener
{
	static final String BUILDING = "building";
    static final String FASTBUILD = "fastbuild";
    static final String QUERY = "query";
    static final String INSERTING = "inserting";
    private String state;
    private int MAX;
    public int numVertices;
    public int numSegments;
    private int counter;
    private int inserted;
    public boolean fast;
    public Segment segments[];
    public Segment randSeg[];
    public Point points[];
    public ArrayList<Trapezoid> trap;
    public Point queryPoint;
    private Point tmpVerticeS;
    private Point tmpVerticeE;
    private TrapezoidPolygon trapezoid;
    private Component object;
    private StatusBar status;
    private int NBSEGMENTS = 35000 ;
    Graphics g;
    Graphics g2;

    DrawingArea(Component component, StatusBar statusBar)
    {
    	status = statusBar;
        state = "inserting";
        MAX = 10000000;
        inserted = -1;
        fast = true;
        segments = new Segment[MAX];
        randSeg = new Segment[MAX];
        points = new Point[MAX];
        trap = new ArrayList<Trapezoid>();
        queryPoint = new Point();
        setSize(1000, 600);
        addMouseListener(this);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		addMouseMotionListener(this);
		
		int ecart = 1;
		for(int i = 0; i<NBSEGMENTS; i+=2)
		{
			Point p1 = new Point(2*i*ecart, 10, 4*i);
			Point p2 = new Point((2*i+1)*ecart, 200, 4*i+1);
			Segment s = new Segment(p1, p2);
			segments[i] = s;
			
			p1 = new Point((2*i+1)*ecart, 300, 4*i+2);
			p2 = new Point(2*i*ecart, 500, 4*i+3);
			s = new Segment(p1, p2);
			segments[i+1] = s;
			
			numSegments+= 2;
			numVertices += 4;
		}
    }

    public void draw()
    {
        Dimension dimension = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, dimension.width - 1, dimension.height - 1);
        g.setColor(Color.black);
        g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
        if(true)
        {
            if(trapezoid != null)
            {
                g.setColor(new Color(255, 218, 185));
                g.fillPolygon(trapezoid.x, trapezoid.y, 4);
            }
            trapezoid = null;
        }
        for(int i = 0; i < numSegments; i++)
        {
            g.setColor(Color.black);
            Segment lsegment = segments[i];
            if(lsegment != null)
            {
                g.drawLine(lsegment.startp().getX(), lsegment.startp().getY(), lsegment.endp().getX(), lsegment.endp().getY());
                Point point = lsegment.left;
                Point vertice1 = lsegment.right;
                if(state != "inserting" && i <= inserted && i >= 0 && (state == "building" || state == "fastbuild" || state == "query") && point != null && vertice1 != null)
                {
                    g.setColor(new Color(255, 127, 0));
                    g.drawLine(point.getX(), point.up, point.getX(), point.down);
                    g.drawLine(vertice1.getX(), vertice1.up, vertice1.getX(), vertice1.down);
                }
            }
        }

        for(int j = 0; j < numVertices; j++)
            if(points[j] != null)
            {
                g.setColor(Color.black);
                g.fillOval(points[j].getX() - 5, points[j].getY() - 5, 6, 6);
            }
        for(int k = 0; k < trap.size(); k++)
        {
        	Trapezoid tr = trap.get(k);
        	g.drawString(tr.name, tr.getMiddleX(), tr.getMiddleY());
        }

        if(state == "query")
        {
            g.setColor(Color.red);
            g.fillOval(queryPoint.getX() - 5, queryPoint.getY() - 5, 5, 5);
        }
        if(numVertices == MAX)
        {
            g.setColor(Color.black);
            g.drawString("Impossible to add more points. Max = " + numVertices, 0, 15);
        }
    }

  
    
    public void update(Graphics g1)
    {
        paint(g1);
    }

    public Insets getInsets()
    {
        return new Insets(5, 5, 5, 5);
    }

    public void drawTrapezoids(boolean flag)
    {
        fast = flag;
        inserted = -1;
    }

    public void drawMap(Segment alsegment[], boolean flag)
    {
        if(fast && !flag)
            state = "fastbuild";
        else
            state = "building";
        randSeg = alsegment;
    }

    public void setInserted(int i)
    {
        inserted = i;
    }

    public void paint(Graphics g1)
    {
        g = getGraphics();
        draw();
        if(!fast && object != null)
        {
            synchronized(object)
            {
                object.notifyAll();
            }
            return;
        } else
        {
            return;
        }
    }

    public void setObject(Component component)
    {
        object = component;
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    	status.displayCoord(mouseevent.getX(), mouseevent.getY());
        if(state == "inserting" && numVertices < MAX)
        {
            if(numVertices == 0)
            {
                Point point = new Point(mouseevent.getX(), mouseevent.getY(), numVertices);
                points[numVertices] = point;
                tmpVerticeS = point;
                numVertices++;
                counter = 1;
            } else
            if(numVertices < MAX)
            {
                boolean flag = false;
                boolean flag3 = false;
                Point vertice1 = null;
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                for(int k = 0; k < numVertices; k++)
                {
                    if(points[k].getX() >= i + 6 || points[k].getX() <= i - 6 || points[k].getY() >= j + 6 || points[k].getY() <= j - 6)
                        continue;
                    vertice1 = points[k];
                    flag = true;
                    break;
                }

                if(!flag)
                {
                    Point vertice2 = new Point(i, j, numVertices);
                    vertice1 = vertice2;
                }
                if(counter == 1)
                {
                    tmpVerticeE = vertice1;
                    counter = 2;
                    if(tmpVerticeS.getIndex() == tmpVerticeE.getIndex())
                    {
                        tmpVerticeS = vertice1;
                        counter = 1;
                    }
                } else
                if(counter == 0)
                {
                    tmpVerticeS = vertice1;
                    counter = 1;
                }
                if(counter == 2)
                {
                    boolean flag2 = false;
                    for(int l = 0; l < numSegments; l++)
                    {
                        if(segments[l].startp().getIndex() != tmpVerticeS.getIndex() && segments[l].startp().getIndex() != tmpVerticeE.getIndex() || segments[l].endp().getIndex() != tmpVerticeS.getIndex() && segments[l].endp().getIndex() != tmpVerticeE.getIndex())
                            continue;
                        flag2 = true;
                        break;
                    }

                    if(!flag2)
                    {
                        Segment lsegment = new Segment(tmpVerticeS, tmpVerticeE);
                        for(int i1 = 0; !flag3 && i1 < numSegments; i1++)
                        {
                            flag3 = Computation.areNotCrossing(lsegment, segments[i1]);
                            if(flag3 && (lsegment.startp().getIndex() == segments[i1].startp().getIndex() || lsegment.startp().getIndex() == segments[i1].endp().getIndex() || lsegment.endp().getIndex() == segments[i1].endp().getIndex() || lsegment.endp().getIndex() == segments[i1].startp().getIndex()))
                                flag3 = false;
                        }

                        if(!flag3)
                        {
                            segments[numSegments] = lsegment;
                            numSegments++;
                        }
                    }
                    counter = 0;
                }
                if(!flag && !flag3)
                {
                    points[numVertices] = vertice1;
                    numVertices++;
                }
            }
        } else
        if(state == "query")
        {
            queryPoint.x = mouseevent.getX();
            queryPoint.y = mouseevent.getY();
        }
        repaint();
    }

    public void quering()
    {
        state = "query";
        repaint();
    }
    public void clear()
    {
        numVertices = 0;
        numSegments = 0;
        counter = 0;
        inserted = -1;
        state = "inserting";
        trap.clear();
        repaint();
    }

    public void repaintArea()
    {
        repaint();
    }

    public void setTrapezoid(TrapezoidPolygon trpolygon)
    {
        trapezoid = trpolygon;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }
    
    public void mouseMoved(MouseEvent e)
    {
    	float x = e.getX();
		float y = e.getY();
    	status.displayCoord(x, y);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
    	status.displayCoord(x, y);		
	}

}
