package hr.fer.zemris.vhdllab.applets.simulations;



import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Dimension;


/**
 * Predstavlja panel po kojem se pomice kursor 
 *
 * @author Boris Ozegovic
 */
class CursorPanel extends JPanel
{
    /* Aktivan je prvi kursor */
    private final byte FIRST = 1;

    /** Zavrsna tocka panela  */
    private int panelEndPoint;
     
    /** Pocetna tocka prvog kursora u pikselima */
    private int firstCursorStartPoint = 100;

    /** Pocetna tocka drugog kursora u pikselima */
    private int secondCursorStartPoint = 200;

    /** Offset horizntalnog scrollbara */
    private int offset;

    /** Vrijednost prvog kursora u stringu */
    private String firstString;

    /** Vrijednost drugog kursora u stringu */
    private String secondString;

    /** Vrijednost prvog kursora */
    private double firstValue = 100;
    
    /** Vrijednost drugog kursora */
    private double secondValue = 200;

    /** Mjerna jedinica */
    private String measureUnitName;

    /** Aktivan kursor; 0 */
    private byte activeCursor = FIRST;

    /** Boje */
    private ThemeColor themeColor;

    /** SerialVersionUID */ 
    private static final long serialVersionUID = 5;



    /**
     * Constructor
     *
     * @param panelEndPoint duzina panela u pikselima
     * @param offset trenutni pomak
     * @param measureUnitName mjerna jedinica
     */
    public CursorPanel (int panelEndPoint, int offset, String measureUnitName, ThemeColor themeColor)
    {
        this.panelEndPoint = panelEndPoint;
        this.offset = offset;
        this.measureUnitName = measureUnitName;
        this.themeColor = themeColor;
        firstString = "100.0" + this.measureUnitName;
        secondString = "200.0" + this.measureUnitName;
    }


    /**
     * Vraca preferiranu velicinu
     */
    public Dimension getPreferredSize ()
    {
        return new Dimension(panelEndPoint, 30);
    }


    /**
     * Postavlja novu vrijednost pocetka prvog kursora
     *
     * @param firstCursorStartPoint nova vrijednost
     */
    public void setFirstCursorStartPoint (int firstCursorStartPoint)
    {
        this.firstCursorStartPoint = firstCursorStartPoint;
    }


    /**
     * Vraca trenutni polozaj prvog kursora
     */
    public int getFirstCursorStartPoint ()
    {
        return firstCursorStartPoint;
    }


    /**
     * Postavlja novu vrijednost pocetka drugog kursora
     *
     * @param secondCursorStartPoint nova vrijednost
     */
    public void setSecondCursorStartPoint (int secondCursorStartPoint)
    {
        this.secondCursorStartPoint = secondCursorStartPoint;
    }


    /**
     * Vraca trenutni polozaj drugog kursora
     */
    public int getSecondCursorStartPoint ()
    {
        return secondCursorStartPoint;
    }



    /**
     * Postavlja trenutni offset
     *
     * @param offset novi offset
     */
    public void setOffset (int offset)
    {
        this.offset = offset;
    }


    /**
     * Vrijednost prvog kursora u stringu
     *
     * @param firstString vrijednost
     */
    public void setFirstString (String firstString)
    {
        this.firstString = firstString;
    }


    /**
     * Vrijednost drugog kursora u stringu
     *
     * @param secondString vrijednost
     */
    public void setSecondString (String secondString)
    {
        this.secondString = secondString;
    }


    /** 
     * Vrijednost prvog kursora
     *
     * @param firstValue vrijednost
     */
    public void setFirstValue (double firstValue)
    {
        this.firstValue = firstValue;
    }


    /**
     * Vrijednost prvog kursora
     */
    public double getFirstValue ()
    {
        return firstValue;
    }


    /** 
     * Vrijednost drugog kursora
     *
     * @param secondValue vrijednost
     */
    public void setSecondValue (double secondValue)
    {
        this.secondValue = secondValue;
    }


    /**
     * Vrijednost drugog kursora
     */
    public double getSecondValue ()
    {
        return secondValue;
    }


    /**
     * Vraca trenutno aktivni kursor 
     */
    public byte getActiveCursor ()
    {
        return activeCursor;
    }


    /**
     * Postavlja novi aktivni kursor
     *
     * @param activeCursor Novi aktivni kursor
     */
    public void setActiveCursor (byte activeCursor)
    {
        this.activeCursor = activeCursor;
    }
    
    
    public void paintComponent (Graphics g) 
	{
        super.paintComponent(g);
        setBackground(themeColor.getCursorPanel());
        g.setColor(themeColor.getLetters());

        /* crtanje kursora */
        if (firstCursorStartPoint < 0)
        {
            firstCursorStartPoint = 0;
        }
        if (secondCursorStartPoint < 0)
        {
            secondCursorStartPoint = 0;
        }
        g.drawString(firstString, firstCursorStartPoint - offset - (firstString.length() * 6) / 2, 20);
        g.drawString(secondString, secondCursorStartPoint - offset - (secondString.length() * 6) / 2, 10);

        if (activeCursor == FIRST)
        {
            g.setColor(themeColor.getActiveCursor());
            g.fillRect(firstCursorStartPoint - offset - 4, 21, 9, 9);
            g.setColor(themeColor.getPasiveCursor());
            g.fillRect(secondCursorStartPoint - offset - 4, 21, 9, 9);
        }
        else
        {
            g.setColor(themeColor.getPasiveCursor());
            g.fillRect(firstCursorStartPoint - offset - 4, 21, 9, 9);
            g.setColor(themeColor.getActiveCursor());
            g.fillRect(secondCursorStartPoint - offset - 4, 21, 9, 9);
        }
    }
}