package hr.fer.zemris.vhdllab.simulations;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Custom layout manager
 *
 * Manager je namjenski, pretpostavlja sve potrebne komponente i nece raditi ako
 * se izostave neke od komponenata.  Iskljucico je napravljen zbog
 * jednostavnosti raspodjele komponenata po containeru.
 *
 * @author Boris Ozegovic
 */
class WaveLayoutManager implements LayoutManager
{
    private final String TOOLBAR = "toolbar";
    private final String TEXTFIELD = "textField";
    private final String SIGNAL_NAMES = "signalNames";
    private final String SIGNAL_NAMES_SCROLLBAR = "signalNamesScrollbar";
    private final String WAVES = "waves";
    private final String SCALE = "scale";
    private final String VERTICAL_SCROLLBAR = "verticalScrollbar";
    private final String HORIZONTAL_SCROLLBAR = "horizontalScrollbar";

    private Component toolbar;
    private Component textField;
    private Component signalNames;
    private Component signalNamesScrollbar;
    private Component waves;
    private Component scale;
    private Component verticalScrollbar;
    private Component horizontalScrollbar;


    public void addLayoutComponent (String name, Component component) 
    {
        if (TOOLBAR.equals(name)) 
        {
            toolbar = component;
        }
        else if (TEXTFIELD.equals(name)) 
        {
            textField = component;
        }
        else if (SIGNAL_NAMES.equals(name)) 
        {
            signalNames = component;
        }
        else if (SIGNAL_NAMES_SCROLLBAR.equals(name)) 
        {
            signalNamesScrollbar = component;
        }
        else if (WAVES.equals(name)) 
        {
            waves = component;
        }
        else if (SCALE.equals(name)) 
        {
            scale = component;
        }
        else if (VERTICAL_SCROLLBAR.equals(name)) 
        {
            verticalScrollbar = component;
        }
        else if (HORIZONTAL_SCROLLBAR.equals(name)) 
        {
            horizontalScrollbar = component;
        }
        else 
        {
            throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
        }
    }


    public void removeLayoutComponent (Component component) 
    {
        if (component == toolbar) 
        {
            toolbar = null;
        }
        else if (component == textField) 
        {
            textField = null;
        }
        else if (component == signalNames) 
        {
            signalNames = null;
        }
        else if (component == signalNamesScrollbar) 
        {
            signalNamesScrollbar = null;
        }
        else if (component == waves) 
        {
            waves = null;
        }
        else if (component == scale) 
        {
            scale = null;
        }
        else if (component == verticalScrollbar) 
        {
            verticalScrollbar = null;
        }
        else if (component == horizontalScrollbar) 
        {
            horizontalScrollbar = null;
        }
    }


  public Dimension minimumLayoutSize (Container parent) 
  {
    return preferredLayoutSize (parent);
  }


