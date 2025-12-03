package gui;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

import domain.Cliente;
import domain.Productos;
import io.LectorCSV;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private VentanaCarrito ventanaCarrito;
    private PanelPrincipalContenido panelProductos;
    private JPanel pCentral;
    private Cliente clienteActual;

    public VentanaPrincipal(Cliente cliente) {
    	this.clienteActual = cliente;
    	
    	List<Productos> listaProductos = LectorCSV.cargarProductos("Resources/productosInfo.txt");

    	System.out.println("Productos cargados: " + listaProductos.size());

        
        setTitle("MercaDeusto");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // 1. INSTANCIAMOS EL CARRITO Y LOS PRODUCTOS UNA SOLA VEZ
        ventanaCarrito = new VentanaCarrito(clienteActual); 
        panelProductos = new PanelPrincipalContenido(ventanaCarrito, listaProductos);
        //panelProductos = new PanelPrincipalContenido(ventanaCarrito); 

        // CREACION PANEL NORTE 
        JPanel panelNorte = new JPanel();
        panelNorte.setPreferredSize(new Dimension(this.getWidth(), 85)); 
      	panelNorte.setLayout(new BorderLayout());
      	this.add(panelNorte, BorderLayout.NORTH);
      		
   		// CREACION PANEL CENTRAL
   		pCentral = new JPanel();
   		pCentral.setLayout(new BorderLayout());  
   		this.add(pCentral, BorderLayout.CENTER);
   		
   		pCentral.add(panelProductos, BorderLayout.CENTER);


    	JPanel panelNorteCentro = new JPanel();
    	panelNorteCentro.setBackground(new Color(0x013ADF));
    	panelNorteCentro.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
    	
    	// 1. El Logo
    	ImageIcon iconoTitulo = new ImageIcon("fotos_png/Logo_MercaDeusto.png");
    	Image imagenEscaladaTitulo = iconoTitulo.getImage().getScaledInstance(150, 40, Image.SCALE_SMOOTH); 
    	ImageIcon iconoRedimensionadoTitulo = new ImageIcon(imagenEscaladaTitulo);
    	JLabel labelLogo = new JLabel(iconoRedimensionadoTitulo);
    	
    	// 2. El Título Grande
    	JLabel labelTituloGrande = new JLabel("MercaDeusto");
    	labelTituloGrande.setFont(new Font("SansSerif", Font.BOLD, 40)); 
    	labelTituloGrande.setForeground(Color.WHITE); 
    	
    	panelNorteCentro.add(labelLogo);
    	panelNorteCentro.add(labelTituloGrande);
    			
    		
   		// BOTONES INICIO, MI LISTA (IZQUIERDA)
   		JPanel panelNorteIzquierda = new JPanel();
   		panelNorteIzquierda.setBackground(new Color(0x013ADF));
   		panelNorteIzquierda.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
    		
    	// Botón Inicio
    	ImageIcon iconoInicio = new ImageIcon("fotos_png/Inicio.png");
    	Image imagenEscaladaInicio = iconoInicio.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
    	ImageIcon iconoRedimensionado = new ImageIcon(imagenEscaladaInicio);
    	
    	JButton botonInicio = new JButton(iconoRedimensionado);
    	botonInicio.setBackground(Color.WHITE);
    	botonInicio.setBorder(new LineBorder(Color.black, 2));
        
        // Lógica botón Inicio: Mostrar panel de productos guardado
        botonInicio.addActionListener(e -> {
            pCentral.removeAll();
            pCentral.add(panelProductos, BorderLayout.CENTER);
            pCentral.revalidate();
            pCentral.repaint();
   		});
        
        panelNorteIzquierda.add(botonInicio);

    	// Botón Mi Lista
    	JButton botonMiLista = new JButton("Mi Lista");
        botonMiLista.setBackground(Color.BLUE);
        botonMiLista.setForeground(Color.WHITE);
        botonMiLista.setFont(new Font("SANS_SERIF", Font.BOLD, 20));
        botonMiLista.setBorder(new LineBorder(Color.black, 2));
        
    	// Lógica botón Mi Lista: Mostrar el carrito guardado
    	botonMiLista.addActionListener(e -> {
    		pCentral.removeAll();
    		pCentral.add(ventanaCarrito, BorderLayout.CENTER);
    		pCentral.revalidate();
    		pCentral.repaint();
    	});
        
        panelNorteIzquierda.add(botonMiLista);
    		
    	panelNorte.add(panelNorteCentro, BorderLayout.CENTER);
    	panelNorte.add(panelNorteIzquierda, BorderLayout.WEST);
    			
    	// PANEL SUR
    	JPanel panelSur = new JPanel();
    	panelSur.setBackground(new Color(0x013ADF));
    	JLabel lblFooter = new JLabel("MercaDeusto");
        lblFooter.setBackground(new Color(0x013ADF));
        lblFooter.setForeground(Color.WHITE);
        lblFooter.setFont(new Font("SANS_SERIF", Font.BOLD, 16));
    	panelSur.add(lblFooter, BorderLayout.CENTER);
    		
    	this.add(panelNorte, BorderLayout.NORTH);	
    	this.add(panelSur, BorderLayout.SOUTH);	
    	
    	// MENU SUPERIOR 
    	JMenuBar menubar = new JMenuBar();
    	JMenu opciones = new JMenu("Opciones");
    	opciones.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    	menubar.add(opciones);
    	
    	JMenuItem Salir = new JMenuItem("Salir");
    	opciones.addSeparator();
    	opciones.add(Salir);
    	
    	JMenuItem cerrar_sesion = new JMenuItem("Cerrar sesión");
    	opciones.add(cerrar_sesion);
    	
    	Salir.addActionListener(e -> System.exit(0));
    	
    	cerrar_sesion.addActionListener(e -> {
    		setVisible(false);
    		new IniciarSesion(); 
    	});
    	
    	setJMenuBar(menubar);
    		
    	setLocationRelativeTo(null);
        setVisible(true);
    }
}