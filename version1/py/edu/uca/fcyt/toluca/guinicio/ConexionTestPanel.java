
package py.edu.uca.fcyt.toluca.guinicio;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.logging.Logger;


import javax.swing.JLabel;
import javax.swing.text.NumberFormatter;

import py.edu.uca.fcyt.toluca.TolucaConstants;
/**
 * @author Dani Cricco
 *
 *14-mar-2005
 */
public class ConexionTestPanel extends JPanel{
    
    private Logger logger=Logger.getLogger(ConexionTestPanel.class.getName());
	private JLabel jLabel = null;
	private JLabel labelConexion;
	private int intervalo=5;
	private int cont=0;
	private float test[];
	
	
	private Properties properties;
	private int valExelente;
	private int valBuena;
	private int valAceptable;
	
	
    public ConexionTestPanel()
    {
        super();
 		
       test=new float[intervalo];
       properties=new Properties();
       
       
       try {
        properties.load(ConexionTestPanel.class.getResource("/py/edu/uca/fcyt/toluca/resources/conexion.properties").openStream());
        //System.out.println("Propiedades d conexion");
        
        valExelente=Integer.parseInt((String) properties.get("EXCELENTE"));
        valBuena=Integer.parseInt((String) properties.get("BUENA"));
        valAceptable=Integer.parseInt((String) properties.get("ACEPTABLE"));
        
       //System.out.println("Val d conexion: "+valExelente+" - "+valBuena+" - "+valAceptable);
    } catch (IOException e) {
       logger.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,"IOException "+e.getMessage());
    }
    catch(NumberFormatException e)
    {
        logger.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL,"No se pudo leer las propiedades de conexion -> NumberFormatException: "+e.getMessage());
    }
       
       initialize();
    }
    
    

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        
        this.setLayout(new BorderLayout());
        
        setBorder(BorderFactory.createEtchedBorder());
        this.add(getJLabel(), BorderLayout.EAST);
        this.add(new JLabel("Conexión"),BorderLayout.CENTER);
        
			
	}
	private Color getColorForMs(double ms)
	{
	    if(ms<=valExelente)
	        return Color.GREEN;
	    if(ms>valExelente && ms <=valBuena)
	        return Color.BLUE;
	    if(ms >valBuena && ms <=valAceptable)
	        return Color.YELLOW;
	    return Color.RED;
	        
	}
	
	private String getStringForMs(double ms)
	{
	    if(ms<=valExelente)
	        return "Excelente";
	    if(ms>valExelente && ms <=valBuena)
	        return "Buena";
	    if(ms >valBuena && ms <=valAceptable)
	        return "Aceptable";
	    return "Mala";
	}
	private JLabel getLabelConexion()
	{
	    if(labelConexion==null)
	    {
	        labelConexion=new JLabel("Conexión: ",JLabel.RIGHT);
	        
	    }
	    return labelConexion;
	}
	public void actualizar(float ms)
	{

	    getJLabel().setOpaque(true);
		 
		 for(int i=0;i<test.length;i++)
		     ms+=test[i];
		 
		 ms=ms/intervalo;
		 NumberFormat format=NumberFormat.getNumberInstance();
		 format.setMaximumFractionDigits(2);
		 format.setGroupingUsed(false);
		 getJLabel().setText("  "+format.format(ms)+"  ");
		 String text=getStringForMs(ms)+" - "+ms+"ms.";
		 getJLabel().setToolTipText(text);
		 getJLabel().setBackground(getColorForMs(ms));
		 getLabelConexion().setToolTipText(text);
		 
		 
	    if(cont>=intervalo-1)
	        cont=0;
	    else
	        test[cont++]=ms;
	    
	}
	private JLabel getJLabel()
	{
	    if(jLabel==null)
	    {
	        jLabel = new JLabel();
	        Dimension dim = new Dimension(65,20);
            jLabel.setMaximumSize(dim);
            jLabel.setMinimumSize(dim);
            jLabel.setPreferredSize(dim);
            jLabel.setHorizontalAlignment(JLabel.RIGHT);            
	    }
	    return jLabel;
	}
    public int getIntervalo() {
        return intervalo;
    }
    public void setIntervalo(int intervaloDeAct) {
        this.intervalo = intervaloDeAct;
    }
}
