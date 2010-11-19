import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class MainWindow extends Applet
{

    TrapezoidPanel panel;
    DrawingArea drawingArea;
    TrapezoidalMap map;
    InfoArea info;
    StatusBar status;

    public MainWindow()
    {
    }
    
    public void start()
    {
    }

    public void init()
    {
        setLayout(new BorderLayout());
        setSize(1024, 660);
        info = new InfoArea();
        status = new StatusBar();
        drawingArea = new DrawingArea(this, status);
        map = new TrapezoidalMap(drawingArea, info, this);
        panel = new TrapezoidPanel(drawingArea, map, info, this);
        add(drawingArea, "East");
        add(panel, "North");
        add(info, "West");
        add(status, "South");
        add(map);
//        status.displayText("Insert segments with the mouse.Then, press 'Compute Trapezoidal Map'");
        info.addItem("Input non-intersecting segments with the mouse.");
        info.addItem("Then, press 'Compute Trapezoidal Map'.\n\n");
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, dimension.width - 1, dimension.height - 1);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public Insets getInsets()
    {
        return new Insets(5, 5, 5, 5);
    }

    public static void main(String args[])
    {
        Frame frame = new Frame("Point Location - Computational Geometry");
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                System.exit(0);
            }

        }
);
        MainWindow guiwindow = new MainWindow();
        guiwindow.init();
        frame.add("Center", guiwindow);
        frame.pack();
        frame.setVisible(true);
    }


}
