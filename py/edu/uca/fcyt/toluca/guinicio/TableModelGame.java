
package py.edu.uca.fcyt.toluca.guinicio;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;




public class TableModelGame extends AbstractTableModel{
	private ArrayList datos;
	private final String [] columnNames={"Mesa","Jug 1","Jug 2","Jug 3","Jug 4","Jug 5","Jug 6","Espec 1","Espect 2"};
    public static final int INDICE_MESA = 0;
	
	public TableModelGame()
	{
		datos=new ArrayList();
		insertPrueba();
	}
	private void insertPrueba()
	{
		RowGame row1=new RowGame(1);
		
		row1.setJugador(0,"Dani");
		row1.setJugador(1,"Cricco");
		
		insertRow(row1);
	}
	public int getRowCount() {

		return datos.size();
	}


	public int getColumnCount() {
		
		return columnNames.length;
	}

	
	public Object getValueAt(int row, int col) {
		
		RowGame data=(RowGame) datos.get(row);
		switch(col)
		{
			case 0: return String.valueOf (data.getTableNumber());
			default: return data.getJugador(col-1);
		}
	}
	public void insertRow(RowGame dato)
	{
		int row=getRowCount();
		datos.add(dato);
		fireTableRowsInserted(row,row);
	}
	public void deleteRow(int row) {

		datos.remove(row);
		fireTableRowsDeleted(row, row);

	}
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
}