// Turtle Graphics Client, for CS-460 Project 5
// Authors: Jesus Garcia, Chance Nelson

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import java.util.Calendar;
import java.lang.Thread;
import java.net.*;
import java.io.*;

// Whiteboard keeps track of client-server communication and initiating line draws
public class Whiteboard implements ActionListener{
	
	// Init Globals
	Date date = new Date();
	Calendar calendar = Calendar.getInstance();
	TextField result;
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	int currentX = 200;
	int currentY = 200;
	int flag = 1;
	int direction = 0;
	
	JPanel totalGUI = new JPanel();
	displayPanel drawDisplay = new displayPanel();
	JPanel titlePanel, textPanel, timePanel, buttonPanel;
	JLabel dateLabel, northOutput, southOutput, eastOutput, westOutput, liftOutput, lowerOutput;
	JButton northButton, southButton, eastButton, westButton, liftButton, lowerButton, resetButton;
	JTextField textField = new JTextField(3);

	// This is where the buttons and draw areas will be drawn
	public JPanel createContentPane(){
		// Create a bottom JPanel to contain everything
		totalGUI.setLayout(null);
		
		// Create a panel to have titles
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setLocation(10,0);
		titlePanel.setSize(360,30);
		// Must be added to the totalGUI JPanel
		totalGUI.add(titlePanel);
		
		// Create a panel to have the input field
		// THIS WILL CREATE AN INPUT FIELD
		textPanel = new JPanel();
		textPanel.setLayout(null);
		textPanel.setLocation(370,0);
		textPanel.setSize(110,30);
		// Must be added to the totalGUI JPanel
		totalGUI.add(textPanel);
		
		// Create a label to label the output of blue score
		// This is just a basic 'output text' field
		dateLabel = new JLabel("Local Time: " + date.toString());
		dateLabel.setLocation(0,0);
		dateLabel.setSize(360,30);
		dateLabel.setHorizontalAlignment(0);
		dateLabel.setForeground(Color.blue);
		// Must be added to the titles JPanel
		titlePanel.add(dateLabel);
		
		// Add the textfield to the textPanel
		textField.setLocation(0,0);
		textField.setSize(100,30);
		textField.setHorizontalAlignment(0);
		textPanel.add(textField);
		
		// Panel for the Buttons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setLocation(10,30);
		buttonPanel.setSize(480,100);
		totalGUI.add(buttonPanel);
		
		// Create a panel to have titles
		drawDisplay.setLayout(null);
		drawDisplay.setLocation(10,150);
		drawDisplay.setSize(480,480);
		// Must be added to the totalGUI JPanel
		totalGUI.add(drawDisplay);
		
		// Create buttons with given actionListeners
		// HERE WE DRAW THE BUTTONS
		// Each one is given a location and a size within the button panel
		northButton = new JButton("Go North");
        northButton.setLocation(0, 0);
        northButton.setSize(120, 30);
        northButton.addActionListener(this);
        buttonPanel.add(northButton);
	
		// Another few buttons GO TO "actionPerformed" function to see what these buttons do
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

		totalGUI.setOpaque(true);
		return totalGUI;
	}
	
