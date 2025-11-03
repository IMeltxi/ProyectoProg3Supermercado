package gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaPrincipal() {
        setTitle("Supermercado OLE");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        
        
        //CREACION PANEL NORTE
        JPanel panelNorte = new JPanel();
        panelNorte.setPreferredSize(new Dimension(this.getWidth(), 75));
      	panelNorte.setLayout(new BorderLayout());
      		
   		//Creacion del panel central
   		JPanel pCentral = new JPanel();
   		pCentral.setLayout(new BorderLayout());  
   		this.add(pCentral, BorderLayout.CENTER);
      		
      	//BUSCADOR Y FILTRADO (PANEL_NORTE_DERECHA)	
   		JPanel panelNorteDerecha = new JPanel();
   		panelNorteDerecha.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
   		panelNorteDerecha.setBackground(new Color(0x013ADF));
      		
      	//BUSCADOR
     	JTextField buscador = new JTextField();
      	buscador.setPreferredSize(new Dimension(300, 40));
      	buscador.setBorder(new LineBorder(Color.BLACK, 2));
      	buscador.setHorizontalAlignment(JTextField.CENTER);
      	panelNorteDerecha.add(buscador);
      		
      	JButton botonBuscar = new JButton("BUSCAR");
      	panelNorteDerecha.add(botonBuscar);
      	botonBuscar.addActionListener(e ->{
      		String textoBuscar = buscador.getText();
      		pCentral.removeAll();
     		//PanelBuscar pBuscar = new PanelBuscar(perfil, textoBuscar);
     		//pCentral.add(pBuscar);
     		pCentral.revalidate();
      	    pCentral.repaint();
      		});

      	//ICONO Mercadeusto (PANEL_NORTE_CENTRO)
    	JPanel panelNorteCentro = new JPanel();
    	panelNorteCentro.setBackground(Color.white);
    	
    		
    	JLabel lblTitulo = new JLabel("MercaDeusto");
    	     lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
    			
    	panelNorteCentro.add(lblTitulo);
    			
    		
    		
   		//BOTONES INICIO, MI LISTA, (PANEL_NORTE_IZQUIERDA)
   		JPanel panelNorteIzquierda = new JPanel();
   		panelNorteIzquierda.setBackground(new Color(0x013ADF));
   		
   		panelNorteIzquierda.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
    		
   		//BOTONES DE LA BARRA NORTE IZQ
   		ArrayList<JButton> menu = new ArrayList<JButton>();
    		
    	ImageIcon iconoInicio = new ImageIcon("fotos_png/Inicio.png");
    	Image imagenEscaladaInicio = iconoInicio.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajustar dimensiones (150x50 como ejemplo)
    	ImageIcon iconoRedimensionado = new ImageIcon(imagenEscaladaInicio);
    		
    	JButton botonInicio = new JButton(iconoRedimensionado);
    	botonInicio.setBackground(Color.WHITE);
    	botonInicio.setBorder(new LineBorder(Color.black, 2));
        panelNorteIzquierda.add(botonInicio);
    	
       botonInicio.addActionListener(e -> {
   	    	pCentral.removeAll();
   	    	//PanelPrincipalContenido panelPrincipal = new PanelPrincipalContenido();
   			//pCentral.add(panelPrincipal);
   			pCentral.revalidate();
   		    pCentral.repaint();
    		   
    		});
    	    
    		
    		
    	JButton botonMiLista = new JButton("Mi Lista");
    	menu.add(botonMiLista);
    		
    	botonMiLista.addActionListener(e -> {
    		pCentral.removeAll();
    		//PanelMiLista pMiLista = new PanelMiLista(perfil);
    		//pCentral.add(pMiLista);
    			pCentral.revalidate();
    		    pCentral.repaint();
    		});
    		
    	    for (JButton boton : menu) {
    	    
    	    	boton.setBackground(Color.BLUE);
    	    	boton.setForeground(Color.WHITE);
    	    	boton.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
    	    	boton.setBorder(new LineBorder(Color.black, 2));
    	        panelNorteIzquierda.add(boton);
    	    }
    		
    		//AÑADIR LOS 3 PANELES AL PANEL PRINCIPAL NORTE
    		panelNorte.add(panelNorteDerecha, BorderLayout.EAST);
    		panelNorte.add(panelNorteCentro, BorderLayout.CENTER);
    		panelNorte.add(panelNorteIzquierda, BorderLayout.WEST);
    			
    		//PANEL SUR (LABEL QUE PONE Mercadeusto)
    		JPanel panelSur = new JPanel();
    		panelSur.setBackground(new Color(0x013ADF));
    		
    		JLabel Mercadeusto = new JLabel("MercaDeusto");
    		Mercadeusto.setBackground(new Color(0x013ADF));
    		Mercadeusto.setForeground(Color.WHITE);
    		Mercadeusto.setFont(new Font("SANS_SERIF", Font.BOLD, 16));
    	      
    		panelSur.add(Mercadeusto, BorderLayout.CENTER);
    		
    		//AÑADIR LOS PANELES PRINCIPALES A LOS BORDERLAYOUTS
    		this.add(panelNorte, BorderLayout.NORTH);	
    		this.add(panelSur, BorderLayout.SOUTH);	
    		
    		PanelPrincipalContenido panelPrincipal = new PanelPrincipalContenido();
    		pCentral.add(panelPrincipal);
    		
    		//menu bar
    		
    		JMenuBar menubar = new JMenuBar();
    		JMenu opciones = new JMenu("Opciones");
    		opciones.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    		menubar.add(opciones);
    		
    		
    		JMenuItem Salir = new JMenuItem("Salir");
    		opciones.addSeparator();
    		opciones.add(Salir);
    		
    		JMenuItem cerrar_sesion = new JMenuItem("Cerrar sesión");
    		opciones.add(cerrar_sesion);
    		
    		JMenuItem CompraAleatoria = new JMenuItem("CompraAleatoria");
    		opciones.add(CompraAleatoria);
    		
    		setJMenuBar(menubar);
    		
    	
    		
    		
    	
    		setLocationRelativeTo(null);
    			
        

        setVisible(true);
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
