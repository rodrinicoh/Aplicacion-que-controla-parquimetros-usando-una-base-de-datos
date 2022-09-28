package nuevo;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;
import javax.swing.*;

import java.util.Calendar;

@SuppressWarnings("serial")
public class VentanaInspector extends JInternalFrame implements ActionListener{
	
	private static final int Ymax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	private static final int Xmax = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	private int frameWidth, frameHeight,frameWidthListaMultas,frameHeightListaMultas;
	
	private Vector<String>patentes;
	private String legajoInspector;
	public JScrollPane jstAtributos;	
	private Connection con;
	private JButton btnCargadPatentes,multas,desconectar,reiniciar;	
	private int cantidadMultas;
	public JPanel panelInspector;
	public JFrame frameppal;
	private VentanaPrincipal ventanaPPAL;
	private JComboBox<String> ubicacionesDisponibles,parqDisponibles;
	private String CallePPAL,AlturaPPAL,idParqPPAL;
	private DefaultListModel<String> model;
	
	@SuppressWarnings("rawtypes")
	private JList listaMultas;
	private JPanel panelListaMultas;
	private JFrame ventanaListaMultas;
	private String[] autosMultas;
	private JScrollPane barraDesplazamiento;
	
	public VentanaInspector(String inspector, VentanaPrincipal ventana){
		super();
		cantidadMultas=0;
		
		ventanaPPAL=ventana;
		panelInspector=new JPanel();
		panelInspector.setVisible(true);
		panelInspector.setBackground(new Color(194,59,34)); 
		frameppal=new JFrame("Menu del Inspector");
		frameppal.setVisible(true);
		frameWidth = (int) (Xmax*0.7); 
		frameHeight = (int) (Ymax*0.83);
		frameppal.setBounds((int) (Xmax * 0.16), (int) (Ymax * 0.081), frameWidth, frameHeight);
		frameppal.getContentPane().add(panelInspector);
		frameppal.setVisible(false);
		legajoInspector=inspector;
		

		iniciarGUI();
	}
	
	
	private void iniciarGUI() {
		patentes= new Vector<String>();
		
		//Parametros para conectar a la base de datos
		String url = "jdbc:mysql://localhost:3306/parquimetros?serverTimezone=America/Argentina/Buenos_Aires";
		String user = "inspector";
		String pwd = "inspector";
		
		//Establece conexion a la base de datos
		try {
			con = DriverManager.getConnection(url, user, pwd);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		panelInspector.setPreferredSize(new Dimension(1100, 900));
		panelInspector.setVisible(true);
		frameppal.setVisible(true);

		Date fecha= new Date();
		
		frameppal.setTitle("Unidad Personal: "+legajoInspector+" Dia: "+obtenerDia()+" Fecha: "+ convertirDateAString(fecha)+" Hora: "+new SimpleDateFormat("HH:mm:ss").format(fecha));

		frameppal.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		
		//Seleccionar Ubicacion##############################################################################	
		inicializarScrollDeHubicaciones();
		
		//Cargar patentes##############################################################################	
		btnCargadPatentes = new JButton("Insertar patentes a Multar");
		btnCargadPatentes.setBackground(new Color(211, 211, 211));
		btnCargadPatentes.setBounds(606, 41, 236, 67);
		panelInspector.add(btnCargadPatentes);
		btnCargadPatentes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				parqDisponibles.setEnabled(false);
				cargarPatentes();
			}
		});
		btnCargadPatentes.setEnabled(false);

		
		//Generar potenciales multas##############################################################################	
		multas = new JButton("Realizar multas");
		multas.setBackground(new Color(211, 211, 211));
		multas.setBounds(606, 158, 236, 67);
		panelInspector.add(multas);
		multas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				generarMultas(obtenerIdAsoc(CallePPAL, AlturaPPAL,obtenerDia(),obtenerTurno(fecha)),CallePPAL,AlturaPPAL,fecha);
				
			}
		});
		multas.setEnabled(false);
    
		
		//Cerrar sesion##############################################################################		
				desconectar = new JButton("Cerrar sesion");
				desconectar.setBackground(new Color(211, 211, 211));
				desconectar.setBounds(466,533,139,43);
				panelInspector.add(desconectar);
				desconectar.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						frameppal.setEnabled(true);
						frameppal.setVisible(false);
						ventanaPPAL.frameppal.setEnabled(true);
						ventanaPPAL.frameppal.setVisible(true);
						
						removeAll();
						setVisible(false);
						ubicacionesDisponibles.removeAll();
					}
				});
				
		//Reiniciar##############################################################################	
				reiniciar = new JButton("Reiniciar");
				reiniciar.setBackground(new Color(211, 211, 211));
				reiniciar.setBounds(287,533,139,43);
				panelInspector.add(reiniciar);
				
				reiniciar.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						frameppal.setEnabled(true);
						frameppal.setVisible(false);
						removeAll();
						setVisible(false);
						ubicacionesDisponibles.removeAll();
						panelInspector=new JPanel();
						panelInspector.setVisible(true);
						panelInspector.setLayout(new GridLayout(2,2));
						panelInspector.setBackground(new Color(194,59,34)); 
						
						frameppal=new JFrame();
						frameppal.setVisible(true);
						frameppal.setBounds((int) (Xmax * 0.16), (int) (Ymax * 0.081), frameWidth, frameHeight);
						frameppal.getContentPane().add(panelInspector);
						frameppal.setVisible(false);
						iniciarGUI();
						
					}
				});
	}
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==ubicacionesDisponibles) {
        	parqDisponibles.removeAllItems();
        	Vector<String> p =null;
            String seleccionado= (String) ubicacionesDisponibles.getSelectedItem();
            String seleccionados[] = seleccionado.split(" ");
            CallePPAL=seleccionados[0];
            AlturaPPAL=seleccionados[1];
            if(seleccionado!="Seleccionar Ubicacion"){
            	p = obtenerParquimetros(CallePPAL, AlturaPPAL);
            	ubicacionesDisponibles.setEnabled(false);
            	}
            int i=0;
            parqDisponibles.addItem("Selecionar parquimetro");
    		 while(i<p.size())
    			 parqDisponibles.addItem(p.get(i++));
    	 	parqDisponibles.setVisible(true);
        }else
        	if(e.getSource()==parqDisponibles){
        		idParqPPAL=(String) parqDisponibles.getSelectedItem();
        		if(idParqPPAL!="Selecionar parquimetro"){
        			parqDisponibles.setEnabled(false);
        			registrarAcceso(idParqPPAL);
        			btnCargadPatentes.setEnabled(true);
        			//mostrarPatentesEnParquimetro(CallePPAL,AlturaPPAL);
        		}
        	} 
		
	}
	
	/*private void mostrarPatentesEnParquimetro(String CallePPAL, String AlturaPPAL) {
		//Lista de patentes en el parquimetro seleccionado###################################################
		
		try {
			int i = 0;
			String[] patentesUb = new String[100];
			
			//obtener cantidad de patentes en esa ubicacion
			String sql = ("SELECT patente FROM estacionados WHERE (calle='"+CallePPAL+"' && altura='"+AlturaPPAL+"');");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				patentesUb[i] = rs.getString(1);
				i+=1;
			}
			
			listaPatentesUb = new JList(patentesUb);
			listaPatentesUb.setBounds(47, 142, 294, 226);
			
			//Cambio de la orientación de presentación y el ajuste
			listaPatentesUb.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			
			//Se agrega la barra de desplazamiento al panel
			panelInspector.add(listaPatentesUb);
			panelInspector.revalidate();
			panelInspector.repaint();
			
		}
		catch(SQLException e){
			e.printStackTrace();
			}	
		}	*/		
	
	
	private Vector<String> obtenerubicacionesDisponibles(){
		
		Vector<String> dips = new Vector<String>();
		
		try {
			String sql = ("SELECT calle,altura FROM asociado_con WHERE (legajo='"+legajoInspector+"' && turno='"+obtenerTurno(new Date())+"' && dia='"+obtenerDia()+"');");// desde asociado con
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				dips.add(rs.getString(1)+" "+rs.getString(2));
			}
	
		}
		catch(SQLException e){
			e.printStackTrace();
			}		
		
		
		return dips;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void cargarPatentes(){			
		JList lista = new JList();
		lista.setBounds(47, 142, 294, 226);
		
		//Cambio de la orientación de presentación y el ajuste
		lista.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		//Se agrega la barra de desplazamiento al panel
		panelInspector.add(lista);
		
		
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Ingrese la patente del auto");
		JTextField patente = new JTextField();
		//JLabel aMultar = new JLabel("   Patentes ingresadas: ");
		patente.setBounds(120, 70, 80, 90);
		patente.setVisible(true);
		panel.add(label);
		panel.add(patente);	
		//panel.add(aMultar);	
		patente.setColumns(5);
		boolean cerrar=false;
		int estaBD=0,yaEstaCargada=1,esValida=0, yaEsta=0;
		while (!cerrar){
			String[] options = new String[] { "OK", "Terminar" };
			int option = JOptionPane.showOptionDialog(null, panel, "Cargar patente", JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0){
				String patenteACargar= patente.getText();
				if(patente.getText().isEmpty()){
					Object[] opt = { "OK" };
					JOptionPane.showOptionDialog(null,"Campo en blanco", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,opt, opt[0]);
				}else{
					if(!estaEnPatentes(patenteACargar))
						yaEstaCargada=0;
					if(esPatente(patenteACargar))
						esValida=1;
					if(yaEstaCargada==0 && esValida==1){
						patente.setText("");
						patentes.addElement(patenteACargar);
						lista.setListData(patentes);
						lista.revalidate();
						lista.repaint();
						
						multas.setEnabled(true);
						estaBD=0;yaEstaCargada=1;esValida=0; 
					}else{
						patente.setText("");
						Object[] opt = { "OK" };
						String error;
						
						if(esValida==0)
							error="No es una patente valida";
						else {
							System.out.println(patenteACargar);
							for(String s: patentes) {
								System.out.println(s);
								if(patenteACargar.equals(s))
									yaEsta=1;
							}
							if(estaBD==0 && yaEsta==0)
								error="La patente "+patenteACargar+" no se encuentra en nuestra base de datos";
							else
								error="La patente "+patenteACargar+" ya esta cargada";	
						}
						JOptionPane.showOptionDialog(null, error, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,opt, opt[0]);
						estaBD=0;yaEstaCargada=1;esValida=0; yaEsta=0; 
						}
					}
				}else
					cerrar=true;
			
			
		}			
	}
	
	private boolean esPatente(String pat){ 
		
		Pattern p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
		Matcher m = p.matcher(pat);
		if (m.matches())
			return true;
		else
			return false;
	}
	
