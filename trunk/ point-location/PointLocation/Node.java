class Node
{

    public String node;
    public Point p;
    public Segment segment;
    public Trapezoid t;
    public Node left;
    public Node right;
    

    Node()
    {
        node = "";
        left = null;
        right = null;
        t = null;
        p = null;
    }
    
    public boolean isLeft(Point point)
    {
        return point.x < p.x;
    }

    public boolean isAbove(Point point)
    {
        int i = Computation.counterClockWise(segment.left, segment.right, point);
        return i <= 0;
    }

    public String toString()
    {
    	return "Node : .node = "+node+"; p = "+p+"; left = "+left+"; right = "+right+"; t = "+t;
    }

}
