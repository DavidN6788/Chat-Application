import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
	
public class ServerThread extends ChatBot implements Runnable {
	/*
	 * Client Socket on the client side.
	 */
	private Socket clientSock;
	
	/*
	 * Ability to send data to the clients
	 */
	private PrintWriter clientOut;
	
	/*
	 * Stores every client socket
	 */
	private ArrayList<ServerThread> clientSockets = new ArrayList<ServerThread>();
	
	/*
	 * Contains all the client IDs
	 */
	private HashMap<Integer, Integer> clientId = new HashMap<Integer, Integer>();
	
	/*
	 * Booleans to see wheter a client or bot entered the server
	 * Both are false since no clients or bots joins when the server runs
	 */
	private static boolean clientEntered;
	private static boolean botEntered;
	
	
	public ServerThread(Socket client, ArrayList<ServerThread> clientSockets, HashMap<Integer, Integer> clientId) throws IOException {
		super();
		this.clientSock = client;
		this.clientSockets = clientSockets;
		this.clientId = clientId;
		clientOut = new PrintWriter(clientSock.getOutputStream(), true); // Set up the ability to send the data to the client
	}
	
	
	/**
	 * Broadcasts messages received from one client to all available clients
	 * It is synchronized so two or more clients cannot read and write messages 
	 * at the same time
	 * 
	 * @param: Input from the clients
	 */
	synchronized private void broadcastAll(String userInput) {
		for(ServerThread aClient : clientSockets) {
			aClient.clientOut.printf("Client %d:" + userInput + "\n", getClientId(clientSock));
		}
	}

	
	/**
	 * Broadcast bot messages to all clients. The bot messages are outputted
	 * depending on certain user commands
	 * 
	 * @param userInput
	 */
	synchronized private void botBroadcast(String userInput) {
		for(ServerThread aClient : clientSockets) {
			aClient.clientOut.println("Bot: " + userInput);
		}
	}
	
	
	/**
	 * This method gets the ClientId (starting from 1) using hashmaps
	 *  
	 * @param: The client socket
	 * 
	 * @return: The ID of the client
	 */
	private Integer getClientId(Socket clientSock) {
		return clientId.get(clientSock.getPort());
	}
	
	
	/**
	 * Closes every client sockets
	 * 
	 * @throws IOException
	 */
	public void close() {
		try {
			clientSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Each client that joins gets a client handler
	 * This method allows the server to read data from each client
	 * and sends data back to all client
	 *
	 */
	private void clientHandler() {
		try {		
			// Setup the ability to read data from the client
			InputStreamReader clientCharStream = new InputStreamReader(clientSock.getInputStream());
			BufferedReader clientIn = new BufferedReader(clientCharStream);	
			while(true) {
				String userInput = clientIn.readLine(); // Reads from the clients and send the messages back
				checkClientType(userInput); // Checks client type i.e. chatclient or chatbot
				clientResponse(userInput);
				}
			} catch (IOException e) {
				disconnect();
			} finally { 
				try {
					clientSock.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Checks which client has disconnected and alters their truth values
	 * @throws IOException 
	 */
	private void disconnect() {
		if(botEntered == false) {
			System.out.printf("CLIENT %d HAS DISCONNECTED \n", getClientId(clientSock)); // Indicates which clients has disconnected
			clientEntered = false;
			try {
				clientSock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(botEntered == true && clientEntered == true) {
			System.out.println("BOT HAS DISCONNECTED");
			botEntered = false;
			clientEntered = true;
		}
	}
	
	
	
	/**
	 * Thie method responds the client or bot messages depending on the commands
	 * If there is not chatbot, the clients will not recieve the bot responses.
	 * @param userInput
	 */
	private void clientResponse(String userInput) {
		if(clientEntered == true && botEntered == false && !(userInput.equals("Client"))) {
			System.out.printf("[Client %d]:" + userInput + "\n" , getClientId(clientSock));
			broadcastAll(userInput);
		}if(clientEntered == true && botEntered == true && !(userInput.equals("Bot"))) {
			broadcastAll(userInput);
			if(userInput.equals("joke")) {
				botBroadcast(jokes.get(randomJokeGenerator()));
			}else if(userInput.equals("fact")) {
				botBroadcast(facts.get(randomFactGenerator()));
			}
		}
	}
	
	
	/**
	 * Checks whether a client or bot has entered. 
	 * If a client entered, the chat client will automatically send a "Client" msg
	 * If a bot entered, the chat bot will automatically a send a "Bot" msg
	 * 
	 * @param userInput
	 */
	private void checkClientType(String userInput) {
		if(userInput.equals("Client")) {
			clientEntered = true;     // Change value to indicate client entered
			System.out.printf("Client %d entered\n", getClientId(clientSock));
		}else if(userInput.equals("Bot")) {
			botEntered = true;  	//Change value to indicate bot entered
			System.out.println("ChatBot entered");
		}
	}
	
	
	@Override
	public void run() {
		clientHandler();
	}
}

