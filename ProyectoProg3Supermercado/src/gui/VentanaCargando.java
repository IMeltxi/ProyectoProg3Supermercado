package gui;
	

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;


public class VentanaCargando extends JFrame{

	private static final long serialVersionUID = 1L;
	protected JPanel pCentro, pSur;
	protected JProgressBar bCargando;
	protected JFrame vActual;

	PanelCargando panelFondo;
	
	public VentanaCargando() {
		super();

		vActual = this;
		
		pCentro = new JPanel();
		pSur = new JPanel();
		panelFondo = new PanelCargando(new ImageIcon("fotos_png/ImagenMercaDeusto.png").getImage());
		panelFondo.add(pCentro, BorderLayout.CENTER);
		panelFondo.add(pSur, BorderLayout.SOUTH);
		int espacioEntrePaneles = 200;
		pCentro.setBorder(new EmptyBorder(espacioEntrePaneles,espacioEntrePaneles,espacioEntrePaneles,espacioEntrePaneles));
		
		
		
		
		
		
		bCargando = new JProgressBar(0,100);
		bCargando.setStringPainted(true);
		bCargando.setForeground(new Color(0,0,0));
		
		bCargando.setUI(new BasicProgressBarUI() {
			protected Color getSelectionBackground() {
				return new Color(0, 0, 0);
			}
			
			protected Color getSelectionForeground() {
				return new Color(255,255,255);
			}
		});
		bCargando.setPreferredSize(new Dimension(1000,25));
		bCargando.setSize(300, 300);
		pSur.add(bCargando);
		pCentro.setOpaque(false);
		pSur.setOpaque(false);


        Thread hilo = new Thread(new Runnable() {

        	
			@Override
			public void run() {
				try {
					
					for (int i = 0;i<101; i++) {
						Thread.sleep(35);
						bCargando.setValue(i);
						
					}

				}catch(InterruptedException ie){
					JOptionPane.showMessageDialog(null, "Error al cargar ventana", "Error", JOptionPane.ERROR_MESSAGE);

				}
				dispose();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {			
					}	
					}
				);
				vActual.setVisible(false);
				VentanaPrincipal ventanaPrincipal= new VentanaPrincipal();
				ventanaPrincipal.setVisible(true);
			}
        	
        });hilo.start();
		
		
        int anchoP = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        int altoP = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        setSize(anchoP, altoP);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(50, 50, 1600, 800);
		setContentPane(panelFondo);
		setTitle("CARGANDO");
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		VentanaCargando vc = new VentanaCargando();
		vc.setVisible(true);
	}
	
	
	
	
	
	
	
	

}



