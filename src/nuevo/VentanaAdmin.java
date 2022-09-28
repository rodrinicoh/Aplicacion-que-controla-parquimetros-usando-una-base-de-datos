package nuevo;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import quick.dbtable.*;
import java.awt.Font;
import java.awt.Label;

@SuppressWarnings("serial")
public class VentanaAdmin extends javax.swing.JInternalFrame {
	
	//Para establecer posición de la ventana principal
	private static final int Ymax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	private static final int Xmax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	private int frameWidth, frameHeight, frameWidthTablas, frameHeightTablas;
	
	private VentanaPrincipal ventanaPPAL;
	
	private JScrollPane scrConsulta, jstAtributos;
	private Connection conexionBase;
	
	public JPanel panelAdmin;
	public JPanel patributos;
	
	public JFrame frameppal;  //Esta es la ventana ppal del administrador
	public JFrame fatributos;
	
	
	private JTextArea Consultas;

	private DBTable tabla, tablaEntidades;
	private JTable tablaAtributos;
	
	private JButton BotonBorrarConsulta;
	private JButton botonEjecutarConsulta,botonCerrarSesion;
	
	private Label labelTablas;
	private Label label;
	
	public VentanaAdmin(VentanaPrincipal ventana) {
		
		super();
		
		ventanaPPAL=ventana;
		panelAdmin=new JPanel();
		panelAdmin.setBackground(new Color(204, 255, 204));
		
		frameWidth = (int) (Xmax*0.736); 
		frameHeight = (int) (Ymax*0.878);
		
		frameppal=new JFrame();
		frameppal.setBounds((int) (Xmax * 0.12), (int) (Ymax * 0.04), frameWidth, frameHeight);
		frameppal.getContentPane().add(panelAdmin);
		frameppal.setVisible(false);
		
		iniciarGUI();
	}

