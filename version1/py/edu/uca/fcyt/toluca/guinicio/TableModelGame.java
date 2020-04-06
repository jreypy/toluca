
package py.edu.uca.fcyt.toluca.guinicio;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;





public class TableModelGame extends AbstractTableModel{
	private ArrayList datos;
	private final String [] columnNames={"Mesa","Silla 1","Silla 2","Silla 3","Silla 4","Silla 5","Silla 6"};
    public static final int INDICE_MESA = 0;
	
	public TableModelGame()
	{
		datos=new ArrayList();
		//insertPrueba();
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
	public void deleteTable(int tableNumber)
	{
		int i=0;
		for(i=0;i<datos.size();i++)
		{
			RowGame row=(RowGame) datos.get(i);
			if(row.getTableNumber()==tableNumber)
				break;
		}
		deleteRow(i);
	}
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	public void setPosicionOwner(int tableId,String trucoPlayer,int chair)
	{
		int rowUpdate=0;
		for(int i=0;i<datos.size();i++)
		{
			RowGame row=(RowGame) datos.get(i);
			if(row.getTableNumber()==tableId)
			{
				//System.out.println(getClass().getName()+"Se encontro la tabla se sienta en "+chair);
				row.setJugador(chair,trucoPlayer);
				rowUpdate=i;
			}
		}
		fireTableDataChanged();
	}
	public void refresh()
	{
		fireTableDataChanged();
	}
}
