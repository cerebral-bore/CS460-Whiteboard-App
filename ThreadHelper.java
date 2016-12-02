// client thread helper for turtle server, CS-460 project 5
// author: Chance Nelson


import java.lang.*;
import java.io.*;
import java.net.Socket;


public class ThreadHelper implements Runnable{
    // init some local variables
    protected Socket clientSocket = null;                              // client socket
    private Map map;                                                 // reference to master copy of map
    private boolean[][] localMapCopy;                                  // local copy of master map

    
    // constructor
    // args:
    //      clientSocket: socket reference for the client
    //      map: reference to the master copy of the map for syncronization purposes
    public ThreadHelper(Socket clientSocket, Map map) {
        this.clientSocket = clientSocket;
        this.map = map;
        localMapCopy = new boolean[map.get_size()][map.get_size()];

    }
    
    // synchronizes the local copy of the map with the master copy; sends out packets to the client for every chenge found
    // args:
    //      out: PrintWriter referece for client communication
    private void check_for_changes(PrintWriter out) {
        // loop through every array index
        for(int i = 0; i < map.get_size(); i++) {
            for(int j = 0; j < map.get_size(); j++) {
                // check for discrepencies. If one is found, change the local copy and send a packet
                if(localMapCopy[i][j] != map.get_map()[i][j]) {
                    out.println(i + " " + j);
                    System.out.println(i + " " + j);
                    localMapCopy[i][j] = !localMapCopy[i][j];
                    
                }

            }
            
        }

    }
    
    
    // Handles the client
    public void run() {
        try {
            
            // set up my input and output streams
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // client-server interaction loop
            while(true) {
                check_for_changes(out);                                 // sync up the client with the server
                
                String[] input = in.readLine().split(" ");                     // concatinate the string received
                // close the thread and socket if the quit signal is received
                if(input[0].contains("quit")) {      
                    System.out.println("Client closing connection");
                    clientSocket.close();
                    return;
                }

                // grab the x and y coordinates sent in the packet
                int x = Integer.parseInt(input[0]);
                int y = Integer.parseInt(input[1]);
                System.out.println("received " + x + " " + y);
                // flip this coordinate in the master copy
                map.flip(x, y);
                
            }
            
        }catch (Exception e) {
                System.out.println("Something borked. Whoopsiedasie");

        }
        
    }
    
}
