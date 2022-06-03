package MainMenu;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;


import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTextPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class MainMenu {

	private String password="76225240";
	private String username="postgres";


	private JFrame frame;
	private JTextField AddTitle;
	private JTextField UpdateTitle;
	private JTextField FunctionsTitle;
	private JTable ResultTable;


	private Connection connection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMenu() {
		initialize();
		connectDB();

	}


	public Connection connectDB() {
		Connection connection = null;

		try {

			Class.forName("org.postgresql.Driver");
			connection=DriverManager.getConnection("jdbc:postgresql://localhost:5433/PS1",username,password);


			if(connection!=null) {
				System.out.println("Connected to DB");
			}else {
				System.out.println("Connection unsuccesful, please Try again :(");
			}

		}catch(Exception e) {
				System.out.println("** Database Connection exception:\n"+e);
		}

		return connection;

	}





	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("US Forest Registry GUI");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(388, 16, 253, 35);
		lblNewLabel.setBackground(Color.BLUE);
		frame.getContentPane().add(lblNewLabel);

		Button button = new Button("Add Forest");
		button.setBounds(0, 0, 0, 0);
		button.setFont(new Font("Chalkboard SE", Font.BOLD, 12));
		button.setForeground(Color.ORANGE);
		frame.getContentPane().add(button);

		final JTextPane TableStatusBar = new JTextPane();
		TableStatusBar.setEditable(false);
		TableStatusBar.setText("No Table Selected");
		TableStatusBar.setBounds(172, 63, 606, 49);
		frame.getContentPane().add(TableStatusBar);
		
		

		JButton AddForest = new JButton("Forest");
		AddForest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					System.out.println("Connected to DB");

					  // get user input
					  JPanel panel = new JPanel(new GridLayout(9, 2));
				      JTextField forest_num = new JTextField(10);
				      JTextField name = new JTextField(10);
				      JTextField acid_level = new JTextField(10);
				      JTextField mbr_xmin = new JTextField(10);
				      JTextField mbr_xmax = new JTextField(10);
				      JTextField mbr_ymin = new JTextField(10);
				      JTextField mbr_ymax = new JTextField(10);
				      JTextField area = new JTextField(10);
				      JTextField state = new JTextField(2);
				      panel.add(new JLabel("Forest No :"));
				      panel.add(forest_num);
				      panel.add(new JLabel("Name :"));
				      panel.add(name);
				      panel.add(new JLabel("Area :"));
				      panel.add(area);
				      panel.add(new JLabel("Acid Level :"));
				      panel.add(acid_level);
				      panel.add(new JLabel("mbr xmin :"));
				      panel.add(mbr_xmin);
				      panel.add(new JLabel("mbr xmax :"));
				      panel.add(mbr_xmax);
				      panel.add(new JLabel("mbr ymin :"));
				      panel.add(mbr_ymin);
				      panel.add(new JLabel("mbr ymax :"));
				      panel.add(mbr_ymax);
				      panel.add(new JLabel("State :"));
				      panel.add(state);
				      JOptionPane.showMessageDialog(null, panel);

				      System.out.println("Inputs:\n- "+forest_num.getText() +"\n- "+ name.getText() +"\n- "+
				    	         area.getText()+"\n- "+ acid_level.getText()+"\n- "+
				    	         mbr_xmin.getText()+"\n- "+ mbr_xmax.getText()+"\n- "+
				    	         mbr_ymin.getText()+"\n- "+ mbr_ymax.getText());

				      // execute statement
					try {
						Statement statement = connection.createStatement();

						//ResultSet rs=statement.executeQuery("SELECT datname FROM pg_database;");

						//Statement st = con.createStatement();
						String inputs= forest_num.getText()+",\'"+name.getText()+"\',"+area.getText()+","+acid_level.getText()+","+mbr_xmin.getText()+","+mbr_xmax.getText()+","+mbr_ymin.getText()+","+mbr_ymax.getText();
						inputs=inputs.replace(",,",",NULL,");
						inputs=inputs.replace(",,",",NULL,");
						if(inputs.endsWith(",")){
							inputs+="NULL";
						}
						
						
						
						
						System.out.println(inputs);
						String query ="INSERT INTO forest(\"forest no\",\"name\",\"area\",\"acid level\",\"mbr xmin\",\"mbr xmax\",\"mbr ymin\",\"mbr ymax\") VALUES ("+inputs+");";
						System.out.println("Querry: "+query);
						try {
							//System.out.println("gets here -1");
							try {
								// check if name already exists
								String duplicate_query="SELECT * FROM forest WHERE name=\'"+name.getText()+"\' OR \'forest no\'=\'"+forest_num.getText()+"\';";
								System.out.println("duplicate query: "+duplicate_query);
								ResultSet dq=statement.executeQuery(duplicate_query);

								int size =0;
								while (dq.next()) {
							           size++;
							      }

								System.out.println("Num duplicates: "+size);
								if(query.contains("NULL")) {
									TableStatusBar.setText("Failure Adding Forest! Please Provide all the fields\nDiplayed Table: Forest Table");
									String error_msg="Failure Adding Forest! Some fields are empty. Please Provide all the fields required";
									JOptionPane.showMessageDialog(frame, error_msg,"Missing Fields", JOptionPane.ERROR_MESSAGE);
								}
								else if(size==0) {
									try {
										
										statement.executeQuery(query);
									}catch(Exception nre){
										//
									}
									TableStatusBar.setText("Adding Forest Succesful!\nDiplayed Table: Forest Table");
									String success_msg="Adding Forest \""+name.getText()+" \" Succesful!";
									JOptionPane.showMessageDialog(frame, success_msg, "Forest Addition Accepted",JOptionPane.PLAIN_MESSAGE);
								}else {
									TableStatusBar.setText("Failure Adding Forest! A forest with name: [ \""+name.getText()+"\" ] already exists\nDiplayed Table: Forest Table");
									String error_msg="A forest with name: [ \""+name.getText()+"\" ] already exists";
									JOptionPane.showMessageDialog(frame, error_msg,"Failure Adding Forest", JOptionPane.ERROR_MESSAGE);
								}

							}catch(Exception qae){
								System.out.println("Querry error:\n"+qae);
								TableStatusBar.setText("Unable to add Forest :(\nQuerry error: "+qae);
							}

							//System.out.println("gets here 0");
							// display table
							ResultSet rs = statement.executeQuery("SELECT * FROM forest;") ;
							//System.out.println("gets here 1");


							System.out.println("Updated table");
							ResultTable.setModel(DbUtils.resultSetToTableModel(rs));

							//System.out.println("gets here 3");
							while (rs.next()) {
								System.out.println(rs.getString("name"));
							}
							//System.out.println("gets here 4");
						}catch(Exception qe) {
							System.out.println("Querry Error: \n"+qe);
						}





					}catch(Exception ex) {
						System.out.println(ex);
					}
				}else {
					System.out.println("Please connect to the DB");
				}
			}
		});
		AddForest.setBounds(16, 150, 117, 29);
		frame.getContentPane().add(AddForest);

		JButton AddWorker = new JButton("Worker");
		AddWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					System.out.println("Connected to DB");

					  // get user input
					  JPanel panel = new JPanel(new GridLayout(8, 2));
				      JTextField ssn = new JTextField(10);
				      JTextField name = new JTextField(10);
				      JTextField rank = new JTextField(10);
				      JTextField employing_state = new JTextField(10);
				      panel.add(new JLabel("SSN :"));
				      panel.add(ssn);
				      panel.add(new JLabel("Name :"));
				      panel.add(name);
				      panel.add(new JLabel("rank :"));
				      panel.add(rank);
				      panel.add(new JLabel("Employing State :"));
				      panel.add(employing_state);
				      JOptionPane.showMessageDialog(null, panel);

				      System.out.println("Inputs:\n- "+ssn.getText() +"\n- "+ name.getText() +"\n- "+
				    	         rank.getText()+"\n- "+ employing_state.getText());

					try {
						Statement statement = connection.createStatement();

						//ResultSet rs=statement.executeQuery("SELECT datname FROM pg_database;");
						String worker_states="OH , PA, VA";
						
						
						
						
						//Statement st = con.createStatement();
						String inputs= ssn.getText()+",\'"+name.getText()+"\',"+rank.getText()+",\'"+employing_state.getText()+"\'";
						inputs=inputs.replace(",,",",NULL,");
						inputs=inputs.replace(",,",",NULL,");
						if(inputs.endsWith(",")){
							inputs+="NULL";
						}


						System.out.println(inputs);
						String query ="INSERT INTO worker(ssn, name, rank,employing_state) VALUES ("+inputs+");";
						System.out.println("Querry: "+query);

						try {
							//System.out.println("gets here -1");

							String duplicate_query="SELECT * FROM worker WHERE name=\'"+ssn.getText()+"\';";
							System.out.println("duplicate query: "+duplicate_query);
							ResultSet dq=statement.executeQuery(duplicate_query);

							int size =0;
							while (dq.next()) {
						           size++;
						      }

							System.out.println("Num duplicates: "+size);

							if(query.contains("NULL")) {
								TableStatusBar.setText("Failure Adding Worker! Please Provide all the fields\nDiplayed Table: Forest Table");
								String error_msg="Failure Adding Worker! Some fields are empty. Please Provide all the fields required";
								JOptionPane.showMessageDialog(frame, error_msg,"Missing Fields", JOptionPane.ERROR_MESSAGE);
							}else if(!worker_states.contains(employing_state.getText())) {
								TableStatusBar.setText("Failure Adding Worker! There are no forests hiring there, try again later\nDiplayed Table: Forest Table");
								String error_msg="Failure Adding Worker! No workers allowed in that state";
								JOptionPane.showMessageDialog(frame, error_msg,"Restricted State", JOptionPane.ERROR_MESSAGE);
							}
							else if(size==0) {
								try {
									statement.executeQuery(query);
								}catch(Exception nre){
									//
									//System.out.println("(System Error) Insert Querry Failed: \n"+nre);
								}
								TableStatusBar.setText("Adding Worker Succesful!\nDiplayed Table: Worker");
								String success_msg="Adding Worker \""+ssn.getText()+" \" Succesful!";
								JOptionPane.showMessageDialog(frame, success_msg, "Worker Addition Accepted",JOptionPane.PLAIN_MESSAGE);
							}else {
								TableStatusBar.setText("Failure Adding Worker! SSIN: [ \""+ssn.getText()+"\" ] already exists\nDiplayed Table: Worker Table");
								String error_msg="A worker with SSIN: [ \""+ssn.getText()+"\" ] already exists";
								JOptionPane.showMessageDialog(frame, error_msg,"Failure Adding Worker", JOptionPane.ERROR_MESSAGE);
							}
						}catch(Exception qe) {
							System.out.println("Querry Error: \n"+qe);
							TableStatusBar.setText("Unable to add Worker :(\nQuerry error: "+qe);
						}

						ResultSet rs = statement.executeQuery("SELECT * FROM worker") ;
						System.out.println("Updated table");
						ResultTable.setModel(DbUtils.resultSetToTableModel(rs));
						while (rs.next()) {
							System.out.println(rs.getString("name"));
						}

					}catch(Exception ex) {
						System.out.println(ex);
					}
				}else {
					System.out.println("Please connect to the DB");
				}
			}
		});
		AddWorker.setBounds(16, 191, 117, 29);
		frame.getContentPane().add(AddWorker);

		JButton AddSensor = new JButton("Sensor");
		AddSensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					System.out.println("Connected to DB");

					// get user input
					  JPanel panel = new JPanel(new GridLayout(8, 2));
				      JTextField sensorID = new JTextField(10);
				      JTextField x = new JTextField(10);
				      JTextField y = new JTextField(10);
				      JTextField last_charged = new JTextField(10);
				      JTextField maintainer = new JTextField(10);
				      JTextField last_read = new JTextField(10);
				      JTextField energy = new JTextField(10);
				      panel.add(new JLabel("Sensor ID :"));
				      panel.add(sensorID);
				      panel.add(new JLabel("X :"));
				      panel.add(x);
				      panel.add(new JLabel("Y:"));
				      panel.add(y);
				      panel.add(new JLabel("Last Charged :"));
				      panel.add(last_charged);
				      panel.add(new JLabel("Maintainer :"));
				      panel.add(maintainer);
				      panel.add(new JLabel("Last Read :"));
				      panel.add(last_read);
				      panel.add(new JLabel("Energy :"));
				      panel.add(energy);
				      JOptionPane.showMessageDialog(null, panel);

				      System.out.println("Inputs:\n- "+sensorID.getText() +"\n- "+ x.getText() +"\n- "+
				    	         y.getText()+"\n- "+ last_charged.getText()+"\n- "+
				    	         maintainer.getText()+"\n- "+ last_read.getText()+"\n- "+
				    	         energy.getText());


					try {
						Statement statement = connection.createStatement();

						String inputs= sensorID.getText()+",\'"+x.getText()+"\',"+y.getText()+",'"+last_charged.getText()+"',"+maintainer.getText()+",'"+last_read.getText()+"',"+energy.getText();
						inputs=inputs.replace(",,",",NULL,");
						inputs=inputs.replace(",,",",NULL,");
						if(inputs.endsWith(",")){
							inputs+="NULL";
						}

						System.out.println(inputs);
						String query ="INSERT INTO sensor(\"sensor id\", x, y, \"last charged\",maintainer,\"last read\",energy) VALUES ("+inputs+");";
						System.out.println("Querry: "+query);
						try {
							//System.out.println("gets here -1");
							String duplicate_query="SELECT * FROM sensor WHERE \"sensor id\"=\'"+sensorID.getText()+"\';";
							System.out.println("duplicate query: "+duplicate_query);
							

							int sim_sensor_ID =0;
							try {
								ResultSet dq=statement.executeQuery(duplicate_query);
								while (dq.next()) {
									sim_sensor_ID++;
								}
							}catch(Exception ex) {
								
							}
							System.out.println("Num duplicate sensor ID : "+sim_sensor_ID);

							duplicate_query="SELECT * FROM sensor WHERE x="+x.getText()+" AND y="+y.getText()+";";
							System.out.println("duplicate query: "+duplicate_query);
							

							int sim_sensor_coord =0;
							try {
								ResultSet dq=statement.executeQuery(duplicate_query);
								while (dq.next()) {
									sim_sensor_coord++;
								}
							}catch(Exception ex) {
								
							}

							System.out.println("Num duplicate sensor Coordinates : "+sim_sensor_coord);


							if(query.contains("NULL") || x.getText().equals("") || y.getText().equals("") || sensorID.getText().equals("")) {
								TableStatusBar.setText("Failure Adding Sensor! Please Provide all the fields\nDiplayed Table: Sensor Table");
								String error_msg="Failure Adding Sensor! Some fields are empty. Please Provide all the fields required";
								JOptionPane.showMessageDialog(frame, error_msg,"Missing Fields", JOptionPane.ERROR_MESSAGE);
							}
							else if(sim_sensor_coord==0 && sim_sensor_ID==0) {
								try {
									statement.executeQuery(query) ;
								}catch(Exception qae){
									System.out.println("Querry error:\n"+qae);
								}
								TableStatusBar.setText("Adding Sensor Succesful!\nDiplayed Table: Sensor");
								String success_msg="Adding Sensor \""+sensorID.getText()+" \" Succesful!";
								JOptionPane.showMessageDialog(frame, success_msg, "Sensor Addition Accepted",JOptionPane.PLAIN_MESSAGE);
							}else {
								TableStatusBar.setText("Failure Adding Sensor\nDiplayed Table: Sensor");
								String success_msg="A sensor with ID: \""+sensorID.getText()+"\" or coordinates ("+x.getText()+", "+y.getText()+") already exists ";
								JOptionPane.showMessageDialog(frame, success_msg, "Failure Adding Sensor",JOptionPane.PLAIN_MESSAGE);
							}




							//System.out.println("gets here 0");
							// display table
							ResultSet rs = statement.executeQuery("SELECT * FROM sensor;") ;
							//System.out.println("gets here 1");
							

							System.out.println("Updated table");
							ResultTable.setModel(DbUtils.resultSetToTableModel(rs));

							//System.out.println("gets here 3");
							while (rs.next()) {
								System.out.println(rs.getString("name"));
							}
							//System.out.println("gets here 4");
						}catch(Exception qe) {
							System.out.println("Querry Error: \n"+qe);
						}

					}catch(Exception ex) {
						System.out.println(ex);
					}
				}else {
					System.out.println("Please connect to the DB");
				}
			}
		});
		AddSensor.setBounds(16, 232, 117, 29);
		frame.getContentPane().add(AddSensor);

		JButton UpdateSensor = new JButton("Sensor Status");
		UpdateSensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					Statement statement;
					ArrayList<String> sensors=new ArrayList<String>();
					
				      try {
				    	  statement = connection.createStatement();
				    	  ResultSet rs = statement.executeQuery("SELECT * FROM sensor;") ;

				    	  while (rs.next()) {
								System.out.println("sensor id: "+rs.getString("sensor id"));
								sensors.add(rs.getString("sensor id"));
								
				    	  }
				    	  
				    	  System.out.println("sensors: "+Arrays.toString(sensors.toArray()));
				    	  
				    	  JPanel panel = new JPanel(new GridLayout(2, 2));
				    	  final JComboBox<String> sensorDropdown = new JComboBox(sensors.toArray());
				    	  panel.add(new JLabel("Pick sensor ID to update :"));
				    	  panel.add(sensorDropdown);
				    	  JOptionPane.showMessageDialog(null, panel,"Pick Sensor",JOptionPane.QUESTION_MESSAGE);
				    	  
				    	  String selectedSensor=sensorDropdown.getItemAt(sensorDropdown.getSelectedIndex());
				    	  System.out.println("selected sensor: "+selectedSensor);
				    	  
				    	  rs = statement.executeQuery("SELECT * FROM sensor;") ;
				    	  while (rs.next()) {
								System.out.println("sensor id: "+rs.getString("sensor id"));
								if(rs.getString("sensor id").equals(selectedSensor)) {
									break;
								}
				    	  }
				    	  
				    	  // wait for user edits then update value
				    	  try {
				    		  statement = connection.createStatement();
				    		  ResultSet rsSensor=rs;
				    		  
				    		  
				    		  panel = new JPanel(new GridLayout(8, 2));
						      JTextField sensorID = new JTextField(10);
						      sensorID.setText(rsSensor.getString("sensor id"));
						      String beginID=rsSensor.getString("sensor id");
						      JTextField x = new JTextField(10);
						      x.setText(rsSensor.getString("x"));
						      String beginX = rsSensor.getString("x");
						      JTextField y = new JTextField(10);
						      y.setText(rsSensor.getString("y"));
						      String beginY = rsSensor.getString("y");
						      JTextField last_charged = new JTextField(20);
						      last_charged.setText(rsSensor.getString("last charged"));
						      JTextField maintainer = new JTextField(10);
						      maintainer.setText(rsSensor.getString("maintainer"));
						      JTextField last_read = new JTextField(20);
						      last_read.setText(rsSensor.getString("last read"));
						      JTextField energy = new JTextField(10);
						      energy.setText(rsSensor.getString("energy"));
						      panel.add(new JLabel("Sensor ID :"));
						      panel.add(sensorID);
						      panel.add(new JLabel("X :"));
						      panel.add(x);
						      panel.add(new JLabel("Y:"));
						      panel.add(y);
						      panel.add(new JLabel("Last Charged :"));
						      panel.add(last_charged);
						      panel.add(new JLabel("Maintainer :"));
						      panel.add(maintainer);
						      panel.add(new JLabel("Last Read :"));
						      panel.add(last_read);
						      panel.add(new JLabel("Energy :"));
						      panel.add(energy);
						      JOptionPane.showMessageDialog(null, panel);

						      System.out.println("Inputs:\n- "+sensorID.getText() +"\n- "+ x.getText() +"\n- "+
						    	         y.getText()+"\n- "+ last_charged.getText()+"\n- "+
						    	         maintainer.getText()+"\n- "+ last_read.getText()+"\n- "+
						    	         energy.getText());
				    		  
				    		  // Same as add Sensor from here but UPDATE instead of INSERT
						      try {
									statement = connection.createStatement();

									String inputs= "\"sensor id\"="+sensorID.getText()+",x="+x.getText()+",y="+y.getText()+",\"last charged\"=\'"+last_charged.getText()+"\',maintainer="+maintainer.getText()+",\"last read\"=\'"+last_read.getText()+"\',energy="+energy.getText();
									

									System.out.println(inputs);
									String query ="UPDATE sensor SET "+inputs+" WHERE \"sensor id\"="+sensorID.getText()+";";
									System.out.println("Querry: "+query);
									
									try {
										//System.out.println("gets here -1");
										String duplicate_query="SELECT * FROM (SELECT * FROM sensor WHERE \"sensor id\"!="+beginID+") AS dp1 WHERE \"sensor id\"="+sensorID.getText()+";";
										System.out.println("duplicate query: "+duplicate_query);
										ResultSet dq=statement.executeQuery(duplicate_query);

										int sim_sensor_ID =0;
										while (dq.next()) {
											sim_sensor_ID++;
										}

										System.out.println("Num duplicate sensor ID : "+sim_sensor_ID);

										duplicate_query="SELECT * FROM (SELECT * FROM sensor WHERE x!="+beginX+" AND y!="+beginY+") AS dp1 WHERE x="+x.getText()+" AND y="+y.getText()+";";
										System.out.println("duplicate query: "+duplicate_query);
										dq=statement.executeQuery(duplicate_query);

										int sim_sensor_coord =0;
										while (dq.next()) {
											sim_sensor_coord++;
										}

										System.out.println("Num duplicate sensor Coordinates : "+sim_sensor_coord);



										if(sim_sensor_coord==0 && sim_sensor_ID==0) {
											try {
												statement.executeQuery(query) ;
											}catch(Exception qae){
												System.out.println("Querry error:\n"+qae);
											}
											TableStatusBar.setText("Updating Sensor Succesful!\nDiplayed Table: Sensor");
											String success_msg="Updating Sensor \""+sensorID.getText()+" \" Succesful!";
											JOptionPane.showMessageDialog(frame, success_msg, "Sensor Update Accepted",JOptionPane.PLAIN_MESSAGE);
										}else {
											TableStatusBar.setText("Failure Updating Sensor\nDiplayed Table: Sensor");
											String success_msg="A sensor with ID: \""+sensorID.getText()+"\" or coordinates ("+x.getText()+", "+y.getText()+") already exists ";
											JOptionPane.showMessageDialog(frame, success_msg, "Failure Updating Sensor",JOptionPane.PLAIN_MESSAGE);
										}


										//System.out.println("gets here 0");
										// display table
										rs = statement.executeQuery("SELECT * FROM sensor;") ;
										//System.out.println("gets here 1");
										

										System.out.println("Updated table");
										ResultTable.setModel(DbUtils.resultSetToTableModel(rs));

										//System.out.println("gets here 4");
									}catch(Exception qe) {
										System.out.println("Querry Error: \n"+qe);
									}

								}catch(Exception ex) {
									System.out.println(ex);
								}
						      
						      
				    	  }catch(Exception qe){
				    		  System.out.println("Error: \n"+qe);
				    	  }
				    	  
				      }catch(Exception ex) {
				    	  //
				      }	
				}else {
					System.out.println("Please connect to the Database");
					JOptionPane.showMessageDialog(frame, "Please Connect to The Database!","Not Connected to DB", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		UpdateSensor.setBounds(16, 395, 117, 29);
		frame.getContentPane().add(UpdateSensor);

		AddTitle = new JTextField();
		AddTitle.setBackground(SystemColor.textHighlight);
		AddTitle.setText("Add to Tables");
		AddTitle.setBounds(16, 112, 130, 26);
		frame.getContentPane().add(AddTitle);
		AddTitle.setColumns(10);

		UpdateTitle = new JTextField();
		UpdateTitle.setBackground(SystemColor.textHighlight);
		UpdateTitle.setText("Update Elements");
		UpdateTitle.setBounds(16, 356, 144, 26);
		frame.getContentPane().add(UpdateTitle);
		UpdateTitle.setColumns(10);

		JButton UpdateForestArea = new JButton("Forest Covered Area");
		UpdateForestArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					Statement statement;
					ArrayList<String> forests=new ArrayList<String>();
					
					
					try {
						statement = connection.createStatement();
				    	ResultSet rs = statement.executeQuery("SELECT DISTINCT * FROM forest;") ;
				    	
				    	while (rs.next()) {
							System.out.println("forest name: "+rs.getString("name"));
							forests.add(rs.getString("name"));
				    	}
				    	
				    	System.out.println("forests: "+Arrays.toString(forests.toArray()));
				    	
				    	  JPanel panel = new JPanel(new GridLayout(2, 2));
				    	  final JComboBox<String> forestDropdown = new JComboBox(forests.toArray());
				    	  panel.add(new JLabel("Pick sensor ID to update :"));
				    	  panel.add(forestDropdown);
				    	  JOptionPane.showMessageDialog(null, panel,"Pick Sensor",JOptionPane.QUESTION_MESSAGE);
				    	  
				    	  String selectedForest=forestDropdown.getItemAt(forestDropdown.getSelectedIndex());
				    	  System.out.println("selected sensor: "+selectedForest);
				    	  
				    	  rs = statement.executeQuery("SELECT * FROM forest;") ;
				    	  while (rs.next()) {
								System.out.println("Forest name: "+rs.getString("name"));
								if(rs.getString("name").equals(selectedForest)) {
									break;
								}
				    	  }
				    	  
				    	  
				    	  try {
				    		  statement = connection.createStatement();
				    		  ResultSet rsForest=rs;
				    		  
				    		  panel = new JPanel(new GridLayout(8, 2));
						      JTextField forest_num = new JTextField(10);
						      forest_num.setText(rsForest.getString("forest no"));
						      JTextField name = new JTextField(10);
						      name.setText(rsForest.getString("name"));
						      String beginFName=rsForest.getString("name");
						      JTextField acid_level = new JTextField(10);
						      acid_level.setText(rsForest.getString("acid level"));
						      JTextField mbr_xmin = new JTextField(10);
						      mbr_xmin.setText(rsForest.getString("mbr xmin"));
						      JTextField mbr_xmax = new JTextField(10);
						      mbr_xmax.setText(rsForest.getString("mbr xmax"));
						      JTextField mbr_ymin = new JTextField(10);
						      mbr_ymin.setText(rsForest.getString("mbr ymin"));
						      JTextField mbr_ymax = new JTextField(10);
						      mbr_ymax.setText(rsForest.getString("mbr ymax"));
						      JTextField area = new JTextField(10);
						      area.setText(rsForest.getString("area"));
						      panel.add(new JLabel("Forest No :"));
						      panel.add(forest_num);
						      panel.add(new JLabel("Name :"));
						      panel.add(name);
						      panel.add(new JLabel("Area :"));
						      panel.add(area);
						      panel.add(new JLabel("Acid Level :"));
						      panel.add(acid_level);
						      panel.add(new JLabel("mbr xmin :"));
						      panel.add(mbr_xmin);
						      panel.add(new JLabel("mbr xmax :"));
						      panel.add(mbr_xmax);
						      panel.add(new JLabel("mbr ymin :"));
						      panel.add(mbr_ymin);
						      panel.add(new JLabel("mbr ymax :"));
						      panel.add(mbr_ymax);
						      JOptionPane.showMessageDialog(null, panel);
						      
						      System.out.println("Inputs:\n- "+forest_num.getText() +"\n- "+ name.getText() +"\n- "+
						    	         area.getText()+"\n- "+ acid_level.getText()+"\n- "+
						    	         mbr_xmin.getText()+"\n- "+ mbr_xmax.getText()+"\n- "+
						    	         mbr_ymin.getText()+"\n- "+ mbr_ymax.getText());
						      
						      // Update value
						      try {
						    	  statement = connection.createStatement();

						    	  String inputs= "\"forest no\"="+forest_num.getText()+",name=\'"+name.getText()+"\',area="+area.getText()+",\"acid level\"="+acid_level.getText()+",\"mbr xmin\"="+mbr_xmin.getText()+",\"mbr xmax\"=\'"+mbr_xmax.getText()+"\',\"mbr ymin\"="+mbr_ymin.getText()+",\"mbr ymax\"="+mbr_ymax.getText();
									

								  System.out.println(inputs);
								  String query ="UPDATE forest SET "+inputs+" WHERE \"name\"=\'"+name.getText()+"\';";
								  System.out.println("Querry: "+query);
								  
								  try {
									  	String duplicate_query="SELECT * FROM (SELECT * FROM forest WHERE \"name\"!=\'"+beginFName+"\') AS f1 WHERE \"name\"=\'"+name.getText()+"\';";
										System.out.println("duplicate query: "+duplicate_query);
										ResultSet dq=statement.executeQuery(duplicate_query);
										
										int sim_name =0;
										while (dq.next()) {
											sim_name++;
										}
										
										System.out.println("Num duplicate forest name : "+sim_name);
										
										if(sim_name==0) {
											try {
												statement.executeQuery(query) ;
											}catch(Exception qae){
												//System.out.println("Querry error:\n"+qae);
											}
											
											TableStatusBar.setText("Updating Forest Succesful!\nDiplayed Table: Forest");
											String success_msg="Updating Forest \""+name.getText()+" - "+forest_num.getText()+" \" Succesful!";
											JOptionPane.showMessageDialog(frame, success_msg, "Forest Update Accepted",JOptionPane.PLAIN_MESSAGE);
										}else {
											TableStatusBar.setText("Failure Updating Forest\nDiplayed Table: Forest");
											String success_msg="Forest No : \""+forest_num.getText()+"\" or Name ("+name.getText()+" already exists ";
											JOptionPane.showMessageDialog(frame, success_msg, "Failure Updating forest",JOptionPane.ERROR_MESSAGE);
										}
										
										rs = statement.executeQuery("SELECT * FROM forest;") ;
										//System.out.println("gets here 1");
										

										System.out.println("Updated Forest table");
										ResultTable.setModel(DbUtils.resultSetToTableModel(rs));
										
										
										
								  }catch(Exception de) {
									  System.out.println("Querry Error: \n"+de);
								  }
								  
						      }catch(Exception ex) {
						    	  //System.out.println(ex);
						      }

				    		  
				    	  }catch(Exception qe) {
				    		  System.out.println("Error: \n"+qe);
				    	  
				    	  }
				    	
					}catch(Exception ex) {
						
					}
					
				}else {
					System.out.println("Please connect to the Database");
					JOptionPane.showMessageDialog(frame, "Please Connect to The Database!","Not Connected to DB", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		UpdateForestArea.setBounds(4, 436, 162, 29);
		frame.getContentPane().add(UpdateForestArea);

		FunctionsTitle = new JTextField();
		FunctionsTitle.setEditable(false);
		FunctionsTitle.setBackground(new Color(32, 178, 170));
		FunctionsTitle.setText("Functions");
		FunctionsTitle.setBounds(806, 112, 130, 26);
		frame.getContentPane().add(FunctionsTitle);
		FunctionsTitle.setColumns(10);

		JButton SwitchWorkerDuty = new JButton("Switch Worker Duties");
		SwitchWorkerDuty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					Statement statement;
					ArrayList<String> workers=new ArrayList<String>();
				      try {
				    	  statement = connection.createStatement();
				    	  ResultSet rs = statement.executeQuery("SELECT * FROM worker;") ;

				    	  while (rs.next()) {
								System.out.println("ssn: "+rs.getString("ssn"));
								workers.add(rs.getString("ssn"));
				    	  }
				      }catch(Exception ex) {
				    	  //
				      }
				  
				      
				      
				  
				  System.out.println("workers: "+Arrays.toString(workers.toArray()));

				  JPanel panel = new JPanel(new GridLayout(2, 2));

			      final JComboBox<String> dropdownA = new JComboBox(workers.toArray());

			      final JComboBox<String> dropdownB = new JComboBox(workers.toArray());
			      panel.add(new JLabel("Worker A :"));
			      //panel.add(workerA);
			      panel.add(dropdownA);
			      panel.add(new JLabel("Worker B :"));
			      panel.add(dropdownB);
			      JOptionPane.showMessageDialog(null, panel,"Switch Worker Duties",JOptionPane.WARNING_MESSAGE);

			      String workerA=dropdownA.getItemAt(dropdownA.getSelectedIndex());
			      String workerB=dropdownB.getItemAt(dropdownB.getSelectedIndex());

			      try {
			    	  statement = connection.createStatement();
			    	  
			    	  
			    	  
			    	  
			    	  String query="SELECT employing_state FROM worker WHERE ssn=\'"+workerA+"\';";
			    	  ResultSet rsA = statement.executeQuery(query);

			    	  String stateA=null;
			    	  while (rsA.next()) {
							System.out.println("state worker A: "+rsA.getString("employing_state"));
							stateA=rsA.getString("employing_state");
			    	  }

			    	  query="SELECT employing_state FROM worker WHERE ssn=\'"+workerB+"\';";
			    	  ResultSet rsB = statement.executeQuery(query);

			    	  String stateB=null;
			    	  while (rsB.next()) {
							System.out.println("State worker B: "+rsB.getString("employing_state"));
							stateB=rsB.getString("employing_state");
			    	  }
			    	  System.out.println("Inputs:\n- "+workerA +"\n- "+ workerB);

			    	  if (stateA.equals(stateB) && stateA!=null && stateB!=null) {
			    		// switch sensors
					      try {
					    	  query="UPDATE sensor SET maintainer=\'"+workerA+"\' WHERE maintainer=\'"+workerB+"\';";
					    	  statement.executeQuery(query) ;

					    	  query="UPDATE sensor SET maintainer=\'"+workerB+"\' WHERE maintainer=\'"+workerA+"\';";
					    	  statement.executeQuery(query);
					      }catch(Exception ue) {
					    	  // handle
					    	  System.out.println("Error changing sensor mainntainers: \n"+ue);
					    	  //JOptionPane.showMessageDialog(frame, "Error changing sensor maintainers: \n"+ue, "Querry Error",JOptionPane.ERROR_MESSAGE);

					      }
					      TableStatusBar.setText("Switching worker duties was succesful!\nDiplayed Table: Sensor");
				    	  String success_msg="Switching workers \""+workerA+" and "+workerB+" maintainance duties was succesful!";
						  JOptionPane.showMessageDialog(frame, success_msg, "Switch Worker Duties Succesful",JOptionPane.PLAIN_MESSAGE);
						  System.out.println("switch success");
			    	  }else if (stateA==null || stateB==null) {
			    		  // one has no state
			    		  TableStatusBar.setText("Switching worker duties failed\nDiplayed Table: Sensor");
				    	  String success_msg="One worker has no state listed on workers table. Failed to switch workers \""+workerA+" and "+workerB+" maintainance duties";
						  JOptionPane.showMessageDialog(frame, success_msg, "Failure Switching Worker Duties",JOptionPane.ERROR_MESSAGE);
			    	  }else {
			    		  // not in same state
			    		  TableStatusBar.setText("Switching worker duties failed\nDiplayed Table: Sensor");
				    	  String success_msg="Mainntained Sensors in different states!\nFailed to switch workers \""+workerA+" and "+workerB+" maintainance duties";
						  JOptionPane.showMessageDialog(frame, success_msg, "Failure Switching Worker Duties",JOptionPane.ERROR_MESSAGE);
			    	  }

			    	  ResultSet rs = statement.executeQuery("SELECT * FROM sensor") ;
						System.out.println("Switch Worker Duties Updated table");
						ResultTable.setModel(DbUtils.resultSetToTableModel(rs));


			      }catch(Exception se) {
			    	  	System.out.println("Error getting worker state: \n"+se);
			      }





				}else {
					System.out.println("Please connect to the Database");
					JOptionPane.showMessageDialog(frame, "Please Connect to The Database!","Not Connected to DB", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		SwitchWorkerDuty.setBounds(785, 150, 168, 29);
		frame.getContentPane().add(SwitchWorkerDuty);

		JButton TopKWorkers = new JButton("Top-k Busy Workers");
		TopKWorkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					// get user input
					  JPanel panel = new JPanel(new GridLayout(1, 2));
				      JTextField k = new JTextField(10);
				      panel.add(new JLabel(" K value :"));
				      panel.add(k);
				      JOptionPane.showMessageDialog(null, panel);

				      try {
				    	  Statement statement = connection.createStatement();
				    	  String input= k.getText();

				    	  System.out.println(input);
				    	  String query ="SELECT maintainer as worker, count(\"sensor id\") AS \"Number of Sensors\" FROM sensor WHERE energy<=2 GROUP BY \"sensor id\" ORDER BY maintainer DESC LIMIT "+input+";";
						  System.out.println("Querry: "+query);

						  ResultSet rs=statement.executeQuery(query);

						  int size =0;
						  while (rs.next()) {
						       size++;
						  }

						  if(size>0) {
							  	TableStatusBar.setText("Some workers need to recharge their sensors!\nDiplayed Table: Sensor (energy<=2)");
								String success_msg="Top "+k.getText()+" workers";
								JOptionPane.showMessageDialog(frame, success_msg, "Top K busy workers",JOptionPane.PLAIN_MESSAGE);

								System.out.println("top K workers Updated table");
								rs=statement.executeQuery(query);
								ResultTable.setModel(DbUtils.resultSetToTableModel(rs));
						  }else{
							  	TableStatusBar.setText("All sensors are charged, no workers need to maintain!\nDiplayed Table: Sensor ");
								String success_msg="No sensors need to be recharged!!";
								JOptionPane.showMessageDialog(frame, success_msg, "No busy workers",JOptionPane.PLAIN_MESSAGE);

								rs = statement.executeQuery("SELECT * FROM sensor") ;
								System.out.println(" No Busy workers Updated table");
								ResultTable.setModel(DbUtils.resultSetToTableModel(rs));
						  }

				      }catch(Exception se) {

				      }
				}else {
					System.out.println("Please connect to the DB");
					JOptionPane.showMessageDialog(frame, "Please connect to the Database","Connect to DB", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		TopKWorkers.setBounds(795, 191, 155, 29);
		frame.getContentPane().add(TopKWorkers);

		JButton SensorRankings = new JButton("Sensor Ranking");
		SensorRankings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					try {
						  Statement statement = connection.createStatement();

				    	  String query ="SELECT \"sensor id\" AS \"Sensor ID\",COUNT(\"sensor id\") AS \"Report Activity\" FROM report GROUP BY \"sensor id\" ORDER BY \"Report Activity\" DESC;";
						  System.out.println("Querry: "+query);

						  ResultSet rs=statement.executeQuery(query);

						  TableStatusBar.setText("Sensors ranked by report activity!\nDiplayed Table: Report group by count of unique sensors");
						  System.out.println("top K workers Updated table");
						  rs=statement.executeQuery(query);
						  ResultTable.setModel(DbUtils.resultSetToTableModel(rs));


						  String success_msg="Table of ranked sensors by temperature report activity";
						  JOptionPane.showMessageDialog(frame, success_msg, " Display Sensors Ranking ",JOptionPane.PLAIN_MESSAGE);



					}catch(Exception qe){
						JOptionPane.showMessageDialog(frame, "Could not complete sensor report activity ranking","System Querry Error", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					System.out.println("Please connect to the DB");
					JOptionPane.showMessageDialog(frame, "Please connect to the Database","Connect to DB", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		SensorRankings.setBounds(814, 232, 122, 29);
		frame.getContentPane().add(SensorRankings);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(172, 118, 606, 479);
		frame.getContentPane().add(scrollPane);

		ResultTable = new JTable();
		scrollPane.setViewportView(ResultTable);
		ResultTable.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		ResultTable.setBackground(new Color(240, 248, 255));

		JButton btnNewButton_1 = new JButton("Reset Database");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connection!=null) {
					//Initialize the script runner
						
				      //Creating a reader object
						System.out.println("Working Directory = " + System.getProperty("user.dir"));
						try {
							Statement statement = connection.createStatement();
							
							// Reset Table
							BufferedReader reader = new BufferedReader(new FileReader("./reset_table.sql"));
							String s = null;
							String query="";
							while ((s=reader.readLine())!=null){
										query+=s+"\n";
							           //System.out.println(s);
							}
							
							//System.out.println(query);
							try {
								statement.executeQuery(query);
							}catch(Exception ce) {
								System.out.println("create exception: "+ce);
							}
							
							// Insert Values from PA1
							reader = new BufferedReader(new FileReader("./insert_values.sql"));
							s = null;
							query="";
							while ((s=reader.readLine())!=null){
										query+=s+"\n";
							           //System.out.println(s);
							}
							//System.out.println(query);
							try {
								statement.executeQuery(query);
							}catch(Exception ae) {
								System.out.println("add exception: "+ae);
							}
							
							
							// 
							TableStatusBar.setText("All Tables Data Reset to Defaults\nDiplayed Table: Forest");
							
							ResultSet rs=statement.executeQuery("SELECT * FROM forest;");
							ResultTable.setModel(DbUtils.resultSetToTableModel(rs));


							String success_msg="All Tables Data Reset to Default";
							JOptionPane.showMessageDialog(frame, success_msg, " Reset Succesful",JOptionPane.PLAIN_MESSAGE);

							
							
						}catch(Exception ex) {
							System.out.println(ex.getMessage());
						}
				      
				}else {
					System.out.println("Please connect to the DB");
					JOptionPane.showMessageDialog(frame, "Please connect to the Database","Connect to DB", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_1.setForeground(new Color(255, 0, 0));
		btnNewButton_1.setBounds(792, 491, 144, 29);
		frame.getContentPane().add(btnNewButton_1);

		final JToggleButton connectButton = new JToggleButton("Connect DB");
		connectButton.setForeground(new Color(255, 0, 0));
		connectButton.setBackground(new Color(0, 0, 0));
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connectButton.isSelected()) {
					connection= connectDB();
					connectButton.setText("Disconnect DB");
					//System.out.println("Connected to DB");
				}else {
					connection=null;
					connectButton.setText("Reconnect DB");
					System.out.println("Disconnected to DB");
				}

			}
		});
		connectButton.setBounds(24, 52, 122, 29);
		frame.getContentPane().add(connectButton);
		
		try {
			connection=connectDB();
			Statement statement = connection.createStatement();
			TableStatusBar.setText("Welcome to Mkhanyisi Gamedze's COSI127B: PA1 GUI\nDefault Table: Forest");
		
			ResultSet rs=statement.executeQuery("SELECT * FROM forest;");
			ResultTable.setModel(DbUtils.resultSetToTableModel(rs));
			connection=null;
		}catch(Exception e) {
			System.out.println("start exception: "+e);
		}
		


	}
}