  public Dimension preferredLayoutSize(Container parent) 
  {
        Dimension dim = new Dimension(0, 0);
        int width = 0;
        int height = 0;

        if ((signalNames != null) && signalNames.isVisible()) 
        {
          width = signalNames.getPreferredSize().width;
          height = signalNames.getPreferredSize().height;
        }
        if ((waves != null) && waves.isVisible()) 
        {
          width += waves.getPreferredSize().width;
          height += waves.getPreferredSize().height;
        }
        if ((verticalScrollbar != null) && verticalScrollbar.isVisible())
        {
            width += verticalScrollbar.getPreferredSize().width;
        }
        if ((toolbar != null) && toolbar.isVisible())
        {
            height += toolbar.getPreferredSize().height;
        }
        if ((horizontalScrollbar != null) && horizontalScrollbar.isVisible())
        {
            height += horizontalScrollbar.getPreferredSize().height;
        }
        if ((scale != null) && scale.isVisible())
        {
            height += scale.getPreferredSize().height;
        }
        
        dim.width = width;
        dim.height = height + 10;

        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;

        return dim;
  }


public void layoutContainer(Container target) 
{
    Insets insets = target.getInsets();
    int north = insets.top;
    int south = target.getSize().height - insets.bottom;
    int west = insets.left;
    int east = target.getSize().width - insets.right;

    int width;
    int height;

    /* prilikom resizea ne mijenjaju svoju duljinu */
    int widthToolbar = toolbar.getPreferredSize().width;
    
    /* 
     * ako bi imena signala bila predugacka tada se korisno fiksno 150 piksela
     * duljine i to je maksimalna moguca vrijednost.  Zato maximumSize() 
     */
    int widthSignalNames = signalNames.getMaximumSize().width;
    int widthVerticalScrollbar = verticalScrollbar.getPreferredSize().width;
    int heightToolbar = toolbar.getPreferredSize().height;
    int heightScale = scale.getPreferredSize().height;
    int heightHorizontalScrollbar = horizontalScrollbar.getPreferredSize().height;

    if ((toolbar != null) && toolbar.isVisible())
    {
        width = toolbar.getPreferredSize().width;
        height = toolbar.getPreferredSize().height;
        toolbar.setSize(width, height);
        toolbar.setBounds
            (
             west + 150, 
             north + 10, 
             width, 
             height
             );
    }
    if ((textField != null) && textField.isVisible())
    {
        width = textField.getPreferredSize().width;
        height = textField.getPreferredSize().height;
        textField.setSize(width, height);
        textField.setBounds
            (
             west + widthToolbar + 300,
             north + 10, 
             width,
             height
             );
    }
    if ((signalNames != null) && signalNames.isVisible())
    {
        width = signalNames.getMaximumSize().width;
        height = signalNames.getPreferredSize().height;
        signalNames.setSize(width, height);
        signalNames.setBounds
            (
             west,
             north + 10 + heightToolbar + 10,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10)
             );
    }
    if ((signalNamesScrollbar != null) && signalNamesScrollbar.isVisible())
    {
        width = signalNamesScrollbar.getPreferredSize().width;
        height = signalNamesScrollbar.getPreferredSize().height;
        signalNamesScrollbar.setSize(width, height);
        signalNamesScrollbar.setBounds
            (
             west,
             south - heightHorizontalScrollbar - heightScale,
             widthSignalNames,
             height
             );
    }
    if ((waves != null) && waves.isVisible())
    {
        width = waves.getPreferredSize().width;
        height = waves.getPreferredSize().height;
        waves.setSize(width, height);
        waves.setBounds
            (
             west + widthSignalNames, 
             north + 10 + heightToolbar + 10, 
             east - widthVerticalScrollbar - (west + widthSignalNames),
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10)
             );                  
    }
    if ((scale != null) && scale.isVisible())
    {
        width = scale.getPreferredSize().width;
        height = scale.getPreferredSize().height;
        scale.setSize(width, height);
        scale.setBounds
            (
             west + widthSignalNames,
             south - heightHorizontalScrollbar - heightScale,
             east - widthVerticalScrollbar - (west + widthSignalNames),
             heightScale
             );
    }
    if ((verticalScrollbar != null) && verticalScrollbar.isVisible())
    {
        width = verticalScrollbar.getPreferredSize().width;
        height = verticalScrollbar.getPreferredSize().height;
        verticalScrollbar.setSize(width, height);
        verticalScrollbar.setBounds
            (
             east - width, 
             north + 10 + heightToolbar + 10,
             width, 
             south - (heightHorizontalScrollbar + heightScale) - (north + 10 + heightToolbar + 10)
             );                                
    }
    if ((horizontalScrollbar != null) && horizontalScrollbar.isVisible())
    {
        width = horizontalScrollbar.getPreferredSize().width;
        height = horizontalScrollbar.getPreferredSize().height;
        horizontalScrollbar.setSize(width, height);
        horizontalScrollbar.setBounds
            (
             west + widthSignalNames,
             south - height,
             east - widthVerticalScrollbar - (west + widthSignalNames),
             height
             );
    }
  }
} 
