import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;


/**
 * Crtanje valnog oblika
 *
 * @author Boris Ozegovic
 */
class WaveForm
{
    /* svi oblici koji su jednaki nuli pripadaju grupi nula */
    private final byte ZERO_SHAPES = 1;

    /* svi oblici u jedinici */
	private final byte ONE_SHAPES = 2;

    /* svi oblici koji predstavljaju vektor ili su neinicijalizirani */
	private final byte HEXAGON_SHAPES = 3;

    /* visoka impendancija */
    private final byte HIGH_IMPEDANCE = 4;

    /* nepoznata vrijednost */
    private final byte UNKNOWN = 5;
    private final int WAVE_START_POINT_IN_PIXELS = 0;
    
    private int screenSizeInPixels;

    /* vrijednosti valnih oblika */
	private String[] values;
	private int[] durationsInPixels; 
    private int screenStartPointInPixels;
    private int screenEndPointInPixels;
    private int waveEndPointInPixels;

    /* svi valni oblici */
    private Shape[] shapes = new Shape[] {new Shape1(), new Shape2(), new Shape3(),
                                          new Shape4(), new Shape5(), new Shape6(), 
                                          new Shape7(), new Shape8(), new Shape9(),
                                          new Shape10(), new Shape11(), new Shape12(),
                                          new Shape13() };    