private boolean estaEnPatentes(String p){ 
		
		int i=0;
		
		while(i<patentes.size())
			if(p.equals(patentes.get(i++)))
				return true;
		
		return false;
	}
	
	
	private String obtenerTurno(Date f){
		String toret=null;
		String hora= new SimpleDateFormat("HH:mm:ss").format(f);
		toret= Character.toString(hora.charAt(0))+Character.toString(hora.charAt(1));
		int hr= Integer.parseInt(toret);
		if (hr>=8 & hr<14)
			return "M";
		else
			if (hr<=20 & hr>=14){
				return "T";
			}
			else{
				return null;
			}	
	}
	
	private void registrarAcceso(String p){
		try {
			Date f= new Date();
			String sql = ("insert into Accede values("+legajoInspector+","+p+","+"'"+convertirDateAString(f)+"','"+new SimpleDateFormat("HH:mm:ss").format(f)+"')");
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			stmt.close();
		}			
		catch(SQLException e){
			e.printStackTrace();
		}		
		
	}
	
	private Vector<String> obtenerParquimetros(String calle, String altura){
		Vector<String> parqs = new Vector<String>();
		try {
			String sql="SELECT id_parq FROM parquimetros WHERE (calle='"+calle+"' && altura="+altura+");";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);	
			while (rs.next())
				parqs.add(rs.getString(1));
		}
		catch(SQLException e){
			e.printStackTrace();
			}	
		return parqs;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void generarMultas(int idAsociado,String c,String a,Date f){
		int iVector=0;
		int i = 0;
		try {
			
			ventanaListaMultas = new JFrame("Listado de autos multados");
			ventanaListaMultas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameWidthListaMultas = (int) (Xmax*0.61); 
			frameHeightListaMultas = (int) (Ymax*0.7);
			ventanaListaMultas.setBounds((int) (Xmax * 0.18), (int) (Ymax * 0.163), frameWidthListaMultas, frameHeightListaMultas);
			ventanaListaMultas.getContentPane().setLayout(new GridLayout());
			
			//Inicialización del panel, que contendra la JList
			panelListaMultas = new JPanel();
			panelListaMultas.setLayout(null);
			
			//creación de los elememtos que componen la lista
			autosMultas = new String[patentes.size()];
			
			
			cantidadMultas=obtenerCantMult(con);
			while(iVector<patentes.size()){
				String sql = ("SELECT * FROM estacionados WHERE (calle='"+c+"' && altura="+a+"&& patente='"+patentes.elementAt(iVector)+"')");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if(!rs.next()) {
					String sql1 = ("insert into Multa values("+(++cantidadMultas)+",'"+convertirDateAString(f)+"','"+new SimpleDateFormat("HH:mm:ss").format(f)+"','"+patentes.elementAt(iVector)+"',"+idAsociado+")");
					Statement stmt1 = con.createStatement();
					stmt1.execute(sql1);
					stmt1.close();
					autosMultas[i] = "Nro de multa: "+cantidadMultas+"   |   Fecha: "+convertirDateAString(f)+"   |   Hora: "+new SimpleDateFormat("HH:mm:ss").format(f)+" |  Calle: "+c+" |  Altura: "+a+" |  Pantente: "+patentes.elementAt(iVector)+"  |  Legajo del inspector: " +legajoInspector ;
				}
				i+=1;
				iVector++;
				
			}
			
			patentes= new Vector<String>();
			btnCargadPatentes.setEnabled(false);
			multas.setEnabled(false);
			
			
			//Inicialización del objeto lista
			if(listaMultas==null) {
				model = new DefaultListModel<>();
				listaMultas = new JList(model);
				listaMultas.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				
			}
			
			
			//Barra de desplazamiento
			if(barraDesplazamiento==null) {
				barraDesplazamiento = new JScrollPane(listaMultas);
				barraDesplazamiento.setBounds(18,30,780,400);
			
				//Se agrega la barra de desplazamiento al panel
				panelListaMultas.add(barraDesplazamiento);
				
				//Se agrega el panel a la ventana
				ventanaListaMultas.getContentPane().add(panelListaMultas);
				ventanaListaMultas.setVisible(true);
				ventanaListaMultas.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				
			}
			
			
			for(int x=0 ; x<autosMultas.length ; x++) {
				model.addElement(autosMultas[x]);
			}
			
			

		}			
		catch(SQLException e){
			System.out.println("SQLException: " + e.getMessage());
	    	System.out.println("SQLState: " + e.getSQLState());
	    	System.out.println("VendorError: " + e.getErrorCode());
	    	JOptionPane.showMessageDialog(null,"Error: Una de las patentes no se encuentra en la base de datos. No es posible labrar multa","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private int obtenerCantMult(Connection c){
		int cant=0;
		try {
			String sql = ("SELECT numero FROM multa");
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
				cant++;
		}			
		catch(SQLException e){
			e.printStackTrace();
		}
		return cant;
	}

	private int obtenerIdAsoc(String c,String a,String d,String t){
		try {
			String sql = ("SELECT id_asociado_con FROM Asociado_con WHERE (calle='"+c+"' && altura='"+a+"' && legajo="+legajoInspector+" && dia='"+d+"' && turno='"+t+"');");// desde asociado con
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				return rs.getInt(1);}
		catch(SQLException e){
			e.printStackTrace();
			}
		
		return 0;
	}
	
	
	
	
	
	private void inicializarScrollDeHubicaciones() {
		 panelInspector.setLayout(null);
		 ubicacionesDisponibles = new JComboBox<String>();
		 ubicacionesDisponibles.setBackground(new Color(211, 211, 211));
		 ubicacionesDisponibles.setBounds(47, 41, 294, 67);
			 panelInspector.add(ubicacionesDisponibles);
			 Vector<String> u = obtenerubicacionesDisponibles();
			 System.out.println(u.toString());
			 ubicacionesDisponibles.setVisible(true);
			 if(u.isEmpty()){
				 JLabel text = new JLabel("No hay ubicaciones disponibles");
				 text.setBounds(119, 179, 200, 40);
				 ubicacionesDisponibles.setVisible(false);
				 panelInspector.add(text);
			 }else{
				 int i=0;
				 ubicacionesDisponibles.addItem("Selecione una ubicacion");
		 		 while(i<u.size())
		 			 ubicacionesDisponibles.addItem(u.get(i++));
		 			 
		 		ubicacionesDisponibles.setSelectedItem(1);
		 		ubicacionesDisponibles.addActionListener(this);
		 		
		 		parqDisponibles = new JComboBox<String>();
		 		parqDisponibles.setBounds(359, 179, 200, 40);
		 		panelInspector.add(parqDisponibles);
		 		parqDisponibles.setVisible(true);
		 		parqDisponibles.setSelectedItem(1);
		 		parqDisponibles.addActionListener(this);
		 		
		 	}
	}

	
	
	
	
	
	/****************************************Operaciones con fechas*******************************************************************/
	
	public static String obtenerDia(){
		Calendar now = Calendar.getInstance();		 
		System.out.println("Fecha actual : " + (now.get(Calendar.MONTH) + 1)
						+ "-"
						+ now.get(Calendar.DATE)
						+ "-"
						+ now.get(Calendar.YEAR)); 
		String[] strDays = new String[]{
						"Domingo",
						"Lunes",
						"Martes",
						"Miercoles",
						"Jueves",
						"Viernes",
						"Sabado"};
		return strDays[now.get(Calendar.DAY_OF_WEEK) - 1].substring(0, 2);
	}



	public static String convertirDateAString(java.util.Date p_fecha) {
		String retorno = null;
		if (p_fecha != null) {
			retorno = (new SimpleDateFormat("yyyy-MM-dd")).format(p_fecha);
		}
		return retorno;
	}
}