package py.edu.uca.fcyt.toluca.table;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class JPlayers extends JScrollPane {

	//private JScrollPane scroll;
	private DefaultListModel listModel;	

	private float matiz = 170;
	private float saturacion = 160;
	private float luminosidad = 110;
	private int sel = -1;

	private JList jList = null;
	public JPlayers(){
		super();
		initComponents();
	}

	private void initComponents(){

		listModel = new DefaultListModel();

//		list = new JList(listModel);
     	setViewportBorder(
     		BorderFactory.createLineBorder(Color.black));
     	this.setViewportView(getJList());        
    }


	public void addPlayer(String name){
		listModel.addElement(name);
	}


	public void removePlayer(String  name){
		listModel.removeElement(name);
	}
	
	public String getSelection()
	{
		return (String) getJList().getSelectedValue();
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


	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getJList() {
		if (jList == null) {
			jList = new JList();
			jList.setModel(listModel);
		}
		return jList;
	}
 }