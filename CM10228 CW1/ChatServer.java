import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatServer {	
	/*
	 * An array list that holds client sockets
	 */
	private ArrayList<ServerThread> clientSockets = new ArrayList<ServerThread>();
	
	/*
	 * A hash map containing client port number with corresponding client ID
	 */
	private HashMap<Integer, Integer> clientId = new HashMap<Integer, Integer>(); 
	
	/*
	 * Stores the number of clients that joins
	 */
	private int clientCount = 0;
	
	/*
	 * The defualtPort being 14001
	 */
	private int defaultPort = 14001;
	
	/*
	 * Server Socket created on the server side
	 */
	private ServerSocket mySocket;
	
	/**
	 * This method carries out the basic function of a server
	 * The server should:
	 * 	(1)Run first
	 * 	(2)Accept new connections
	 * 	(3)Accept requests
	 * 	(4)React to those requests
	 * 	(5)Provide a response
	 * 
	 * In addition:
	 *  This server will add each client to an arrayList
	 *  Assigns a unique client ID to every client
	 * 
	 * @throws IOException 
	 */
	private void goChatServer() throws IOException {
		try {
			while(true) {
				Socket client = mySocket.accept(); //Accepts new connections
				ServerThread clientSocket = new ServerThread(client, clientSockets, clientId);
				new Thread(clientSocket).start(); // Create a client handler thread each time a connection is made
				assignClientId(clientSocket, client);
				}
		} catch(IOException e) {
			mySocket.close();
			
			} 	
		}

	
	/**
	 * This method is the assignment of clients. 
	 * It adds client sockets to an array list
	 * Assigns a uniquie client ID to each client
	 * 
	 * @param clientSocket
	 * @param client
	 */
	private void assignClientId(ServerThread clientSocket, Socket client) {
		clientCount += 1;
		clientSockets.add(clientSocket); // Add client sockets to an array list
		clientId.put(client.getPort(), clientCount); // Assign unique client ID to each client
	}
	
	
	/**
	 * Creates a server socket given a port number
	 * 
	 * @param: the arguements in the command line
	 * 
	 */
	private void createSocket(int serverPort) throws IOException {
		mySocket = new ServerSocket(serverPort);	
	}
	
	
	/*
	 * Runs a thread which carries out the server user input
	 */
	private void serverUserInput() {
		serverShutDown serverShut = new serverShutDown(mySocket, clientSockets); 
		new Thread(serverShut).start();
		System.out.println("Server is listening...");	
	}
	
	
	/**
	 * Creates the server by calling createServer(), serverUserInput(), goChatServer()
	 * 
	 * @param: Server port number either default or optional
	
	 * @throws IOException 
	 */
	private void createServer(int portNumber) throws IOException {
		System.out.printf("The server is running on port number: %d \n", portNumber);
		createSocket(portNumber);
		serverUserInput();
		goChatServer();
	}
	
	
	/**
	 * Checks to see if an optional parameter is in the command line
	 * Run default port if command line is empty
	 * @param args
	 * @throws IOException
	 */
	private void runChatServer(String[] args) throws IOException {
		if(args.length == 0) {
			createServer(defaultPort);
		}else {
			for(int i=0; i<args.length; i++) {
				if(args[i].equals("-csp")) {
					String myString = args[i+1];
					try {
						int serverPort = Integer.parseInt(myString);
						validPort(serverPort);
					}catch(NumberFormatException e) {
						BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // User can input data from the keyboard
						System.out.println("Please enter a valid port: ");
						String validPort = userInput.readLine();
						int portNumber = Integer.parseInt(validPort);	
						validPort(portNumber);
					}
				}
			}
		}
	}
	
	
	/**
	 * This method checks if the port number in the command line is valid
	 * It is valid when it is greater than 14001 but less the 60000(arbritrary)
	 * 
	 * @param serverPort
	 * @throws IOException
	 */
	private void validPort(int serverPort) throws IOException {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // User can input data from the keyboard
		while(true){
				if(serverPort >= 14001 && serverPort <= 60000) {
					createServer(serverPort);
					userInput.close();
					break;
				}else {
					System.out.println("Please input a valid port: ");
					String validPort = userInput.readLine();
					int portNumber = Integer.parseInt(validPort);	
					validPort(portNumber); // Keeps recursing until a valid input is inputted
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		ChatServer myChatServer = new ChatServer();
		myChatServer.runChatServer(args);
	}
}
		

		
	
