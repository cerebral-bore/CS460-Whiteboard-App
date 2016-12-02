// Turtle Server for CS-460 Project 5
// author: Chance Nelson, Jesus Garcia

import java.io.*;
import java.net.*;


public class Server {
	// Initialize server variables
    private static int port = 5000;
    private static ServerSocket socket;

    
    public static void main(String[] args) throws Exception{
        // check args
        if(args.length == 0) {
            System.out.println("Turtle Graphics Server\nCorrect Usage: Server size port");
            System.exit(1);

        }        

        // init variables
        Map masterMap = new Map(Integer.parseInt(args[0]));            // set up a master copy of the map
        if(args.length == 2) port = Integer.parseInt(args[1]);

        // Initialize the sockets
        Server.socket = new ServerSocket(port);
		System.out.println("Server started successfully");
        Socket clientSock = null;

        while(true) {
			// Link the client to the server
            clientSock = socket.accept();

            if(clientSock != null){
				// Wait for the client to connect
                System.out.println("Client accepted");

            } else {
				System.out.println("Waiting for Connections");

            }

            // create a new thread to handle the newly accepted client
            (new Thread(new ThreadHelper(clientSock, masterMap))).start();

        }

    }

}
