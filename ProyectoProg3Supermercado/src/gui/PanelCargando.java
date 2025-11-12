package gui;
import java.awt.BorderLayout; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;	

public class PanelCargando extends JPanel{
		private static final long serialVersionUID = 1L;
		Image imagen;
		
		public PanelCargando(Image i) {
			imagen = i;
			setLayout(new BorderLayout());
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D gr = (Graphics2D)g;
			if(imagen == null) {
				super.paintComponent(g);
			}else {
				gr.drawImage(imagen, 0, 0, this.getWidth(), this.getHeight(),this);
			}
		}
		
		public static void main(String[] args) {
			PanelCargando pcf = new PanelCargando(null);
			pcf.setVisible(true);
		}
	} 

	
