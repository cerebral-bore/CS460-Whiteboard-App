# CS460-Whiteboard-App

Link to youtube Video: https://www.youtube.com/watch?v=Z6XXEhwY3co&feature=youtu.be

Current Status:
	
	+ Fully functional!!
	
	- Except for a couple of drawing bugs..

" 	The application to be developed has two parts: a server, that represents a distributed "white board" and a client application, that sends commands to the server.

The server interprets and executes the commands, which results in simple lines being drawn on the white board.

At any time there may be several clients talking to the server and thus drawing on the white board.

The white board behaves like a turtle graphics application.

	- This means that there is a single pen, which may be lifted (and thus does not draw) or put down (when it is supposed to draw).
	- The pen can be moved in any one direction of north/south/east/west.
	- When it is moved, it will do so in a pre-configured (but not hard-coded) increment in terms of length.
	* This may or may not result in a line being drawn, depending on whether the pen is lifted (up) or not (down). *
	
The client has a simple GUI with these input elements:

	- Server address/port
	- Buttons that represent the geographic directions N/S/E/W.
	*If any one of these buttons is hit the pen is moved in that direction hit, at a length that is configurable.*
	- Buttons UP/DOWN for lifting the pen or putting it down
	- Field to input the length of the distance to be moved upon hitting one of N/S/E/W "
