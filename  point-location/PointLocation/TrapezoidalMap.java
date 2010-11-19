import java.awt.*;
import java.util.Random;
/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class TrapezoidalMap extends Panel
{
    InfoArea infoArea;
    static final String xNode = "x";
    static final String yNode = "y";
    static final String trNode = "t";
    static final boolean up = true;
    static final boolean down = false;
    private boolean fast;
    private int nbTrap;
    private Node D;
    private Trapezoid T;
    private DrawingArea drawingArea;
    private Segment S[];
    private int numSegments;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private Component parent;
    private Graphics gr;
    private static int over = 500;
	
    public TrapezoidalMap(DrawingArea data, InfoArea infobox, Component component)
    {
        fast = true;
        numSegments = -1;
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        parent = component;
        drawingArea = data;
        infoArea = infobox;
    }
    

    private Segment[] randPermutation(Segment segment[], int i)
    {
        Random random = new Random();
        for(int k = numSegments - 1; k >= 2; k--)
        {
            int j = (int)(Math.random() * (double)i);
            random.setSeed((long)k * 100L);
            Segment lsegment = segment[k];
            segment[k] = segment[j];
            segment[j] = lsegment;
        }

        return segment;
    }
    
    private void computeBoundingBox(Segment segment[], int i)
    {
        for(int j1 = 0; j1 < i; j1++)
        {
            int j = segment[j1].getStartingPoint().x;
            int k = segment[j1].getStartingPoint().y;
            int l = segment[j1].getEndingPoint().x;
            int i1 = segment[j1].getEndingPoint().y;
            if(j < minX)
                minX = j;
            if(j > maxX)
                maxX = j;
            if(k < minY)
                minY = k;
            if(k > maxY)
                maxY = k;
            if(l < minX)
                minX = l;
            if(l > maxX)
                maxX = l;
            if(i1 < minY)
                minY = i1;
            if(i1 > maxY)
                maxY = i1;
        }

    }

    public boolean createMap(Segment segments[], int i)
    {
        clrData();
        parent.repaint();
        if(i > 0)
        {
            numSegments = i;
            computeBoundingBox(segments, numSegments);
            Point p1 = new Point(minX - over, minY - over, -1);
            Point p2 = new Point(maxX + over, minY - over, -1);
            Point p3 = new Point(minX - over, maxY + over, -1);
            Point p4 = new Point(maxX + over, maxY + over, -1);
            Segment seg1 = new Segment(p1, p2);
            Segment seg2 = new Segment(p3, p4);
            T = new Trapezoid();
            T.name = "root";
            T.top = seg1;
            T.bottom = seg2;
            T.left = p3;
            T.right = p2;
            T.lRight = null;
            T.uRight = null;
            T.lLeft = null;
            T.uLeft = null;
            D = new Node();
            D.node = "t";
            D.t = T;
            T.node = D;
            nbTrap++;

            infoArea.addItem("Randomized insertion of segments in map.");
            S = randPermutation(segments, numSegments);

            insertSegments(S, numSegments);

            infoArea.addItem("Segments inserted in map : " + numSegments+".");
        }
        return true;
    }

    private void insertSegments(Segment segments[], int i)
    {
        drawingArea.drawMap(segments);
        for(int j = 0; j < i; j++)
        {
            drawingArea.setInserted(j);
            Segment lsegment = segments[j];
            Trapezoid tr1 = followSegment(lsegment);
            Trapezoid tr2 = updateT(tr1, lsegment);
            updateD(tr1, lsegment, tr2);
        }

    }

    private void drawTRFound(Trapezoid trapezoid){
    	drawingArea.setTrapezoid(Computation.trapezoidToPolygon(trapezoid));
        drawingArea.setObject(this);
        gr = getGraphics();
        if(gr != null)
        {
            drawingArea.paint(gr);
            gr.dispose();
        }
    }
    
    private void draw(Trapezoid trapezoid)
    {
        if(!drawingArea.fast)
        {
            drawingArea.setTrapezoid(Computation.trapezoidToPolygon(trapezoid));
            drawingArea.setObject(this);
            gr = getGraphics();
            if(gr != null)
            {
                drawingArea.paint(gr);
                gr.dispose();
            }
            synchronized(this)
            {
                try
                {
                    wait(1000L);
                }
                catch(Exception _ex) { }
            }
            return;
        }
        if(!fast)
        {
            gr = getGraphics();
            if(gr != null)
            {
                drawingArea.paint(gr);
                gr.dispose();
            }
        }
    }

    public void paint(Graphics g)
    {
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    private Trapezoid updateT(Trapezoid newTrap, Segment segment)
    {
        int i = 0;
        Trapezoid m_tr = newTrap;
        if(m_tr.next == null)
        {
            Trapezoid trA1 = null;
            Trapezoid trB1 = null;
            Trapezoid trC1 = new Trapezoid();
            Trapezoid trD = new Trapezoid();
            i = 2;
            trC1.left = segment.left;
            trC1.right = segment.right;
            trC1.bottom = segment;
            trC1.top = m_tr.top;
            trC1.name = "C" + nbTrap;
            drawingArea.trap.add(trC1);
            nbTrap++;
            trD.top = segment;
            trD.bottom = m_tr.bottom;
            trD.right = segment.right;
            trD.left = segment.left;
            trD.name = "D" + nbTrap;
            drawingArea.trap.add(trD);
            nbTrap++;
            if(segment.left != m_tr.left)
            {
                trA1 = new Trapezoid();
                trA1.top = m_tr.top;
                trA1.bottom = m_tr.bottom;
                trA1.left = m_tr.left;
                trA1.right = segment.left;
                trA1.lLeft = m_tr.lLeft;
                trA1.uLeft = m_tr.uLeft;
                trA1.lRight = trD;
                trA1.uRight = trC1;
                trA1.node = m_tr.node;
                trA1.name = "A" + nbTrap;
                drawingArea.trap.add(trA1);
                nbTrap++;
                updateLeft(trA1, m_tr);
                trC1.lLeft = null;
                trC1.uLeft = trA1;
                trD.lLeft = trA1;
                trD.uLeft = null;
                i++;
                Computation.setVerticalLine(trA1, null, segment);
            } else
            {
                trC1.uLeft = m_tr.uLeft;
                trC1.lLeft = null;
                trD.lLeft = m_tr.lLeft;
                trD.uLeft = null;
                if(m_tr.uLeft != null)
                    m_tr.uLeft.uRight = trC1;
                if(m_tr.lLeft != null)
                    m_tr.lLeft.lRight = trD;
            }
            if(segment.right != m_tr.right)
            {
                trB1 = new Trapezoid();
                trB1.top = m_tr.top;
                trB1.bottom = m_tr.bottom;
                trB1.left = segment.right;
                trB1.right = m_tr.right;
                trB1.lRight = m_tr.lRight;
                trB1.uRight = m_tr.uRight;
                trB1.lLeft = trD;
                trB1.uLeft = trC1;
                trB1.node = m_tr.node;
                trB1.name = "B" + nbTrap;
                drawingArea.trap.add(trB1);
                nbTrap++;
                updateRight(trB1, m_tr);
                trC1.lRight = null;
                trC1.uRight = trB1;
                trD.lRight = trB1;
                trD.uRight = null;
                i++;
                Computation.setVerticalLine(null, trB1, segment);
            } else
            {
                trC1.uRight = m_tr.uRight;
                trC1.lRight = null;
                trD.lRight = m_tr.lRight;
                trD.uRight = null;
                if(m_tr.uRight != null)
                    m_tr.uRight.uLeft = trC1;
                if(m_tr.lRight != null)
                    m_tr.lRight.lLeft = trD;
            }
            trD.node = m_tr.node;
            trC1.node = m_tr.node;
            trC1.next = trD;
            trD.next = null;
            draw(m_tr);
            if(trA1 != null)
            {
                if(trB1 != null)
                {
                    trA1.next = trB1;
                    trB1.next = trC1;
                } else
                {
                    trA1.next = trC1;
                }
                return trA1;
            }
            if(trB1 != null)
            {
                trB1.next = trC1;
                return trB1;
            } else
            {
                return trC1;
            }
        }
        Trapezoid tr1 = null;
        Trapezoid tr2 = null;
        Trapezoid tr3 = null;
        Trapezoid trA2 = null;
        Trapezoid trB2 = null;
        Trapezoid trC2 = null;
        if(segment.left != m_tr.left)
        {
            trA2 = new Trapezoid();
            trB2 = new Trapezoid();
            trC2 = new Trapezoid();
            i += 3;
            trA2.top = m_tr.top;
            trA2.bottom = m_tr.bottom;
            trA2.left = m_tr.left;
            trA2.right = segment.left;
            trA2.lLeft = m_tr.lLeft;
            trA2.uLeft = m_tr.uLeft;
            trA2.lRight = trC2;
            trA2.uRight = trB2;
            trA2.node = m_tr.node;
            trA2.name = "A" + nbTrap;
            drawingArea.trap.add(trA2);

            nbTrap++;
            updateLeft(trA2, m_tr);
            trB2.top = m_tr.top;
            trB2.bottom = segment;
            trB2.left = segment.left;
            trB2.lLeft = null;
            trB2.uLeft = trA2;
            trB2.name = "B" + nbTrap;
            drawingArea.trap.add(trB2);

            nbTrap++;
            trC2.top = segment;
            trC2.bottom = m_tr.bottom;
            trC2.left = segment.left;
            trC2.lLeft = trA2;
            trC2.uLeft = null;
            trC2.name = "C" + nbTrap;
            drawingArea.trap.add(trC2);

            nbTrap++;
        } else
        {
            trB2 = new Trapezoid();
            trC2 = new Trapezoid();
            i += 2;
            trB2.top = m_tr.top;
            trB2.bottom = segment;
            trB2.left = m_tr.left;
            trB2.name = "B" + nbTrap;
            drawingArea.trap.add(trB2);

            nbTrap++;
            trB2.uLeft = m_tr.uLeft;
            trB2.lLeft = null;
            trC2.lLeft = m_tr.lLeft;
            trC2.uLeft = null;
            if(m_tr.uLeft != null)
                m_tr.uLeft.uRight = trB2;
            if(m_tr.lLeft != null)
                m_tr.lLeft.lRight = trC2;
            trC2.top = segment;
            trC2.bottom = m_tr.bottom;
            trC2.left = m_tr.left;
            trC2.name = "C" + nbTrap;
            drawingArea.trap.add(trC2);

            nbTrap++;
        }
        trB2.uRight = m_tr.uRight;
        trB2.lRight = null;
        trC2.lRight = m_tr.lRight;
        trC2.uRight = null;
        if(m_tr.uRight != null)
            m_tr.uRight.uLeft = trB2;
        if(m_tr.lRight != null)
            m_tr.lRight.lLeft = trC2;
        trB2.node = m_tr.node;
        trC2.node = m_tr.node;
        m_tr.end = trA2;
        m_tr.up = trB2;
        m_tr.down = trC2;
        draw(m_tr);
        m_tr = m_tr.next;
        tr2 = trB2;
        tr3 = trC2;
        for(; m_tr.next != null; m_tr = m_tr.next)
        {
            Trapezoid trU = new Trapezoid();
            trU.name = "U" + nbTrap;
            drawingArea.trap.add(trU);

            nbTrap++;
            i++;
            if(Computation.isAbove(segment.left, segment.right, m_tr.left))
            {
                tr2.right = m_tr.left;
                trU.top = m_tr.top;
                trU.bottom = segment;
                trU.left = m_tr.left;
                trU.lLeft = tr2;
                trU.uLeft = m_tr.uLeft;
                trU.uRight = m_tr.uRight;
                tr2.lRight = trU;
                tr2 = trU;
                if(m_tr.uLeft != null)
                    m_tr.uLeft.uRight = trU;
                if(m_tr.uRight != null)
                    m_tr.uRight.uLeft = trU;
                Computation.cutLine(m_tr, segment, false);
                tr3.lRight = m_tr.lRight;
                tr3.node = m_tr.node;
            } else
            {
                tr3.right = m_tr.left;
                trU.top = segment;
                trU.bottom = m_tr.bottom;
                trU.left = m_tr.left;
                trU.lLeft = m_tr.lLeft;
                trU.uLeft = tr3;
                trU.lRight = m_tr.lRight;
                tr3.uRight = trU;
                tr3 = trU;
                if(m_tr.lLeft != null)
                    m_tr.lLeft.lRight = trU;
                if(m_tr.lRight != null)
                    m_tr.lRight.lLeft = trU;
                Computation.cutLine(m_tr, segment, true);
                tr2.uRight = m_tr.uRight;
                tr2.node = m_tr.node;
            }
            trU.node = m_tr.node;
            m_tr.up = tr2;
            m_tr.down = tr3;
            draw(m_tr);
        }

        Trapezoid trN = null;
        Trapezoid trL = null;
        Trapezoid trM = null;
        if(Computation.isAbove(segment.left, segment.right, m_tr.left))
        {
            trM = tr3;
            trM.right = segment.right;
            trL = new Trapezoid();
            trL.top = m_tr.top;
            trL.bottom = segment;
            trL.left = m_tr.left;
            trL.right = segment.right;
            trL.lLeft = tr2;
            trL.uLeft = m_tr.uLeft;
            if(m_tr.uLeft != null)
                m_tr.uLeft.uRight = trL;
            trL.name = "L" + nbTrap;
            drawingArea.trap.add(trL);

            nbTrap++;
            tr2.lRight = trL;
            tr2.right = m_tr.left;
            Computation.cutLine(m_tr, segment, false);
            trL.node = m_tr.node;
        } else
        {
            trL = tr2;
            trL.right = segment.right;
            trM = new Trapezoid();
            trM.top = segment;
            trM.bottom = m_tr.bottom;
            trM.left = m_tr.left;
            trM.right = segment.right;
            trM.lLeft = m_tr.lLeft;
            trM.uLeft = tr3;
            if(m_tr.lLeft != null)
                m_tr.lLeft.lRight = trM;
            trM.name = "M" + nbTrap;
            drawingArea.trap.add(trM);

            nbTrap++;
            tr3.uRight = trM;
            tr3.right = m_tr.left;
            Computation.cutLine(m_tr, segment, true);
            trM.node = m_tr.node;
        }
        if(segment.right != m_tr.right)
        {
            trN = new Trapezoid();
            i++;
            trN.top = m_tr.top;
            trN.bottom = m_tr.bottom;
            trN.left = segment.right;
            trN.right = m_tr.right;
            trN.lLeft = trM;
            trN.uLeft = trL;
            trN.lRight = m_tr.lRight;
            trN.uRight = m_tr.uRight;
            trN.name = "N" + nbTrap;
            drawingArea.trap.add(trN);

            nbTrap++;
            trN.node = m_tr.node;
            updateRight(trN, m_tr);
            trL.lRight = null;
            trL.uRight = trN;
            trM.lRight = trN;
            trM.uRight = null;
        } else
        {
            trL.uRight = m_tr.uRight;
            trL.lRight = null;
            trM.lRight = m_tr.lRight;
            trM.uRight = null;
            if(m_tr.uRight != null)
                m_tr.uRight.uLeft = trL;
            if(m_tr.lRight != null)
                m_tr.lRight.lLeft = trM;
        }
        m_tr.up = trL;
        m_tr.down = trM;
        m_tr.end = trN;
        Trapezoid trLeft = null;
        Trapezoid trRight = null;
        if(newTrap.left != segment.left)
            trLeft = newTrap;
        if(m_tr.right != segment.right)
            trRight = m_tr;
        Computation.setVerticalLine(trLeft, trRight, segment);
        draw(m_tr);
        return tr1;
    }

    private void updateD(Trapezoid tr1, Segment lsegment, Trapezoid tr2)
    {
        if(tr1.next == null)
        {
            Node node = tr2.node;
            Trapezoid trTmp = node.t;
            if(trTmp.left != lsegment.left)
            {
                node.node = "x";
                node.p = lsegment.left;
                node.t = null;
                node.left = new Node();
                node.left.node = "t";
                node.left.t = tr2;
                node.left.t.node = node.left;
                node.right = new Node();
                node = node.right;
                tr2 = tr2.next;
            }
            if(trTmp.right != lsegment.right)
            {
                node.node = "x";
                node.p = lsegment.right;
                node.t = null;
                node.right = new Node();
                node.right.node = "t";
                node.right.t = tr2;
                node.right.t.node = node.right;
                tr2 = tr2.next;
                node.left = new Node();
                node.left.node = "y";
                node.left.segment = lsegment;
                node.left.t = null;
                node.left.left = new Node();
                node.left.left.node = "t";
                node.left.left.t = tr2;
                node.left.left.t.node = node.left.left;
                tr2 = tr2.next;
                node.left.right = new Node();
                node.left.right.node = "t";
                node.left.right.t = tr2;
                node.left.right.t.node = node.left.right;
                return;
            } else
            {
                node.node = "y";
                node.segment = lsegment;
                node.t = null;
                node.left = new Node();
                node.left.node = "t";
                node.left.t = tr2;
                node.left.t.node = node.left;
                tr2 = tr2.next;
                node.right = new Node();
                node.right.node = "t";
                node.right.t = tr2;
                node.right.t.node = node.right;
                return;
            }
        }
        Trapezoid trTmp = null;
        Node node = tr1.node;
        if(tr1.left != lsegment.left)
        {
            node.node = "x";
            node.p = lsegment.left;
            node.t = null;
            node.left = new Node();
            node.left.node = "t";
            node.left.t = tr1.end;
            node.left.t.node = node.left;
            node.right = new Node();
            node.right.node = "y";
            node.right.segment = lsegment;
            node.right.left = new Node();
            node.right.left.node = "t";
            node.right.left.t = tr1.up;
            node.right.left.t.node = node.right.left;
            node.right.right = new Node();
            node.right.right.node = "t";
            node.right.right.t = tr1.down;
            node.right.right.t.node = node.right.right;
            tr1.end.next = null;
            trTmp = tr1.end;
            tr1.up.next = trTmp;
            trTmp = tr1.up;
            tr1.down.next = trTmp;
            trTmp = tr1.down;
        } else
        {
            node.node = "y";
            node.segment = lsegment;
            node.t = null;
            node.left = new Node();
            node.left.node = "t";
            node.left.t = tr1.up;
            node.left.t.node = node.left;
            node.right = new Node();
            node.right.node = "t";
            node.right.t = tr1.down;
            node.right.t.node = node.right;
            tr1.up.next = null;
            trTmp = tr1.up;
            tr1.down.next = trTmp;
            trTmp = tr1.down;
        }
        tr1 = tr1.next;
        boolean flag = false;
        boolean flag1 = false;
        while(tr1.next != null) 
        {
            Trapezoid trLoop = trTmp;
            node = tr1.node;
            node.node = "y";
            node.segment = lsegment;
            node.t = null;
            for(; trLoop != null; trLoop = trLoop.next)
            {
                if(trLoop == tr1.up)
                    flag = true;
                if(trLoop == tr1.down)
                    flag1 = true;
            }

            if(flag)
            {
                node.left = tr1.up.node;
            } else
            {
                node.left = new Node();
                node.left.node = "t";
                node.left.t = tr1.up;
                node.left.t.node = node.left;
                tr1.up.next = trTmp;
                trTmp = tr1.up;
            }
            if(flag1)
            {
                node.right = tr1.down.node;
            } else
            {
                node.right = new Node();
                node.right.node = "t";
                node.right.t = tr1.down;
                node.right.t.node = node.right;
                tr1.down.next = trTmp;
                trTmp = tr1.down;
            }
            tr1 = tr1.next;
            flag1 = false;
            flag = false;
        }
        node = tr1.node;
        for(Trapezoid trIter = trTmp; trIter != null; trIter = trIter.next)
        {
            if(trIter == tr1.up)
                flag = true;
            if(trIter == tr1.down)
                flag1 = true;
        }

        if(tr1.right != lsegment.right)
        {
            node.node = "x";
            node.p = lsegment.right;
            node.t = null;
            node.right = new Node();
            node.right.node = "t";
            node.right.t = tr1.end;
            node.right.t.node = node.right;
            node.left = new Node();
            node.left.node = "y";
            node.left.segment = lsegment;
            if(flag1)
            {
                node.left.right = tr1.down.node;
            } else
            {
                node.left.right = new Node();
                node.left.right.node = "t";
                node.left.right.t = tr1.down;
                node.left.right.t.node = node.left.right;
            }
            if(flag)
            {
                node.left.left = tr1.up.node;
                return;
            } else
            {
                node.left.left = new Node();
                node.left.left.node = "t";
                node.left.left.t = tr1.up;
                node.left.left.t.node = node.left.left;
                return;
            }
        }
        node.node = "y";
        node.segment = lsegment;
        node.t = null;
        if(flag1)
        {
            node.right = tr1.down.node;
        } else
        {
            node.right = new Node();
            node.right.node = "t";
            node.right.t = tr1.down;
            node.right.t.node = node.right;
        }
        if(flag)
        {
            node.left = tr1.up.node;
            return;
        } else
        {
            node.left = new Node();
            node.left.node = "t";
            node.left.t = tr1.up;
            node.left.t.node = node.left;
            return;
        }
    }

    private Node findNode(Node node, Point p, Point q)
    {
        Node nodeFound;
    	nodeFound = node;

		while(true)
		{
        	while(nodeFound.node == "x")
        	{
        		if(nodeFound.p != null && nodeFound.isLeft(p))
                {
                    nodeFound = nodeFound.left;
                } else {
                    nodeFound = nodeFound.right;
                }
        	}
            if(nodeFound.node == "y")
            {
                if(Computation.counterClockWise(nodeFound.segment.left, nodeFound.segment.right, p) == -1 || Computation.counterClockWise(nodeFound.segment.left, nodeFound.segment.right, p) == 0 && Computation.counterClockWise(nodeFound.segment.left, nodeFound.segment.right, q) == -1)
                    nodeFound = nodeFound.left;
                else
                    nodeFound = nodeFound.right;
            } else {
                return nodeFound;
            }
        }
    }

    public boolean findPoint(Point point)
    {

        infoArea.addItem("\nLooking for query point : ("+point.x+" , "+point.y+")");        
        long timerStart = System.currentTimeMillis();          		

	    Node node = D;
	    while(true)
	    {
            if(node.node == "x")
            {
                if(node.isLeft(point))
                {
                    node = node.left;

                } else
                {
                    node = node.right;
                }
                continue;
            }
            if(node.node != "y")
                break;
            if(Computation.isAbove(node.segment.left, node.segment.right, point))
            {
                node = node.left;
            } else
            {
                node = node.right;
            }
        }
        
        long timerEnd = System.currentTimeMillis();
		long runningTime = Math.abs(timerEnd - timerStart);
		System.out.println("----->>> Running time to locate query point : "+runningTime+" ms");	
        infoArea.addItem("Query point found in : " + node.t.name);
        if(node.t.uLeft != null)
        {
            infoArea.addItem("     Trap. on upper left: " + node.t.uLeft.name);
        }
        if(node.t.uRight != null)
        {
        	infoArea.addItem("     Trap. on upper right: " + node.t.uRight.name);
        }
        if(node.t.lLeft != null)
        {
        	infoArea.addItem("     Trap. on lower left: " + node.t.lLeft.name);
        }
        if(node.t.lRight != null)
        {
        	infoArea.addItem("     Trap. on lower right: " + node.t.lRight.name);
        }
        drawTRFound(node.t);
        
        return true;
    }

    private Trapezoid followSegment(Segment seg)
    {
        Point p = seg.getStartingPoint();
        Point q = seg.getEndingPoint();

        Node node = findNode(D, p, q);

        Trapezoid trTmp = node.t;
        Trapezoid trResult = trTmp;
        trResult.next = null;

        while(q.isRightOf(trTmp.rightp())) 
        {
            if(Computation.isAbove(seg.left, seg.right, trTmp.rightp()))
                trTmp.next = trTmp.lRight;
            else
                trTmp.next = trTmp.uRight;
            trTmp = trTmp.next;
            if(trTmp == null)
                return trResult;
        }
        trTmp.next = null;
        return trResult;
    }

    private void updateLeft(Trapezoid tr1, Trapezoid tr2)
    {
        if(tr2.uLeft != null)
            tr2.uLeft.uRight = tr1;
        if(tr2.lLeft != null)
            tr2.lLeft.lRight = tr1;
    }

    private void updateRight(Trapezoid tr1, Trapezoid tr2)
    {
        if(tr2.uRight != null)
            tr2.uRight.uLeft = tr1;
        if(tr2.lRight != null)
            tr2.lRight.lLeft = tr1;
    }

    public void clrData()
    {
        nbTrap = 0;
        D = null;
        T = null;
        S = null;
        numSegments = -1;
        maxX = -10;
        maxY = -10;
        minX = 10000;
        minY = 10000;
    }


}
