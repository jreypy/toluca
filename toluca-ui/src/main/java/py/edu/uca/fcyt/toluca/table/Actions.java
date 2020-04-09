package py.edu.uca.fcyt.toluca.table;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Actions extends JButton implements ChangeListener {
    public static final Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 22);

    //	JSlider jSlider;
    //	JLabel fps;

    //Constructor
    public Actions(JButton[] buttons, ActionListener actListener,
            ChangeListener chListener) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPreferredSize(DEFAULT_BUTTON_SIZE);
            buttons[i].setMaximumSize(DEFAULT_BUTTON_SIZE);
            buttons[i].addActionListener(actListener);
            add(buttons[i]);
        }
        //		add(fps = new JLabel("FPS: 5"));
        /*
         * add(jSlider = new JSlider(1, 100, 5));
         * jSlider.addChangeListener(chListener);
         * jSlider.addChangeListener(this);
         */

        setBorder(null);
    }

    public int getValue() {
        //		return jSlider.getValue();
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent arg0) {
        //		fps.setText("FPS: " + jSlider.getValue());
    }

}