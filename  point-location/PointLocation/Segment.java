/**
 * @author Depoyant Guillaume & Ludmann Micha�l
 */
public class Segment
{

    public Point left;
    public Point right;
    
    public Segment(Point p1, Point p2)
    {
        if(p1.x < p2.x)
        {
            left = p1;
            right = p2;
            return;
        }
        if(p1.x > p2.x)
        {
            left = p2;
            right = p1;
            return;
        }
        if(p1.y < p2.y)
        {
            left = p1;
            right = p2;
            return;
        } else
        {
            left = p2;
            right = p1;
            return;
        }
    }

    public boolean isAbove(Point point)
    {
        int i = Computation.counterClockWise(left, right, point);
        return i <= 0;
    }

    public Point getStartingPoint()
    {
        return left;
    }

    public Point getEndingPoint()
    {
        return right;
    }

    public void setStartingPoint(Point point)
    {
        right = point;
    }

    public void setEndingPoint(Point point)
    {
        left = point;
    }

}
