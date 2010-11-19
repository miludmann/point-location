import java.awt.Component;
/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class Computation extends Component
{

    Computation()
    {
    }

    public static void setVerticalLine(Trapezoid trLeft, Trapezoid trRight, Segment segment)
    {
        if(trLeft != null)
        {
            float f = (float)(trLeft.top.right.x * 100) - (float)(trLeft.top.left.x * 100);
            float f2 = (float)(trLeft.bottom.right.x * 100) - (float)(trLeft.bottom.left.x * 100);
            if(f != 0.0F && f2 != 0.0F)
            {
                float f5 = ((float)(trLeft.top.right.y * 100) - (float)(trLeft.top.left.y * 100)) / f;
                segment.left.up = (int)(f5 * ((float)(segment.left.x * 100) - (float)(trLeft.top.left.x * 100)) + (float)(trLeft.top.left.y * 100));
                segment.left.up /= 100;
                f5 = ((float)(trLeft.bottom.right.y * 100) - (float)(trLeft.bottom.left.y * 100)) / f2;
                segment.left.down = (int)(f5 * ((float)(segment.left.x * 100) - (float)(trLeft.bottom.left.x * 100)) + (float)(trLeft.bottom.left.y * 100));
                segment.left.down /= 100;
            }
        }
        if(trRight != null)
        {
            float f1 = (float)(trRight.top.right.x * 100) - (float)(trRight.top.left.x * 100);
            float f3 = (float)(trRight.bottom.right.x * 100) - (float)(trRight.bottom.left.x * 100);
            if(f1 != 0.0F && f3 != 0.0F)
            {
                float f7 = ((float)(trRight.top.right.y * 100) - (float)(trRight.top.left.y * 100)) / f1;
                segment.right.up = (int)(f7 * ((float)(segment.right.x * 100) - (float)(trRight.top.left.x * 100)) + (float)(trRight.top.left.y * 100));
                segment.right.up /= 100;
                f7 = ((float)(trRight.bottom.right.y * 100) - (float)(trRight.bottom.left.y * 100)) / f3;
                segment.right.down = (int)(f7 * ((float)(segment.right.x * 100) - (float)(trRight.bottom.left.x * 100)) + (float)(trRight.bottom.left.y * 100));
                segment.right.down /= 100;
            }
        }
    }
    
    public static boolean isAbove(Point point, Point vertice1, Point vertice2)
    {
        int i = counterClockWise(point, vertice1, vertice2);
        return i < 0;
    }

    public static TrapezoidPolygon trapezoidToPolygon(Trapezoid trapezoid)
    {
        TrapezoidPolygon trPoly = null;
        if(trapezoid != null)
        {
            trPoly = new TrapezoidPolygon();
            float f = (float)trapezoid.top.right.x - (float)trapezoid.top.left.x;
            float f1 = (float)trapezoid.bottom.right.x - (float)trapezoid.bottom.left.x;
            float f2 = 0.0F;
            if(f != 0.0F && f1 != 0.0F)
            {
                f2 = ((float)trapezoid.top.right.y - (float)trapezoid.top.left.y) / f;
                trPoly.x[1] = trapezoid.left.x;
                trPoly.y[1] = (int)(f2 * ((float)trapezoid.left.x - (float)trapezoid.top.left.x) + (float)trapezoid.top.left.y);
                f2 = ((float)trapezoid.bottom.right.y - (float)trapezoid.bottom.left.y) / f1;
                trPoly.x[0] = trapezoid.left.x;
                trPoly.y[0] = (int)(f2 * ((float)trapezoid.left.x - (float)trapezoid.bottom.left.x) + (float)trapezoid.bottom.left.y);
            }
            f = (float)trapezoid.top.right.x - (float)trapezoid.top.left.x;
            f1 = (float)trapezoid.bottom.right.x - (float)trapezoid.bottom.left.x;
            f2 = 0.0F;
            if(f != 0.0F && f1 != 0.0F)
            {
                float f3 = ((float)trapezoid.top.right.y - (float)trapezoid.top.left.y) / f;
                trPoly.x[2] = trapezoid.right.x;
                trPoly.y[2] = (int)(f3 * ((float)trapezoid.right.x - (float)trapezoid.top.left.x) + (float)trapezoid.top.left.y);
                f3 = ((float)trapezoid.bottom.right.y - (float)trapezoid.bottom.left.y) / f1;
                trPoly.x[3] = trapezoid.right.x;
                trPoly.y[3] = (int)(f3 * ((float)trapezoid.right.x - (float)trapezoid.bottom.left.x) + (float)trapezoid.bottom.left.y);
            }
        }
        return trPoly;
    }

    public static boolean doNotIntersect(Segment seg1, Segment seg2)
    {
        int i = counterClockWise(seg1.getStartingPoint(), seg1.getEndingPoint(), seg2.getStartingPoint());
        int j = counterClockWise(seg1.getStartingPoint(), seg1.getEndingPoint(), seg2.getEndingPoint());
        int k = counterClockWise(seg2.getStartingPoint(), seg2.getEndingPoint(), seg1.getStartingPoint());
        int l = counterClockWise(seg2.getStartingPoint(), seg2.getEndingPoint(), seg1.getEndingPoint());
        return i * j < 0 && k * l < 0 || i * j * k * l == 0;
    }

    public static int counterClockWise(Point p1, Point p2, Point p3)
    {
        int res = 0;
        int i = p2.getX() - p1.getX();
        int k = p2.getY() - p1.getY();
        int j = p3.getX() - p1.getX();
        int l = p3.getY() - p1.getY();
        if(i * l > k * j)
            res = 1;
        if(i * l < k * j)
            res = -1;
        if(i * l == k * j)
            if(i * j < 0 || k * l < 0)
                res = -1;
            else
            if(i * i + k * k >= j * j + l * l)
                res = 0;
            else
                res = 1;
        return res;
    }

    public static void cutLine(Trapezoid trapezoid, Segment segment, boolean flag)
    {
        float f = segment.right.x * 100 - segment.left.x * 100;
        if(f != 0.0F)
        {
            if(flag)
            {
                float f2 = ((float)(segment.right.y * 100) - (float)(segment.left.y * 100)) / f;
                trapezoid.left.up = (int)(f2 * ((float)(trapezoid.left.x * 100) - (float)(segment.left.x * 100)) + (float)(segment.left.y * 100));
                trapezoid.left.up /= 100;
                return;
            }
            float f3 = ((float)(segment.right.y * 100) - (float)(segment.left.y * 100)) / f;
            trapezoid.left.down = (int)(f3 * ((float)(trapezoid.left.x * 100) - (float)(segment.left.x * 100)) + (float)(segment.left.y * 100));
            trapezoid.left.down /= 100;
        }
    }
}