	private void iniciarGUI() {
		try {
			panelAdmin.setVisible(true);
			frameppal.setTitle("Menu Administrador");
			tabla = new DBTable();
			frameppal.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			panelAdmin.setLayout(null);
			
			//Cerrar sesión de administrador
			botonCerrarSesion = new JButton("Cerrar sesi\u00F3n");
			botonCerrarSesion.setBackground(new Color(211, 211, 211));
			botonCerrarSesion.setFont(new Font("Tahoma", Font.PLAIN, 12));
			botonCerrarSesion.setBounds(394,593,114,35);
			panelAdmin.add(botonCerrarSesion);
			botonCerrarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frameppal.setEnabled(true);
					frameppal.setVisible(false);
					if(fatributos!=null)
						fatributos.setVisible(false);
					Consultas.setText("SELECT * \n" + "FROM Automoviles;");
					ventanaPPAL.frameppal.setEnabled(true);
					ventanaPPAL.frameppal.setVisible(true);
				}
			});
			
			//Se setea un espacio para realizar consultas
			{
				{	//Se agrega un scroll de la consulta al panel admin
					scrConsulta = new JScrollPane();
					scrConsulta.setBounds(422, 44, 518, 204);
					panelAdmin.add(scrConsulta);
					{
						//Se setea el espacio para realizar consultas
						Consultas = new JTextArea();
						scrConsulta.setViewportView(Consultas);
						Consultas.setTabSize(3);
						Consultas.setColumns(70);
						Consultas.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						Consultas.setText("SELECT * \n" + "FROM Automoviles;");
						Consultas.setFont(new java.awt.Font("Monospaced", 0, 12));
						Consultas.setRows(10);
						}
				}
				{   
					//Se setea boton ejecutar consulta
					botonEjecutarConsulta = new JButton();
					botonEjecutarConsulta.setBackground(new Color(211, 211, 211));
					botonEjecutarConsulta.setFont(new Font("Tahoma", Font.PLAIN, 12));
					botonEjecutarConsulta.setBounds(557, 259, 91, 35);
					panelAdmin.add(botonEjecutarConsulta);
					botonEjecutarConsulta.setText("Ejecutar");
					botonEjecutarConsulta.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							tabla.setVisible(true);
							botonEjecutarConsultaActionPerformed(evt);
						}
					});
				}
				{
					//Se setea boton borrar consulta
					BotonBorrarConsulta = new JButton();
					BotonBorrarConsulta.setBackground(new Color(211, 211, 211));
					BotonBorrarConsulta.setFont(new Font("Tahoma", Font.PLAIN, 12));
					BotonBorrarConsulta.setBounds(673, 258, 90, 36);
					panelAdmin.add(BotonBorrarConsulta);
					BotonBorrarConsulta.setText("Borrar");
					BotonBorrarConsulta.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Consultas.setText("");
							tabla.setVisible(false);
							tabla = new DBTable();
							tabla.setBounds(422, 305, 518, 277);
							panelAdmin.add(tabla);
							tabla.setEditable(false);
						}
					});
					
					
				}
			}
			{
				// creo y agrego la tabla de consulta
				tabla.setBounds(422, 305, 518, 277);
				panelAdmin.add(tabla);
				tabla.setEditable(false);
				
				// Creo la tabla que contendrá como elementos las tablas del problema
				tablaEntidades = new DBTable();
				panelAdmin.add(tablaEntidades);
				tablaEntidades.setBounds(46, 44, 308, 318);
				tablaEntidades.setEditable(false);
				
				labelTablas = new Label("Tablas en la base de datos parquimetros");
				labelTablas.setFont(new Font("Arial", Font.PLAIN, 15));
				labelTablas.setBounds(46, 16, 308, 22);
				panelAdmin.add(labelTablas);
				
				label = new Label("Consultas SQL");
				label.setFont(new Font("Arial", Font.PLAIN, 15));
				label.setBounds(419, 16, 317, 22);
				panelAdmin.add(label);
				tablaEntidades.setVisible(true);
				cargarTabla();
				
				tablaEntidades.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						tablaMouseClicked(evt, tablaEntidades);
					}
				});
			}

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void botonEjecutarConsultaActionPerformed(ActionEvent evt) {
		tabla.setVisible(true);
		refrescarTabla();
	}

	
	private void conectarBD(DBTable tabla) {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String usuario = "admin";
			String clave = "admin";
			String urlConexion = "jdbc:mysql://localhost:3306/parquimetros?serverTimezone=America/Argentina/Buenos_Aires";

			tabla.connectDatabase(driver, urlConexion, usuario, clave);

			conexionBase = DriverManager.getConnection(urlConexion, usuario, clave);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this,
					"Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void desconectarBD(DBTable tabla) {
		try {
			tabla.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private void refrescarTabla(){
		try {
			this.conectarBD(tabla);
			Statement stm = null;
			stm = conexionBase.createStatement();

			if (stm.execute(Consultas.getText())) {
				
				tabla.setSelectSql(Consultas.getText().trim());		
				
				tabla.createColumnModelFromQuery();
				for (int i = 0; i < tabla.getColumnCount(); i++) {

					if (tabla.getColumn(i).getType() == Types.TIME) {
						tabla.getColumn(i).setType(Types.CHAR);
					}

					if (tabla.getColumn(i).getType() == Types.DATE) {
						tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
					}
					
				}
				
				tabla.refresh();				
				
				
			
			} else {
				if (tabla.getRowCount() > 0) 
					tabla.refresh();
				cargarTabla();
				JOptionPane.showMessageDialog(this, "Comando " + Consultas.getText() + " con Exito\n", "Exito",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException ex) {

			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "La consulta SQL no es válida. Reingrese su consulta" + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		}
		this.desconectarBD(tabla);
	}

	private void cargarTabla() {
		conectarBD(tablaEntidades);
		try {
			tablaEntidades.setSelectSql("SHOW tables from parquimetros;");
			tablaEntidades.setRowCountSql("select'12' as count");
			tablaEntidades.refresh();
			
		} catch (SQLException ex) {
			
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);

		}
		desconectarBD(tablaEntidades);
	}

	
	private void tablaMouseClicked(MouseEvent evt, DBTable tabla){
		if ((tabla.getSelectedRow() != -1)){
			this.seleccionarFila(tabla);
		}
	}

	private void seleccionarFila(DBTable tabla) {
		patributos=new JPanel();
		fatributos=new JFrame("Atributos asociados");
		fatributos.setVisible(true);
		fatributos.setSize(500, 500);
		
		frameWidthTablas = (int) (Xmax*0.37); 
		frameHeightTablas = (int) (Ymax*0.63);
		
		fatributos.setBounds((int) (Xmax * 0.31), (int) (Ymax * 0.19), frameWidthTablas, frameHeightTablas);
		
		patributos.setVisible(true);
		patributos.setBackground(new Color(176,242,194));
		fatributos.getContentPane().add(patributos);
		tablaAtributos = new JTable();
		jstAtributos = new JScrollPane(tablaAtributos);
		tablaAtributos.setEnabled(false);
		jstAtributos.setEnabled(false);
		jstAtributos.setBounds(905, 50, 100, 410);
		patributos.add(jstAtributos);
		jstAtributos.setVisible(true);
		
		String str=tabla.getValueAt(tabla.getSelectedRow(), 0).toString();
		Statement stm=null;
		try{
			stm = conexionBase.createStatement();
			ResultSet rs;
			rs = stm.executeQuery("SELECT * FROM " + str);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnas = rsmd.getColumnCount();
			String[] name = new String[columnas];
			String[] colName = { "Atributos" };
			DefaultTableModel model = new DefaultTableModel(null, colName);
			
			for (int i = 0; i < columnas; i++){
				name[i] = rsmd.getColumnName(i + 1);
				model.insertRow(0, new Object[] { name[i] });
			}
			
			tablaAtributos.setModel(model);
			jstAtributos.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
		} 
		catch (SQLException e){
			e.printStackTrace();
		}
	}
}

