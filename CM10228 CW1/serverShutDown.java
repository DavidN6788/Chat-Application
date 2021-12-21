import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class serverShutDown implements Runnable {
	private ServerSocket mySocket;
	private String serverInput;
	private ArrayList<ServerThread> clientSockets = new ArrayList<ServerThread>();
	
	public serverShutDown(ServerSocket mySocket, ArrayList<ServerThread> clientSockets) {
		this.mySocket = mySocket;
		this.clientSockets = clientSockets;
	}
	
	
	/**
	 * Shuts down the server by closing the server socket 
	 * 
	 * @param: ServerSocket 
	 */
	private void cleanShutDown(ServerSocket mySocket, ArrayList<ServerThread> clientSockets) {
		System.out.println("Server is closing...");
		try {
			for(ServerThread clientSocket : clientSockets) {
				clientSocket.close();
			}
			mySocket.close();
			System.out.println("Server has shut down");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Reads input by the user(who acts as the server)
	 * from the keyboard.
	 * 
	 * It checks whether the user inputs "EXIT" so it can shut down
	 * the server willingly
	 *
	 */
	private void serverInput() {
		InputStreamReader inputStream = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(inputStream);
		
		while(true) {
			try {
				serverInput = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(serverInput.toLowerCase().equals("exit")) { 
				cleanShutDown(mySocket, clientSockets);
			}
		}
	}
	
	
	@Override
	public void run() {
		while(true) {
		serverInput();
		}
	}
}		



