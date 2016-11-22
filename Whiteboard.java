/*
	CS460 - Turtle Graphics Client
	Created by: Jesus Garcia
	
	Task: 
		" 	The application to be developed has two parts: a server, that represents a distributed "white board" and a client application, that sends commands to the server. The server interprets and executes the commands, which results in simple lines being drawn on the white board. At any time there may be several clients talking to the server and thus drawing on the white board.
			The white board behaves like a turtle graphics application. This means that there is a single pen, which may be lifted (and thus does not draw) or put down (when it is supposed to draw). The pen can be moved in any one direction of north/south/east/west. When it is moved, it will do so in a pre-configured (but not hard-coded) increment in terms of length. This may or may not result in a line being drawn, depending on whether the pen is lifted (up) or not (down).
			The client has a simple GUI with these input elements:
			- Server address/port
			- Buttons that represent the geographic directions N/S/E/W. If any one of these buttons is hit the pen is moved in that direction hit, at a length that is configurable.
			- Buttons UP/DOWN for lifting the pen or putting it down
			- Field to input the length of the distance to be moved upon hitting one of N/S/E/W "
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Calendar;
import java.lang.Thread;
import java.net.*;
import java.io.*;

public class Whiteboard implements ActionListener{
	
	// Init Globals
	Date date = new Date();
	Calendar calendar = Calendar.getInstance();
	TextField result;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
	public static String[] cmdInput;
	
	JPanel titlePanel, timePanel, buttonPanel;
	JLabel dateLabel, northOutput, southOutput, eastOutput, westOutput, liftOutput, lowerOutput;
	JButton northButton, southButton, eastButton, westButton, liftButton, lowerButton, resetButton;
	/*
	public void connect() throws IOException{
		try {
			socket = new Socket(cmdInput[0], Integer.parseInt(cmdInput[1]));
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("Connected to Server.");
		} catch (ConnectException e){
			System.out.println("Error: Couldn't connect to server");
			System.exit(1);
		}
	}
	*/
	
	public JPanel createContentPane(){
		// Create a bottom JPanel to contain everything
		JPanel totalGUI = new JPanel();
		totalGUI.setLayout(null);
		
			// Create a panel to have titles
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setLocation(10,0);
		titlePanel.setSize(480,30);
		// Must be added to the totalGUI JPanel
		totalGUI.add(titlePanel);
		
		// Create a label to label the output of blue score
		dateLabel = new JLabel("Local Time: " + date.toString());
		dateLabel.setLocation(0,0);
		dateLabel.setSize(480,30);
		dateLabel.setHorizontalAlignment(0);
		dateLabel.setForeground(Color.blue);
		// Must be added to the titles JPanel
		titlePanel.add(dateLabel);
		
			// Panel for the Buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setLocation(10,30);
		buttonPanel.setSize(480,100);
		totalGUI.add(buttonPanel);
		
		// Create a panel to have titles
		displayPanel drawDisplay = new displayPanel();
		drawDisplay.setLayout(null);
		drawDisplay.setLocation(10,150);
		drawDisplay.setSize(480,400);
		// Must be added to the totalGUI JPanel
		totalGUI.add(drawDisplay);
		
		// Create buttons with given actionListeners
		northButton = new JButton("Go North");
        northButton.setLocation(0, 0);
        northButton.setSize(120, 30);
        northButton.addActionListener(this);
        buttonPanel.add(northButton);

        southButton = new JButton("Go South");
        southButton.setLocation(120, 0);
        southButton.setSize(120, 30);
        southButton.addActionListener(this);
        buttonPanel.add(southButton);
		
		eastButton = new JButton("Go East");
        eastButton.setLocation(240, 0);
        eastButton.setSize(120, 30);
        eastButton.addActionListener(this);
        buttonPanel.add(eastButton);
		
		westButton = new JButton("Go West");
        westButton.setLocation(360, 0);
        westButton.setSize(120, 30);
        westButton.addActionListener(this);
        buttonPanel.add(westButton);

        liftButton = new JButton("Lift Pen");
        liftButton.setLocation(0, 35);
        liftButton.setSize(240, 30);
        liftButton.addActionListener(this);
        buttonPanel.add(liftButton);
		
		lowerButton = new JButton("Lower Pen");
        lowerButton.setLocation(240, 35);
        lowerButton.setSize(240, 30);
        lowerButton.addActionListener(this);
        buttonPanel.add(lowerButton);

        resetButton = new JButton("Clear board");
        resetButton.setLocation(0, 70);
        resetButton.setSize(480, 30);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);
		
		totalGUI.setOpaque(true);
		return totalGUI;
	}
	
	// Create the actionPerformed method that catches ActionListeners
	public void actionPerformed(ActionEvent e){
		calendar = Calendar.getInstance();/*
		if(e.getSource() != resetButton){
			try {
				connect();
			} catch(IOException exception) {
				System.out.println("Error: Couldn't connect to server");
				System.exit(1);
			}
		}
		
		if(e.getSource() == northButton){
			northOutput.setText(""+(calendar.get(Calendar.YEAR)));
		} else if(e.getSource() == southButton){
			southOutput.setText(""+(calendar.get(Calendar.MONTH)+1));
		} else if(e.getSource() == eastButton){
			eastOutput.setText(""+(calendar.get(Calendar.DAY_OF_MONTH)));
		} else if(e.getSource() == westButton){
			westOutput.setText(""+(calendar.get(Calendar.HOUR)));
		} else if(e.getSource() == liftButton){
			liftOutput.setText(""+(calendar.get(Calendar.MINUTE)));
		} else if(e.getSource() == lowerButton){
			lowerOutput.setText(""+(calendar.get(Calendar.SECOND)));
		} else if(e.getSource() == resetButton){
			northOutput.setText("North");
			southOutput.setText("South");
			eastOutput.setText("East");
			westOutput.setText("West");
			liftOutput.setText("Lifted Pen");
			lowerOutput.setText("Lowered Pen");
		}
		*/
	}
	
	private static void createWindow(){
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("CS460 Whiteboard GUI");
		
		// Create the content panel
		Whiteboard demo = new Whiteboard();
		
		frame.setContentPane(demo.createContentPane());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(520,600);
		frame.setVisible(true);
		// Refresh time
		while(true){
			try{ Thread.sleep(1000); }
			catch(InterruptedException except){
				Thread.currentThread().interrupt();
			}
			demo.date = new Date();
			demo.dateLabel.setText("Local Time: " + demo.date.toString());
		}
	}
	
	public static void main(String[] args){
		/*if(args.length != 2){
			System.out.println("Error: Proper usage -> 'javaGUI [server] [port]");
			System.exit(1);
		}*/
		// cmdInput = args;
		createWindow();
	}
}