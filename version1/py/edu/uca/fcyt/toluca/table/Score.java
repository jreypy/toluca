package py.edu.uca.fcyt.toluca.table;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

class Score extends JPanel {
	
	protected static final int ABAJO = 0;
	protected static final int ARRIBA = 1;
	protected static final int IZQUIERDA = 0;
	protected static final int DERECHA = 1;
	protected int ptsTeam1 = 30;
	protected int ptsTeam2 = 30;
	private int gamePoints;
	int ballDim = 9;
	
	public void setGamePoints(int totalPts)
	{
	    gamePoints = totalPts;

		Util.verifParam
		(
		        getGamePoints() == 30 || getGamePoints() == 18, 
			"Parámetro 'totalPts' inválido"
		);
		
	}	

	public void paint(Graphics g)
	{
		Graphics2D gr;
		super.paint(g);
		
		gr = (Graphics2D) g;

		//se pintan las lìneas divisorias del puntaje
		drawHorizontalLine(gr, getGamePoints()+1);
		drawVerticalLine(gr);
		
		gr.translate(2, 46);
//		gr.scale(getHeight() / 390.0, getHeight() / 390.0);
		
		gr.setRenderingHint
		(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON
		);
		
		//se pintan los puntajes representados por fósforos
		drawMatch(18,0,gr,ptsTeam1);
		drawMatch(95,0,gr,ptsTeam2);
	}
	//------------------------------------------
	//este metodo es llamado cuando se actualizan los puntajes		
	//el que se encarga de eso tiene que mandarnos el puntaje 
	//actual de cada equipo
	public void actualizarPuntaje(int ptsTeam1,int ptsTeam2)
	{
		this.ptsTeam1 = ptsTeam1;
		this.ptsTeam2 = ptsTeam2;
		
		repaint();
	}
	
	//------------------------------------------
	//dibujar puntajes con fosforito
	private void drawMatch(int x,int y, Graphics g,int pts){
		int cont1 = 0;
		int cont2 = 0;
		y += 2;
		
		//Programacion porcina total
		if(getGamePoints() == 18 && pts>9)
		    pts = pts+1;
		
		if (pts == 0) return;		
		
		label:  //goto
		while(true){
		    	System.out.println("(pts, cont1, cont2): " + pts + "," + cont1 + "," + cont2);
				horizontal(x,y+(53*cont2),g,ARRIBA);
				if (++cont1==pts)
					break label;
				vertical(x-11,(y+6)+(53*cont2),g,IZQUIERDA);
				if (++cont1==pts)
					break label;
				horizontal(x-9,(y+35)+(53*cont2),g,ABAJO);
				if (++cont1==pts)
					break label;
				vertical(x+19,(y+15)+(53*cont2),g,DERECHA);
				if (++cont1==pts)
					break label;
				if((getGamePoints() == 18 && cont1!=9) || getGamePoints() == 30)				
				    diagonal(x-5,(y+28)+(53*cont2),g);	
				else
				    System.out.println("No hay diagonal para (pts, cont1, cont2): " + pts + "," + cont1 + "," + cont2);
				if (++cont1==pts)
					break label;
				cont2++;
		}
	}	
	
	//------------------------------------------
	private void horizontal(int x,int y,Graphics g, int posicion)
	{
		//dibuja el palito en degrade
		for(int i=0;i<5;i++){
			g.setColor(new Color(240-i*20,240-i*20,0));	
			if(posicion == ARRIBA)
				g.drawLine(x,y+i,x+20,y+i);
			else
				g.drawLine(x,y+i,x+20,y+i);
			
		}
		
		//dibuja el borde del palito en negro
		g.setColor(Color.BLACK);
		g.drawRect(x,y,20,5);		
		

		g.setColor(Color.RED);
		if(posicion == ARRIBA){
			g.fillOval(x-9, y-2, ballDim, ballDim);
			//Se coloca el borde de la cabeza del fosforo
			g.setColor(Color.BLACK);
			g.drawOval(x-9, y-2, ballDim, ballDim);
		}
		else{
			g.fillOval(x+27-9, y-2, ballDim, ballDim);
			//Se coloca el borde de la cabeza del fosforo
			g.setColor(Color.BLACK);
			g.drawOval(x+27-9, y-2, ballDim, ballDim);
		}
	}

