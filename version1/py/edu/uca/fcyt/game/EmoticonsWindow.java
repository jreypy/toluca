/* EmoticonsWindow.java
 * Created on June 13, 2003
 *
 * Last modified: $Date: 2005/02/11 21:06:08 $
 * @version $Revision: 1.5 $ 
 * @author Mirna, Mali y Nati
 */
package py.edu.uca.fcyt.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import rath.jmsn.util.Emoticon;

/**
 * 
 * @author afeltes
 *  
 */
public class EmoticonsWindow extends JWindow implements ActionListener {

    private Icon lastIconSelected;

    private ChatPanel chatPanel = null;

    private JPanel jContentPane = null;

    private JButton jBocultar;

    private GridBagLayout gBlayoutManager;

    public static void main(String[] args) {
        JFrame name = new JFrame();
        name.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel jl = new JLabel("texto");
        name.setSize(230, 200);
        jl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Viiiiiiiiiiivo!");
                new EmoticonsWindow().setVisible(true);
                /*
                 * JPopupMenu jp = new JPopupMenu(); jp.add(new
                 * EmoticonsWindow()); jp.show((Component) e.getSource(),
                 * e.getX(), e.getY());
                 */
            }
        });
        name.getContentPane().add(jl);
        name.setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public EmoticonsWindow() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(450, 220);
        addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                setVisible(false);
            }
        });
        getJContentPane().setLayout(new GridLayout(10, 10));
        //getJContentPane().setLayout(getGBlayour());
        setContentPane(getJContentPane());
        initEmoticons();
        getJContentPane().add(getJBocultar());
    }

    /**
     * @return
     */
    private GridBagLayout getGBlayour() {

        if (gBlayoutManager == null)
            gBlayoutManager = new GridBagLayout();
        return gBlayoutManager;
    }

    /**
     * @return
     */
    private JButton getJBocultar() {
        if (jBocultar == null) {
            jBocultar = new JButton("<html><small>sa<br>lir</small>");    
            jBocultar.setPreferredSize(new Dimension(50,20));
            jBocultar.setMinimumSize(new Dimension(50,20));
            jBocultar.setMinimumSize(new Dimension(50,20));
            jBocultar.setToolTipText("Salir de la ventana de emoticones");
            jBocultar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return jBocultar;
    }

    /**
     * @return
     */
    private JPanel getJContentPane() {

        if (jContentPane == null)
            jContentPane = new JPanel();

        return jContentPane;
    }

    /**
     *  
     */
    private void initEmoticons2() {
        int row = 0;
        GridBagConstraints c = new GridBagConstraints();
        Emoticon e = Emoticon.getInstance();
        ArrayList list = new ArrayList();
        Iterator it = e.getEmoticons();
        int i = 0;
        JButton[] botones = new JButton[e.getSize()];
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        while (it.hasNext()) {
            String string = it.next().toString();
            row++;
            if (row % 3 == 0) {
                c.gridx = row % 3;
                c.gridy = row - 1;
                getGBlayour().setConstraints(getJContentPane(), c);
                botones[i] = new JButton(e.get(string));
                getJContentPane().add(botones[i++]);
                c.weightx = 0.0; //reset to the default
                botones[i] = new JButton(e.get(string));
                getGBlayour().setConstraints(getJContentPane(), c);
                getJContentPane().add(botones[i]);
            } else {
                c.weightx = 1.0;
                getGBlayour().setConstraints(getJContentPane(), c);
                botones[i] = new JButton(e.get(string));
                botones[i].addActionListener(this);
                botones[i].setName(string);
                botones[i].setToolTipText(string);
                getJContentPane().add(botones[i]);
            }
            if (row > 6)
                break;
        }
        //setIcon(getLastIconSelected());
    }

    private void initEmoticons() {
        Emoticon e = Emoticon.getInstance();
        ArrayList list = new ArrayList();
        Iterator it = e.getEmoticons();
        int i = 0;
        JButton[] botones = new JButton[e.getSize()];
        while (it.hasNext()) {
            String string = it.next().toString();
            botones[i] = new JButton(e.get(string));
            botones[i].addActionListener(this);
            botones[i].setName(string);
            botones[i].setToolTipText(string);
            getJContentPane().add(botones[i]);
        }
        //setIcon(getLastIconSelected());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton jmi = (JButton) e.getSource();
            setLastIconSelected(jmi.getIcon());
            setIconText(jmi);
        } else
            System.out.println("source: " + e.getSource());

        System.out.println("emoticons action performed");

        setVisible(false);

    }

    /**
     * @param jmi
     */
    private void setIconText(JButton jmi) {
        StringBuffer texto = new StringBuffer(getChatPanel().getJtInput()
                .getText());
        texto.append(" ");
        texto.append(jmi.getName());
        getChatPanel().getJtInput().setText(texto.toString());
        setName(jmi.getName());
    }

    /**
     * @return Returns the lastIconSelected.
     */
    public Icon getLastIconSelected() {
        if (lastIconSelected == null) {
            lastIconSelected = Emoticon.getInstance().get(":)");
            setName(":)");
        }
        return lastIconSelected;
    }

    /**
     * @param lastIconSelected
     *            The lastIconSelected to set.
     */
    public void setLastIconSelected(Icon lastIconSelected) {
        this.lastIconSelected = lastIconSelected;
        getChatPanel().getJBemoticones().setIcon(lastIconSelected);
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

}