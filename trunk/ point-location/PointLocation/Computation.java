import java.awt.Component;

@SuppressWarnings("serial")
class Computation extends Component
{

    Computation()
    {
    }

    public static void setVerticalLine(Trapezoid trapezoid, Trapezoid trapezoid1, Segment lsegment)
    {
        if(trapezoid != null)
        {
            float f = (float)(trapezoid.top.right.x * 100) - (float)(trapezoid.top.left.x * 100);
            float f2 = (float)(trapezoid.bottom.right.x * 100) - (float)(trapezoid.bottom.left.x * 100);
            if(f != 0.0F && f2 != 0.0F)
            {
                float f5 = ((float)(trapezoid.top.right.y * 100) - (float)(trapezoid.top.left.y * 100)) / f;
                lsegment.left.up = (int)(f5 * ((float)(lsegment.left.x * 100) - (float)(trapezoid.top.left.x * 100)) + (float)(trapezoid.top.left.y * 100));
                lsegment.left.up /= 100;
                f5 = ((float)(trapezoid.bottom.right.y * 100) - (float)(trapezoid.bottom.left.y * 100)) / f2;
                lsegment.left.down = (int)(f5 * ((float)(lsegment.left.x * 100) - (float)(trapezoid.bottom.left.x * 100)) + (float)(trapezoid.bottom.left.y * 100));
                lsegment.left.down /= 100;
            }
        }
        if(trapezoid1 != null)
        {
            float f1 = (float)(trapezoid1.top.right.x * 100) - (float)(trapezoid1.top.left.x * 100);
            float f3 = (float)(trapezoid1.bottom.right.x * 100) - (float)(trapezoid1.bottom.left.x * 100);
            if(f1 != 0.0F && f3 != 0.0F)
            {
                float f7 = ((float)(trapezoid1.top.right.y * 100) - (float)(trapezoid1.top.left.y * 100)) / f1;
                lsegment.right.up = (int)(f7 * ((float)(lsegment.right.x * 100) - (float)(trapezoid1.top.left.x * 100)) + (float)(trapezoid1.top.left.y * 100));
                lsegment.right.up /= 100;
                f7 = ((float)(trapezoid1.bottom.right.y * 100) - (float)(trapezoid1.bottom.left.y * 100)) / f3;
                lsegment.right.down = (int)(f7 * ((float)(lsegment.right.x * 100) - (float)(trapezoid1.bottom.left.x * 100)) + (float)(trapezoid1.bottom.left.y * 100));
                lsegment.right.down /= 100;
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
        TrapezoidPolygon trpolygon = null;
        if(trapezoid != null)
        {
            trpolygon = new TrapezoidPolygon();
            float f = (float)trapezoid.top.right.x - (float)trapezoid.top.left.x;
            float f1 = (float)trapezoid.bottom.right.x - (float)trapezoid.bottom.left.x;
            float f2 = 0.0F;
            if(f != 0.0F && f1 != 0.0F)
            {
                f2 = ((float)trapezoid.top.right.y - (float)trapezoid.top.left.y) / f;
                trpolygon.x[1] = trapezoid.left.x;
                trpolygon.y[1] = (int)(f2 * ((float)trapezoid.left.x - (float)trapezoid.top.left.x) + (float)trapezoid.top.left.y);
                f2 = ((float)trapezoid.bottom.right.y - (float)trapezoid.bottom.left.y) / f1;
                trpolygon.x[0] = trapezoid.left.x;
                trpolygon.y[0] = (int)(f2 * ((float)trapezoid.left.x - (float)trapezoid.bottom.left.x) + (float)trapezoid.bottom.left.y);
            }
            f = (float)trapezoid.top.right.x - (float)trapezoid.top.left.x;
            f1 = (float)trapezoid.bottom.right.x - (float)trapezoid.bottom.left.x;
            f2 = 0.0F;
            if(f != 0.0F && f1 != 0.0F)
            {
                float f3 = ((float)trapezoid.top.right.y - (float)trapezoid.top.left.y) / f;
                trpolygon.x[2] = trapezoid.right.x;
                trpolygon.y[2] = (int)(f3 * ((float)trapezoid.right.x - (float)trapezoid.top.left.x) + (float)trapezoid.top.left.y);
                f3 = ((float)trapezoid.bottom.right.y - (float)trapezoid.bottom.left.y) / f1;
                trpolygon.x[3] = trapezoid.right.x;
                trpolygon.y[3] = (int)(f3 * ((float)trapezoid.right.x - (float)trapezoid.bottom.left.x) + (float)trapezoid.bottom.left.y);
            }
        }
        return trpolygon;
    }

    public static boolean areNotCrossing(Segment lsegment, Segment lsegment1)
    {
        int i = counterClockWise(lsegment.startp(), lsegment.endp(), lsegment1.startp());
        int j = counterClockWise(lsegment.startp(), lsegment.endp(), lsegment1.endp());
        int k = counterClockWise(lsegment1.startp(), lsegment1.endp(), lsegment.startp());
        int l = counterClockWise(lsegment1.startp(), lsegment1.endp(), lsegment.endp());
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

    public static void cutLine(Trapezoid trapezoid, Segment lsegment, boolean flag)
    {
        float f = lsegment.right.x * 100 - lsegment.left.x * 100;
        if(f != 0.0F)
        {
            if(flag)
            {
                float f2 = ((float)(lsegment.right.y * 100) - (float)(lsegment.left.y * 100)) / f;
                trapezoid.left.up = (int)(f2 * ((float)(trapezoid.left.x * 100) - (float)(lsegment.left.x * 100)) + (float)(lsegment.left.y * 100));
                trapezoid.left.up /= 100;
                return;
            }
            float f3 = ((float)(lsegment.right.y * 100) - (float)(lsegment.left.y * 100)) / f;
            trapezoid.left.down = (int)(f3 * ((float)(trapezoid.left.x * 100) - (float)(lsegment.left.x * 100)) + (float)(lsegment.left.y * 100));
            trapezoid.left.down /= 100;
        }
    }
}