    /**
     * Constructor
     *
     * @param values polje vrijednosti za svaki signal
     * @param durationsInPixels vrijeme trajanja u pikselima
     */
	public WaveForm (String[] values, int[] durationsInPixels)
	{
		this.values = values;
        this.durationsInPixels = durationsInPixels;
	}
	
	
    /**
     * Metoda koji odlucuje koji dio valnih oblika ce se iscrtati u ovisnosti u
     * screenEndPointInPixels
     * 
     * @param g Graphics
     * @param yAxis na koji dio y-osi se crta
     * @param offsetXAxis ovisno o offsetu crta dio ekrana
     * @param durationsInPixels trajanje dobivena iz skale
     * @param waveEndPointInPixels dobivenu iz skale
     */
	public void drawWave (Graphics g, int screenWidth, int yAxis, int offsetXAxis, int[] durationsInPixels,
                          int waveEndPointInPixels) 
	{
        this.durationsInPixels = durationsInPixels;

        /* 
         * postoji sadasnja i sljedeca grupa, jer postoji vise vrsta oblika,
         * npr. jedinica koja slijedi iza nule drugacije je od jednice koja
         * slijedi iza vektora
         */
		byte previousGroup = 0;
		byte presentGroup = 0;
        screenStartPointInPixels = offsetXAxis;
        screenSizeInPixels = screenWidth + 200;
        screenEndPointInPixels = screenStartPointInPixels + screenSizeInPixels;
        int x1 = WAVE_START_POINT_IN_PIXELS;
		int y1 = yAxis;
		int x2;
  
        /* 
         * samo ako nije jednak nuli screenStartPointInPixels je umanjen za
         * jedan zato da ne iscrta na ekranu pocetak signala, vec treba ostaviti
         * dojam da signal traje od prije 
         */
        if (screenStartPointInPixels != WAVE_START_POINT_IN_PIXELS)
        {
            screenStartPointInPixels -= 1 ;
        }

		int i = 0;

        /* za svaku pojedinu vrijednost signala */
		for (String string : values)
        {
			x2 = x1 + durationsInPixels[i]; 
            if (durationsInPixels[i] == 0)
            {
                i++;
                continue;
            }

			if (string.equals("0"))
            {
				presentGroup = ZERO_SHAPES;
			}
			else if (string.equals("1"))
            {
				presentGroup = ONE_SHAPES;
			}
			else if (string.toUpperCase().equals("Z"))
            {
				presentGroup = HIGH_IMPEDANCE;
			}
            else if (string.toUpperCase().equals("X"))
            {
                presentGroup = UNKNOWN;
            }
            else
            {
                presentGroup = HEXAGON_SHAPES;
            }
            
            /* 
             * ako je x2 manji od pocetka na kojem treba iscrtati jednostavno
             * ignorira te signale
             */
            if (x2 > screenStartPointInPixels)
            {
                /* 
                 * ako neki signal ide preko ekrana pocinje njegovo iscrtavanje,
                 * medutim, treba ograniciti jer signalmoze trajati po tisucu
                 * piksela, a  moze biti i i manji od screenEndPointInPixels
                 */
                if (x2 < screenEndPointInPixels)
                {
                    draw(g, string, previousGroup, presentGroup, 
                            screenStartPointInPixels - offsetXAxis, y1, 
                            x2 - offsetXAxis);
                }
                else
                {
                    /* ako je screenEndPointInPixels jednak tocno kraju skale */
                    if (screenEndPointInPixels == waveEndPointInPixels)
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels - offsetXAxis);
                    }

                    /* 
                     * inace crta s jednim prosirenim pikselom koji daje dojam
                     * da signal ne zavrsava, vec se nastavlja
                     */
                    else
                    {
                        draw(g, string, previousGroup, presentGroup, 
                                screenStartPointInPixels - offsetXAxis, 
                                y1, screenEndPointInPixels + 1 - offsetXAxis);
                        break;
                    }
                }
                screenStartPointInPixels = x2;
            }
			previousGroup = presentGroup;
			x1 = x2;
			i++;
		}
	}


    /**
     * Metoda za samo iscrtavanje
     *
     * @param g Graphics
     * @param sring vrijednost signala
     * @param previousGroup prijasnja grupa
     * @param presentGroup sadasnja grupa
     * @param x1 vrijednost od koje pocinje crtanje
     * @param y1 vrijednost po y-osi
     * @param x2 trajanje ove vrijednosti signala u pikselima
     */
    public void draw (Graphics g, String string, int previousGroup, 
                      int presentGroup, int x1, int y1, int x2)
    {
        switch (previousGroup)
        {
            case ZERO_SHAPES : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[0].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[4].draw(g, x1, y1, x2); 
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[8].draw(g, x1, y1, x2); 
                        shapes[8].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[10].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case ONE_SHAPES : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[1].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[3].draw(g, x1, y1, x2); 
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[7].draw(g, x1, y1, x2); 
                        shapes[7].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE : 
                        shapes[11].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case HEXAGON_SHAPES :
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[2].draw(g, x1, y1, x2); 
                        break;
                    case ONE_SHAPES : 
                        shapes[5].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[6].draw(g, x1, y1, x2); 
                        shapes[6].putLabel(g, string, x1, y1, x2); 
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case HIGH_IMPEDANCE :
                switch (presentGroup)
                {
                    case ZERO_SHAPES :
                        shapes[2].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES :
                        shapes[5].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES :
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            case UNKNOWN :
                switch (presentGroup)
                {
                    case ZERO_SHAPES :
                        shapes[0].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES :
                        shapes[3].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES :
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN : 
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
                break;
            default : 
                switch (presentGroup)
                {
                    case ZERO_SHAPES : 
                        shapes[0].draw(g, x1, y1, x2);
                        break;
                    case ONE_SHAPES : 
                        shapes[4].draw(g, x1, y1, x2);
                        break;
                    case HEXAGON_SHAPES : 
                        shapes[6].draw(g, x1, y1, x2);
                        shapes[6].putLabel(g, string, x1, y1, x2);
                        break;
                    case HIGH_IMPEDANCE :
                        shapes[9].draw(g, x1, y1, x2);
                        break;
                    case UNKNOWN :
                        shapes[12].draw(g, x1, y1, x2);
                        break;
                }
        }
    }


    /**
     * Metoda postavlja nove vrijednosti 
     */
    public void setSignalValues (String[] values)
    {
        this.values = values;
    }
}