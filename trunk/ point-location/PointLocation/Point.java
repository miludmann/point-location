class Point
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

    public boolean liesToRightOf(Point point)
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

    public void addX(int i)
    {
        x = i;
    }

    public void addY(int i)
    {
        y = i;
    }

    public int getIndex()
    {
        return id;
    }

}
