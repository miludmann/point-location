/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
public class Point
{

    public int x;
    public int y;
    public int id;
    public int up;
    public int down;

    public Point()
    {
    }

    public Point(int i, int j, int k)
    {
        x = i;
        y = j;
        id = k;
    }

    public boolean isRightOf(Point point)
    {
        return x > point.x;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int i)
    {
        x = i;
    }

    public void setY(int i)
    {
        y = i;
    }

    public int getId()
    {
        return id;
    }

}
