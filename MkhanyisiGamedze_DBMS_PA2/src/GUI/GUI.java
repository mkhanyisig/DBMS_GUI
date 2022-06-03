package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI implements ActionListener{
	
	private int clicks = 0;
    private JLabel label = new JLabel("Number of clicks:  0     ");
    private JFrame frame = new JFrame();

	
	public GUI() {
		buildGUI();
		Connection conn= connectDB();
		
		if(conn!=null) {
			System.out.println("Connected to DB");
			try {
				Statement statement = conn.createStatement();
			
				//ResultSet rs=statement.executeQuery("SELECT datname FROM pg_database;");
			
				//Statement st = con.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM forest;") ;
	        
				while (rs.next()) {
					System.out.println(rs.getString("name"));
				}
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		
	}
	
	public void buildGUI() {
		// the clickable button
        JButton button = new JButton("Click Me");
        button.addActionListener(this);
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(40,40,20,40));
		frame.setBounds(100, 100, 570, 300);
		panel.setLayout( new GridLayout(0,1));
		
		// the panel with the button and text
        panel.add(button);
        panel.add(label);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Forest");
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public Connection connectDB() {
		Connection connection = null;
		
		try {
			
			Class.forName("org.postgresql.Driver");
			connection=DriverManager.getConnection("jdbc:postgresql://localhost:5433/PS1","postgres","76225240");
			
			
			if(connection!=null) {
				System.out.println("Connected to DB");
			}else {
				System.out.println("Connection unsuccesful :(");
			}
			
		}catch(Exception e) {
				System.out.println("** exception\n"+e);
		}
		
		return connection;
		
	}
	
	// process the button clicks
    public void actionPerformed(ActionEvent e) {
        clicks++;
        label.setText("Number of clicks:  " + clicks);
    }

	
	public static void main(String[] args) {
        new GUI();
		System.out.println("\n*****\nMkhanyisi Gamedze PA1 GUI");
    }

}
