package gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Ventanagestion extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public Ventanagestion(JFrame vAnterior) {
		
		setTitle("Administrador");
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setVisible(true);
		
	
		
		
		
		JTabbedPane tabbed= new JTabbedPane();
		
		JPanel usuarios = new JPanel(new BorderLayout());
		JPanel p_s = new JPanel(new BorderLayout());
		//JPanel estadísticas = new JPanel();
		
		tabbed.add(usuarios , "Usuarios");
		tabbed.add(p_s, "Productos");
		//tabbed.add(estadísticas, "Estadísticas");
		
		add(tabbed, BorderLayout.CENTER);
		
		JMenuBar menu = new JMenuBar();
		JMenu fichero = new JMenu("Fichero");
		menu.add(fichero);
		
		
		fichero.addSeparator();
		
		JMenuItem salir = new JMenuItem("Salir");
		fichero.add(salir);
		fichero.addSeparator();
		
		JMenuItem cerrar_sesion = new JMenuItem("Cerrar sesión");
		fichero.add(cerrar_sesion);
		
		add(menu, BorderLayout.NORTH);
		
		salir.addActionListener(e -> {
		    if (vAnterior != null) {
		        vAnterior.setVisible(true); 
		    }
		    dispose();
		});
		cerrar_sesion.addActionListener(e -> {
			new IniciarSesion();
			dispose();
		});

		
       repaint(); 
	}
}
