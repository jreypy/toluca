package py.edu.uca.fcyt.toluca.table;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

class Watchers extends JScrollPane {

	//private JScrollPane scroll;
	private DefaultListModel listModel;
	private JList list;

	private float matiz = 170;
	private float saturacion = 160;
	private float luminosidad = 110;
	private int sel = -1;

	public Watchers(){
		super();
		initComponents();
	}

	private void initComponents(){

		listModel = new DefaultListModel();

		list = new JList(listModel);
     	setViewportBorder(
     		BorderFactory.createLineBorder(Color.black));
        getViewport().add(list);
    }


	public void addPlayer(String name){
		listModel.addElement(name);
	}


	public void removePlayer(String  name){
		listModel.removeElement(name);
	}
	
	public String getSelection()
	{
		return (String) list.getSelectedValue();
	}


	/*static public void main(String []args){

		Watchers w = new Watchers();
		w.addPlayer("CURIOSO");
		w.addPlayer("Nati");
		w.removePlayer("Nati");
		JFrame jf = new JFrame ();
		jf.getContentPane().add(w);
		jf.pack();
		jf.show();
	}*/


}