	//------------------------------------------
	private void vertical(int x,int y,Graphics g, int posicion)
	{
		//dibuja el palito en degrade
		for(int i=0;i<5;i++){
			g.setColor(new Color(240-i*20,240-i*20,0));	
			if(posicion == IZQUIERDA)
				g.drawLine(x+i,y,x+i,y+20);
			else
				g.drawLine(x+i,y,x+i,y+20);
		
		}
		
		//dibuja el borde del palito en negro
		g.setColor(Color.BLACK);
		g.drawRect(x,y,5,20);		
		
		//se pinta en degrade
		/*for(int i=0;i<10;i++){
			g.setColor(new Color(240-i*20,0,0));	
			g.drawLine
			(
				(int)(x -2 - Math.sqrt(25-Math.pow(i-5,2))),
				y-3+i, 
				(int)(x -2 + Math.sqrt(25-Math.pow(i-5,2))),y-3+i
			);
		}*/
		
			
		//Dibuja la punta del fosforo
		g.setColor(Color.RED);
		if(posicion == IZQUIERDA){
			g.fillOval(x-2, y+19, ballDim, ballDim);
			//Se coloca el borde de la cabeza del fosforo
			g.setColor(Color.BLACK);
			g.drawOval(x-2, y+19, ballDim, ballDim);
		}
		else{
			g.fillOval(x-2, y-9, ballDim, ballDim);
			//Se coloca el borde de la cabeza del fosforo
			g.setColor(Color.BLACK);
			g.drawOval(x-2, y-9, ballDim, ballDim);
		}
	}

	//------------------------------------------
	private void diagonal(int x,int y,Graphics g)
	{	    
		    
		//dibuja el palito en degrade
		for(int i=0;i<5;i++){
			if (i==0 || i == 4)
				g.setColor(Color.black);
			else
				g.setColor(new Color(240-i*20,240-i*20,0));	
			
			g.drawLine(x+i/2,y+i,(x+17)+i/2,y-17+i);
			
			
			if (i >0 && i < 4) g.drawLine(1+x+i/2,y+i,1+(x+17)+i/2,y-17+i);
		}

		//Dibuja la punta del fosforo
		g.setColor(Color.RED);
		g.fillOval(x+14, y-20, ballDim, ballDim);

		//Se coloca el borde de la cabeza del fosforo
		g.setColor(Color.BLACK);
		g.drawOval(x+14, y-20, ballDim, ballDim);
	}
	
	//-----------------------------------------
	
	//dibuja las líneas divisorias del puntaje
	private void drawVerticalLine(Graphics g)
	{
		int h, w;

		h = (int) getHeight() - 3;
		w = (int) (getWidth() - 3) / 2;
		
		g.setColor(Color.white);
		g.drawLine(w,40,w,h);
		g.setColor(Color.gray);
		g.drawLine(w + 1,40, w + 1,h);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(w + 2,40, w + 2,h);
	}
	
	private void drawHorizontalLine(Graphics g, int p)
	{
		int w, y;

		w = (int) getWidth() - 3;
		
		
		g.setColor(Color.black);
		//dibuja la linea de separacion del titulo
		g.setColor(Color.white);
		g.drawLine(0,38,w,38);
		g.setColor(Color.gray);
		g.drawLine(0,39, w,39);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0,40, w, 40);
		
		
//		y = getHeight() / 2 + (p - 20) * 5 - 20;
		y = 150 + (p - 20) * 5;
		g.setColor(Color.white);
		g.drawLine(0, y, w, y);
		g.setColor(Color.gray);
		g.drawLine(0, y + 1, w, y + 1);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, y + 2, w, y + 2);
	}

    public int getGamePoints() {
        return gamePoints;
    }
}