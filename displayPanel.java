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

public class displayPanel extends JPanel{
	Graphics g;
	int direction = 0;
	int initial = 0;
	int increment = 20;
	int currentX = 200;
	int currentY = 200;
	
	public displayPanel(){
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));
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
					g2.drawRect(currentX, currentY, 0, 10);
				} else {
					g2.drawRect(currentX, currentY, 10, 0);
				}
		} else if(direction == 1){
				System.out.println("Going North.");
				currentY -= 20;
				g2.drawRect(currentX, currentY, 0, 10);
				// increment += 5;
				
			} else if(direction == 2){
				System.out.println("Going South.");
				currentY += 20;
				g2.drawRect(currentX, currentY, 0, 10);
				// increment += 5;
				
			} else if(direction == 3){
				System.out.println("Going East.");
				currentX += 20;
				g2.drawRect(currentX, currentY, 10, 0);
				// increment += 5;
				
			} else if(direction == 4){
				System.out.println("Going West.");
				currentX -= 20;
				g2.drawRect(currentX, currentY, 10, 0);
				// increment += 5;
				
			}
		} else {
			g2.drawRect(currentX, currentY, 10, 0);
		}
		// Call the draw array here if so
	}
	
	public void draw_a_Line(int x, int y, int inDirection){
		if(inDirection != 0){
			initial = inDirection;
		}
		direction = inDirection;
	}
	
}