	// Create the actionPerformed method that catches ActionListeners
	public void actionPerformed(ActionEvent e){
		calendar = Calendar.getInstance();
		int length = 0;
		String text = textField.getText();
		
		if(e.getSource() == liftButton){
			// Update a value to not draw things
			flag = 0;
			direction = 0;
		} else if(e.getSource() == lowerButton){
			// Update a value to start drawing things again
			flag = 1;
		}
		
		if(textField.getText() != ""){
			// Here we parse the input text from the text field and convert it to an Int
			try{
				length = Integer.parseInt(text);
			}catch(NumberFormatException exception){
				System.err.println("Empty text field... Setting length to '0.'");
				length = 0;
			}
		} else {
			length = 0;
		}
		if(length >= 51){
			// Here we clamp the input number to be a max of 50
			System.out.println("Max line size is 50.");
			length = 50;
		}
		
		/* Now for the actual drawing buttons
			Each button will draw a given length line in a certain direction
			Each of the methods called will be in the DisplayPanel.java file
			Think of displaypanel.java as a helper class for the GUI
		*/
		if(e.getSource() == northButton){
			direction = 1;

		} else if(e.getSource() == southButton){
			direction = 2;

		} else if(e.getSource() == eastButton){
			direction = 3;

		} else if(e.getSource() == westButton){
			direction = 4;

		}
             
		
		if(flag == 1){
			if(e.getSource() == lowerButton){
				// Do nothing
			} else {
				// send the packets corresponding to the line being drawn, and then draw the line
				// locally just to make sure things dont break horribly
				send_packet(length, direction, drawDisplay.currentX, drawDisplay.currentY);           
				drawDisplay.draw_a_Line(flag, length, direction);
				System.out.print("Drawing line of size: " + length + ", ");
				// Repaint is required as without it, the display will not update OR will only paint the most recent
				// button press, if i recall correctly from when I was testing, the latter will occur.
				drawDisplay.repaint();
				totalGUI.repaint();
				sync();
			}
		} else {
			switch(direction){
				case 1:
					System.out.println("Moving line " + length + " steps North-ward");
					drawDisplay.draw_a_Line(flag, length, direction);
					break;
				case 2:
					System.out.println("Moving line " + length + " steps South-ward");
					drawDisplay.draw_a_Line(flag, length, direction);
					break;
				case 3:
					System.out.println("Moving line " + length + " steps East-ward");
					drawDisplay.draw_a_Line(flag, length, direction);
					break;
				case 4:
					System.out.println("Moving line " + length + " steps West-ward");
					drawDisplay.draw_a_Line(flag, length, direction);
					break;
			}
		}
	}

	/* desc: sends a series of packets to the server that represents the line being
			drawn
		args: length - length of line
			direction - flag for north, south, east, or west
			x, y - x and y coordinates for the start of the line */
	public void send_packet(int length, int direction, int x, int y) {
		
		if(direction == 1){  // n
			// Loops through for the length of a line, and sends a packet for 
			// each pixel to be changed	
			for(int i = 0; i < length; i++) {
                output.println(x + " " + y);
                y++;
            
            }

		} else if(direction == 2){  // s
			for(int i = 0; i < length; i++) {
                output.println(x + " " + y);
                y--;
            
            }

		} else if(direction == 3){  // e
			for(int i = 0; i < length; i++) {
                output.println(x + " " + y);
                x++;
            
            }

		} else if(direction == 4){  // w
			for(int i = 0; i < length; i++) {
                output.println(x + " " + y);
                x--;
            
            }

		}

    }
    
	
	// This function is largely irrelevant in terms of server creation and connection as this just draws the whole GUI window.
	private static void createWindow(String[] args){
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("CS460 Whiteboard GUI");
		
		// Create the content panel
		Whiteboard demo = new Whiteboard();
		
        demo.init_connection(args);

		frame.setContentPane(demo.createContentPane());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(520,680);
		frame.setVisible(true);
		// Refresh time
		while(true){
			try{ Thread.sleep(100); }
			catch(InterruptedException except){
				Thread.currentThread().interrupt();
			}
			demo.date = new Date();
			demo.dateLabel.setText("Local Time: " + demo.date.toString());
		}
	}

	// desc: retreives any packets the server has sent to the client, and adds
    //       the coordinates from these packets to the draw queue in drawDisplay
    public void sync() {
        System.out.println("syncing"); // println for debug purposes
        try {
            while(input.ready()) {
				String packet = input.readLine();
                System.out.println(packet);
                String[] temp = packet.split(" ");
                drawDisplay.updateQueue(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), 0, 0);                
            }
		// catch any IOEcxeptions from wacky errors
        } catch (IOException e) {
            System.out.println("Done syncing");            
            return;
        }
    }

	// desc: initialize a connection between the client and the server
    // args: args - command line arguments from main()
    public void init_connection(String[] args) {
		try{
            socket = new Socket(args[0], Integer.parseInt(args[1]));
			// create input and output streams to make my life easy
		    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    output = new PrintWriter(socket.getOutputStream(), true);
        // check for horrible errors
        } catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection init failed. Exiting");
			System.exit(1);
        }

    }

	
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Error: Proper usage -> 'Whiteboard [server] [port]");
			System.exit(1);
		
		}

        createWindow(args);

	}
}
