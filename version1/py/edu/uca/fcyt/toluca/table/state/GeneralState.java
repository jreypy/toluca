/*
 * Created on 08/09/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package py.edu.uca.fcyt.toluca.table.state;

/**
 * @author Roberto
 *
 * Representa el estado de una serie de valores
 * <code>double</code>.
 */
public class GeneralState implements State
{
	// pares (objeto, valor)
	private double[] values;
	private String name;
	
	/** Crea una nueva instancia de GeneralTransitioner */
	public GeneralState(String name, double[] values)
	{
		this.values = values;
		this.name = name; 
	}
	
	/** Crea una nueva instancia de GeneralTransitioner */
	public GeneralState(double[] values)
	{
		this(null, values);
	}
	
	/** Crea una copia de <code>state</code> */
	public GeneralState(GeneralState state)
	{
		this.values = state.values;
		this.name = state.name;
	}
	
	/** Devuelve el 'index'-ésimo valor */
	public double getValue(int index)
	{
		return values[index];
	}
	
	/** Devuelve la cantidad de valores cargados */
	public int getValCount()
	{
		return values.length;
	}
	
	public State getCopy()
	{
		return new GeneralState(this);
	}

	public void transition(State state, double ratio)
	{
		int size;
		double[] gsValues;
		
		gsValues = ((GeneralState) state).values;
		
		size = Math.max(values.length, gsValues.length);
		
		for (int i = 0; i < size; i++)
			values[i] = (1 - ratio) * values[i] + 
				gsValues[i] * ratio;
	}
}
