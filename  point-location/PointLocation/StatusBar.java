import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.TextField;
import java.text.DecimalFormat;

/**
 * @author Depoyant Guillaume & Ludmann Michaël
 */
@SuppressWarnings("serial")
public class StatusBar extends Panel implements Status
{
	// les zones d'information
	/**
	 * le Label correspondant aux coordonnées de la souris
	 * */
	private TextField coord;
	
	/**
	 * le Label correspondant au message d'information générale
	 * 
	 * */
	private TextField info;
	/** 
	 * Le format des coordonnées dans la barre d'état
	 * sur 3 digits, sans décimales
	 */
	private final static String NUMBERFORMAT = "000";

	/**
     * Constructeur de la barre d'état
     */
    public StatusBar()
    {
	    super();
		this.setLayout(new BorderLayout());
		this.add ( info = new TextField("Status Area"), BorderLayout.WEST );
		this.add ( coord = new TextField(), BorderLayout.EAST);
		displayDefaultCoord();
		info.setEditable(false);
		info.setSize(180, 30);
		coord.setEditable(false);

    }
	
    /**
     * Affichage des coordonnées dans la barre d'état
     * @param float x abscisse
     * @param float x ordonnée
     */
	@Override
	public void displayCoord(float x, float y)
	{
		DecimalFormat coordFormat = new DecimalFormat(NUMBERFORMAT);
		String ys = coordFormat.format(y);
		String xs = coordFormat.format(x);
		coord.setText("x : " + xs + " y : " + ys);
	}

    /**
     * Affichage les coordonnées "vide" hors de la zone de dessin
     */
	@Override
    public void displayDefaultCoord()
    {
		coord.setText("x : ___ y : ___");
    }

    /**
     * 
     */
	@Override
	public void displayText(String message)
	{
		info.setText(message);
	}

	/**
	 */
	private MainWindow mainWindow;

	/**
	 * Getter of the property <tt>mainWindow</tt>
	 * @return  Returns the mainWindow.
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Setter of the property <tt>mainWindow</tt>
	 * @param mainWindow  The mainWindow to set.
	 */
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
    public void paint(Graphics g)
    {
        super.paint(g);
        Dimension dimension = getSize();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, dimension.width - 1, dimension.height - 1);
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
