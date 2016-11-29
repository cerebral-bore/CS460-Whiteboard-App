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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Line2D;

public class displayPanel extends JPanel{
	Graphics g;
	Graphics2D g2 = (Graphics2D) g;
	int direction = 0;
	int initial = 0;
	int length;
	int currentX = 225;
	int currentY = 225;
	int iteration = -1;
	int[] array = new int[4];
	int[] tempArray = new int[4];
	List<Shape> draws = new ArrayList<>();
	
	// Setup the basic drawing plane
	public displayPanel(){
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));
		System.out.println("Test");
	}
	
	// Here is where things get complicated..
	// Long story short, in order to make a drawing area that contains all applied draws,
	// We need to create an extend a new JPanel type, here it is called displayPanel
	// paintComponent is a method from JPanel, so we call super.paintComponent to "include" the code from the original paintComponent
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		// Graphics 2d is a class that will be used throughout the drawing, not really too complex as it is a requirement to drawing
		Graphics2D g2 = (Graphics2D) g;
		
		// Set a draw width and color
		g2.setStroke(new BasicStroke(5));
		g2.setColor(Color.RED);
		
		// here we begin the painting
		// Initial will just mean that we need a starting point
		if(initial != 0){
			if(direction == 0){
				// Do nothing with make the current draw disappear
				// initial will equal either 1 or 2 or 3 or 4, being North South or East West
				// So we need to make the line face either up and down or left and right
				if((initial == 1) || (initial == 2)){
					updateQueue(currentX, currentY, 0, length);
				} else {
					updateQueue(currentX, currentY, length, 0);
				}
		// We enter this section if the drawing is NOT the first drawing to be made.
		} else if(direction == 1){
				// Here is where we go when the North button is pressed.
				System.out.println("Going North.");
				currentY -= length;
				clamp(currentY);
				updateQueue(currentX, currentY, 0, length);
				
			} else if(direction == 2){
				// Here is where we go when the South button is pressed.
				System.out.println("Going South.");
				currentY += length;
				clamp(currentY);
				updateQueue(currentX, currentY, 0, length);
				
			} else if(direction == 3){
				// Here is where we go when the East button is pressed.
				System.out.println("Going East.");
				currentX += length;
				clamp(currentX);
				updateQueue(currentX, currentY, length, 0);
				
			} else if(direction == 4){
				// Here is where we go when the West button is pressed.
				System.out.println("Going West.");
				currentX -= length;
				clamp(currentX);
				updateQueue(currentX, currentY, length, 0);
				
			}
		} else {
			updateQueue(currentX, currentY, 1, 1);
		}
		drawAll(g2);
	}
	
	// draw_a_Line is a function that has multiple possible arguments.
	// This one is the main function where the drawing will change
	public void draw_a_Line(int flag, int length, int inDirection){
		// Here is where we go when ANY button is pressed.
		if(flag == 0){
			switch(inDirection){
				case 1: currentY -= length; // Go up (North)
						break;
				case 2: currentY += length; // Go down (South)
						break;
				case 3: currentX += length; // Go right (East)
						break;
				case 4: currentX -= length; // Go left (West)
						break;
				
			}
		} else {
			// Set the initial values if the button pressed is the first one
			if(inDirection != 0){
				initial = inDirection;
			}
			// Variable storage for the actual draw function
			this.length = length;
			direction = inDirection;
		}
	}
	
	// I dont believe this function is ever used..
	public void draw_a_Line(int length, int inDirection){
		if(inDirection != 0){
			initial = inDirection;
		}
		this.length = length;
		direction = inDirection;
	}
	
	// This is just a basic null case call for the lift and lower functions, i believe the reset button
	// also currently uses this function as a placeholder
	public void draw_a_Line(int inDirection){
		if(inDirection != 0){
			initial = inDirection;
		}
		direction = inDirection;
	}
	
	// This function simply stops the drawings from going outside the boundaries of the window, currently hardcoded
	public void clamp(int input){
		if(input < 0){
			if(currentX == input){
				System.out.println("Clamping X value, it was < 0.");
				currentX = 0;
			} else if(currentY == input){
				System.out.println("Clamping Y value, it was < 0.");
				currentY = 0;
			}
		} else if(input >= (475-length)){
			if(currentX == input){
				currentX = (475-length);
				System.out.println("Clamping X value, it was > the window size.");
			} else if(currentY == input){
				currentY = (475-length);
				System.out.println("Clamping Y value, it was > the window size.");
			}
		}
	}
	
	// This is a function where the lines will all add up, 'draws' is a queue datatype
	// This will take in the x, y, and length values for each drawing to be made
	// Line2D create a line by taking in x1, y1, x2, and y2 values
	// We take advantage of this by keeping our x and y as the current x and y, and adding the length value
	// to EITHER the addX value or addY value, this is determined in paintComponent 'else if' direction cases
	public void updateQueue(int x, int y, int addX, int addY){
		draws.add(new Line2D.Double(x,y,x+addX,y+addY));
	}
	
	// Call draw for the all items inside of the 'draws' queue
	public void drawAll(Graphics2D g2){
		for(Shape shape : draws){
			g2.draw(shape);
		}
	}
}
