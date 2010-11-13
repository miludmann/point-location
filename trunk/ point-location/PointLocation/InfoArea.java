import java.awt.*;

@SuppressWarnings("serial")
class InfoArea extends Panel
{

    TextArea field;
    String newline;
    
    InfoArea()
    {
        newline = "\n";
        field = new TextArea(50, 40);
        field.setEditable(false);
        add(field);
        validate();
    }

    public void addItem(String s)
    {
        field.append(newline + s);
    }

    public Insets getInsets()
    {
        return new Insets(2, 2, 2, 2);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, dimension.width - 1, dimension.height - 1);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

}
