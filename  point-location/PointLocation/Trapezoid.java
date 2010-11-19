
/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
public class Trapezoid
{

    public Trapezoid next;
    public Trapezoid up;
    public Trapezoid down;
    public Trapezoid end;
    public Segment top;
    public Segment bottom;
    public Point left;
    public Point right;
    public Node node;
    public Trapezoid lLeft;
    public Trapezoid uLeft;
    public Trapezoid lRight;
    public Trapezoid uRight;
    public String name;
	
    Trapezoid()
    {
        next = null;
        top = null;
        bottom = null;
        left = null;
        right = null;
    }

    Trapezoid(Segment segTop, Segment segBottom, Point leftPoint, Point rightPoint)
    {
        top = segTop;
        bottom = segBottom;
        left = leftPoint;
        right = rightPoint;
    }

    public Segment getTop()
    {
        return top;
    }

    public Segment getBottom()
    {
        return bottom;
    }

    public Point getLeft()
    {
        return left;
    }

    public Point getRight()
    {
        return right;
    }

    public void setTop(Segment lsegment)
    {
        top = lsegment;
    }

    public void setBottom(Segment lsegment)
    {
        bottom = lsegment;
    }

    public void setLeft(Point point)
    {
        left = point;
    }

    public void setRight(Point point)
    {
        right = point;
    }

	public int getMiddleX() {
		return (left.x+right.x)/2;
	}
	public int getMiddleY() {
		return (left.y+right.y)/2;
	}

}
