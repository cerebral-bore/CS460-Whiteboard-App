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
	
	public displayPanel(){
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));
		System.out.println("Test");
	}
	
	public void paintComponent(Graphics g){
		// Perhaps create an array that stores drawn objects to be repainted
		// Over and over again, very inefficient???
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.setColor(Color.RED);
		
		if(initial != 0){
			if(direction == 0){
				// Do nothing with make the current draw disappear
				if((initial == 1) || (initial == 2)){
					updateQueue(currentX, currentY, 0, length);
				} else {
					updateQueue(currentX, currentY, length, 0);
				}
		} else if(direction == 1){
				System.out.println("Going North.");
				currentY -= length;
				clamp(currentY);
				updateQueue(currentX, currentY, 0, length);
				// increment += 5;
				
			} else if(direction == 2){
				System.out.println("Going South.");
				currentY += length;
				clamp(currentY);
				updateQueue(currentX, currentY, 0, length);
				// increment += 5;
				
			} else if(direction == 3){
				System.out.println("Going East.");
				currentX += length;
				clamp(currentX);
				updateQueue(currentX, currentY, length, 0);
				// increment += 5;
				
			} else if(direction == 4){
				System.out.println("Going West.");
				currentX -= length;
				clamp(currentX);
				updateQueue(currentX, currentY, length, 0);
				// increment += 5;
				
			}
		} else {
			updateQueue(currentX, currentY, 1, 1);
		}
		// Call the draw array here if so
		drawAll(g2);
	}
	
	public void draw_a_Line(int flag, int length, int inDirection){
		if(flag == 0){
			switch(inDirection){
				case 1: currentY -= length;
						break;
				case 2: currentY += length;
						break;
				case 3: currentX += length;
						break;
				case 4: currentX -= length;
						break;
				
			}
		} else {
			if(inDirection != 0){
				initial = inDirection;
			}
			this.length = length;
			direction = inDirection;
		}
	}
	
	public void draw_a_Line(int length, int inDirection){
		if(inDirection != 0){
			initial = inDirection;
		}
		this.length = length;
		direction = inDirection;
	}
	
	public void draw_a_Line(int inDirection){
		if(inDirection != 0){
			initial = inDirection;
		}
		direction = inDirection;
	}
	
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
	
	public void updateQueue(int a, int b, int c, int d){
		draws.add(new Line2D.Double(a,b,a+c,b+d));
	}
	
	public void drawAll(Graphics2D g2){
		for(Shape shape : draws){
			g2.draw(shape);
		}
	}
}