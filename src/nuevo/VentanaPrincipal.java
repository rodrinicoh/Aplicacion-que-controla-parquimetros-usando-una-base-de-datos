package nuevo;


import java.awt.Color;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Font;



@SuppressWarnings("serial")
public class VentanaPrincipal extends javax.swing.JFrame {

	private static final int Ymax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	private static final int Xmax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	private int frameWidth, frameHeight;
	
	private VentanaAdmin ventanaAdmin;
	private VentanaInspector ventanaInspector;
	public String userLeg;
	public JPanel panelppal;
	public JFrame frameppal;
	private JButton menuAdmin;
	private JButton menuInspector;
	private JButton menuSalir;

	public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal vppal= new VentanaPrincipal();
					vppal.setLocationRelativeTo(null);
					vppal.frameppal.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	public VentanaPrincipal() {
		super();
		iniciarGUI();
		
		frameppal=new JFrame();
		frameppal.setTitle("Menu Principal");
		panelppal = new JPanel();
		panelppal.setPreferredSize(new java.awt.Dimension(1024, 768));
		panelppal.setVisible(true);
		panelppal.setBackground(new Color(250, 250, 210)); 
		frameppal.setVisible(true);
		frameppal.getContentPane().add(panelppal);
		panelppal.setLayout(null);
		panelppal.add(menuAdmin);
		panelppal.add(menuInspector);
		panelppal.add(menuSalir);
		frameppal.setResizable(false);
		frameWidth = (int) (Xmax*0.277); 
		frameHeight = (int) (Ymax*0.424);
		frameppal.setBounds((int) (Xmax * 0.37), (int) (Ymax * 0.275), frameWidth, frameHeight);
		
		ventanaAdmin = new VentanaAdmin(this);
		ventanaAdmin.setLocation(0, -12);
		ventanaAdmin.setVisible(false);
	}

	private void iniciarGUI(){
		try {
			{
				{
					{
						menuAdmin = new JButton();
						menuAdmin.setFont(new Font("Tahoma", Font.PLAIN, 13));
						menuAdmin.setBackground(new Color(211, 211, 211));
						menuAdmin.setBounds(35, 76, 137, 48);
						menuAdmin.setEnabled(true);
						menuAdmin.setText("Administradores");
						menuAdmin.addActionListener(new ActionListener() {
							
							public void actionPerformed(ActionEvent evt) {
								crearAdmin(evt);
							}
						});
						
						
					}

					{
						
						menuInspector = new JButton();
						menuInspector.setFont(new Font("Tahoma", Font.PLAIN, 13));
						menuInspector.setBackground(new Color(211, 211, 211));
						menuInspector.setBounds(193, 76, 144, 48);
						menuInspector.setEnabled(true);
						
						menuInspector.setText("Inspectores");
						menuInspector.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								loguearInspector();
							}
						});
						
					}

					{
						menuSalir = new JButton();
						menuSalir.setFont(new Font("Tahoma", Font.PLAIN, 12));
						menuSalir.setBackground(new Color(211, 211, 211));
						menuSalir.setBounds(118, 186, 138, 48);
						menuSalir.setEnabled(true);
						menuSalir.setText("Salir");
						
						menuSalir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								System.exit(1);
							}
						});
						
					}
				}
			}
			
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	

	
	

	private void crearAdmin(ActionEvent ev) {
		
		try {
			this.ventanaAdmin.setMaximum(true);
			ventanaAdmin = new VentanaAdmin(this);
		} catch (PropertyVetoException e) {
		}

		loguearAdmin();
	}

		
	private void loguearAdmin() {
		frameppal.setEnabled(false);
		frameppal.setVisible(false);
		
		//Creo la ventana para logearse
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Ingrese la contraseña:");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		
		//Agrego los botones
		String[] options = new String[] { "OK", "Cancelar" };
		int option = JOptionPane.showOptionDialog(null, panel, "Login Admin", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		
		
		if (option == 0) //si apreto OK
		{
			char[] password = pass.getPassword();
			String pwd = new String(password);
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				@SuppressWarnings("unused")
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/parquimetros?serverTimezone=America/Argentina/Buenos_Aires", "admin", pwd);
				ventanaAdmin.panelAdmin.setVisible(true);
				ventanaAdmin.frameppal.setVisible(true);
				
			} catch (SQLException | ClassNotFoundException e) {
				mensajeError("La contraseña ingresada es incorrecta");
				frameppal.setEnabled(true);
				frameppal.setVisible(true);
				
			}

		}else {
			frameppal.setEnabled(true);
			frameppal.setVisible(true);
		}
	}

	public void mensajeError(String s) {
		Object[] options = { "OK" };
		JOptionPane.showOptionDialog(null, s, "Error en el ingreso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null,
				options, options[0]);
	}
	
	private void loguearInspector(){
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conect = DriverManager.getConnection("jdbc:mysql://localhost:3306/parquimetros?serverTimezone=America/Argentina/Buenos_Aires", "admin", "admin");
			
			//Creamos la ventana para logearse
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			JLabel u = new JLabel("User");
			JLabel p = new JLabel("Password");
			JTextField usuario = new JTextField();
			JPasswordField pass = new JPasswordField(10);
			panel.add(u);
			panel.add(usuario);
			panel.add(p);
			panel.add(pass);
			
			//Agrego los botones
			String[] options = new String[] { "OK", "Cancelar" };
			int option = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			
			
			if (option == 0){
				userLeg =usuario.getText();
				if (userLeg.length()>0 && existeInspector(userLeg, new String(pass.getPassword()), conect)) {
					iniciarInspector(userLeg);
				} else {
					this.mensajeError("Los datos ingresados no se corresponden con los datos asociados a un inspector");
					loguearInspector();
				}}else {
					
					frameppal.setEnabled(true);
					frameppal.setVisible(true);
				}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void iniciarInspector(String inspector) {
		ventanaInspector = new VentanaInspector(inspector,this);
		ventanaInspector.frameppal.setVisible(true);
		frameppal.setEnabled(false);
		frameppal.setVisible(false);

		try {
			this.ventanaInspector.setMaximum(true);
		} 
		catch (PropertyVetoException e) {}
	}
	
	private boolean existeInspector(String legajo, String pass, Connection conect) {
		String sql = ("SELECT password FROM Inspectores WHERE legajo=" + legajo + " AND password=MD5('" + pass + "');");
		ResultSet rs;
		boolean to_ret=false;
		try {
			rs = conect.createStatement().executeQuery(sql);
			to_ret = rs.next();
		} 
		catch (SQLException e){
			System.out.println("Error de Conexión:");
			System.out.println("SQL Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("SQL Error Code: " + e.getErrorCode());
		}
		return to_ret;
	}
}