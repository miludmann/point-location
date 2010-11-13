import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
class TrapezoidPanel extends Panel
    implements ActionListener
{
    Button build;
    Button clear;
    Button query;
    Button find;
    static final String BUILD = "build";
    static final String CLEAR = "clear";
    static final String QUERY = "query";
    static final String FIND = "find";
    String command;
    DrawingArea drawingArea;
    TrapezoidalMap map;
    InfoArea info;
    Component parent;
    
    TrapezoidPanel(DrawingArea data, TrapezoidalMap trmap, InfoArea infobox, Component component)
    {
        parent = component;
        drawingArea = data;
        map = trmap;
        info = infobox;
        build = new Button();
        build.setLabel("Compute Trapezoidal Map");
        build.setActionCommand("computeMap");
        clear = new Button("Clear Screen");
        clear.setActionCommand("clear");
        query = new Button("Input Query Point");
        query.setEnabled(false);
        query.setActionCommand("query");
        find = new Button("Point Location");
        find.setEnabled(false);
        find.setActionCommand("location");
        build.addActionListener(this);
        clear.addActionListener(this);
        query.addActionListener(this);
        find.addActionListener(this);
        setLayout(new GridLayout(1, 4, 1, 1));
        add(build);
        add(clear);
        add(query);
        add(find);
    }

    public void paint(Graphics g)
    {
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public Insets getInsets()
    {
        return new Insets(2, 2, 2, 2);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        command = actionevent.getActionCommand();
        if(command == "computeMap")
            if(drawingArea.numSegments > 0)
            {
                map.clear();
                drawingArea.repaintArea();
                drawingArea.drawTrapezoids();

                long timerStart = System.currentTimeMillis();          		
                map.createMap(drawingArea.segments, drawingArea.numSegments);
        		long timerEnd = System.currentTimeMillis();
        		long runningTime = Math.abs(timerEnd - timerStart);

        		System.out.println("Running time to compute the map : "+runningTime+" ms");
        		
                info.addItem("\nPress 'Input Query Point' and \n     click anywhere in the drawing area.\nThen, press 'Point Location' to highlight\n     the corresponding area.");

                query.setEnabled(true);
                find.setEnabled(true);
                build.setEnabled(false);

                return;
            } else
            {
                info.addItem("\nNo segment in input.");
                return;
            }
        if(command == "clear")
        {
            drawingArea.clear();
            map.clear();
            query.setEnabled(false);
            find.setEnabled(false);
            build.setEnabled(true);
            info.addItem("\n------------------------------------------------------------\nData cleared.\n");
            return;
        }
        if(command == "query")
        {
            drawingArea.quering();
            return;
        }
        if(command == "location")
        {
            long timerStart = System.currentTimeMillis();          		

            map.findPoint(drawingArea.queryPoint);
            
            long timerEnd = System.currentTimeMillis();
    		long runningTime = Math.abs(timerEnd - timerStart);
    		System.out.println("Running time to locate query point : "+runningTime+" ms");
            return;
        }

    }

}
