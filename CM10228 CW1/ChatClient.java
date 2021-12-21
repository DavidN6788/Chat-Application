import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ChatClient{
	/*
	 * Client socket in the client side
	 */
	protected Socket socket;
	
	/*
	 * The default port i.e. 14001
	 */
	private int defaultPort;
	
	/*
	 * The default IPAddress i.e. localhost
	 */
	private String defaultIPAddress;
	
	/*
	 * Optional port from the command line
	 */
	private String optionalPort;
	
	/*
	 * Optional IPAddress from the command line
	 */
	private String optionalIPAddress;
	
	/*
	 * Set up the ability to send the data to the server
	 */
	protected PrintWriter serverOut;
	
	/*
	 * Holds data inputted from the keyboard
	 */
	protected String userInputString;
	
	/*
	 * Setup the ability to read from user
	 */
	protected BufferedReader userInput;
	
	
	public ChatClient() throws IOException {
		defaultPort = 14001;
		defaultIPAddress = "localhost";
		userInputString = null;
		userInput = new BufferedReader(new InputStreamReader(System.in)); // User can input data from the keyboard
	}

	
	/**
	 * This method carries out the basic functions of a client
	 * The client should:
	 * 	(1)Find servers using address and port
	 *	(2)Make requests on them
	 * 	(3)Receieve responses
	 * 	(4)Be able to display the response 
	 * 
	 * @param: IPAddress and the server port number in order to create client socket
	 * @throws IOException 
	 */
	protected void goChatClient(String IPAddress, int serverPortNumber) throws IOException {
		try {
			socket = new Socket(IPAddress, serverPortNumber);
			System.out.printf("Client has successfully connected to port number: %d \n\n", serverPortNumber);
			ClientRead serverConn = new ClientRead(socket);  // A thread that handles the reading ability of the client
			new Thread(serverConn).start();
			goClientWrite();
			userInput();
		}catch (IOException e) {
			validPort(serverPortNumber);
			}
		}
	
	
	/**
	 * Runs the chat client given optional port number or IPAddress.
	 * If no arguements are given, run default port and IP.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	protected void runChatClient(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.printf("Trying to connect with IP Address: %s ...\n", defaultIPAddress);
			System.out.printf("Trying to connect to port number: %d ... \n", defaultPort);
			goChatClient(defaultIPAddress, defaultPort);
		}else if(optionalPort == null) {
			System.out.printf("Tyring to connect with IP Address: %s... \n", optionalIPAddress);
			System.out.printf("Trying to connect to port number: %d... \n", defaultPort);
			goChatClient(optionalIPAddress, defaultPort);
		}else if(optionalIPAddress == null) {
			int optionalPortNumber = Integer.parseInt(optionalPort);
			System.out.printf("Trying to connect wit IP Address: %s...\n", defaultIPAddress);
			System.out.printf("Trying to connect to port number: %s... \n", optionalPort);
			goChatClient(defaultIPAddress, optionalPortNumber);
		}else {
			int optionalPortNum = Integer.parseInt(optionalPort);
			System.out.printf("Tyring to connect with IP Address: %s... \n", optionalIPAddress);
			System.out.printf("Trying to connect to port number: %s... \n", optionalPort);
			goChatClient(optionalIPAddress, optionalPortNum);
		}
	}
	
	
	/**
	 * Checks to see if -ccp and -cca is in the command line arguements
	 * @param args
	 * @throws IOException 
	 */
	protected void commandLineArg(String[] args) throws IOException {
		for(int i=0; i<args.length; i++) {
			if(args[i].equals("-ccp")) {
				try{
					optionalPort = args[i+1];
				}catch(NumberFormatException e) {
					validPort(Integer.parseInt(optionalPort));
				}
			}else if(args[i].equals("-cca")) {
				optionalIPAddress = args[i+1];
			}
		}
	}
	
	
	/**
	 * Enables the ability for the client to input another port number
	 * if the intial port number is incorrect
	 * 
	 * @param serverPortNumber
	 * @throws IOException
	 */
	protected void validPort(int serverPortNumber) throws IOException {
		System.out.println("The server you are trying to connect does not exist");
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.println("Please input a valid port: ");
			String validPort = userInput.readLine();
			int validPortNumber = Integer.parseInt(validPort);
			goChatClient("localhost", validPortNumber);
			break;
			}
	}
	
	
	/**
	 * This method allows the ability to write to the server
	 * It also sends the "Client" msg indicating that it is a client
	 * 
	 */
	protected void goClientWrite() {
		try {
			serverOut = new PrintWriter(socket.getOutputStream(), true); // Set up the ability to send the data to the server
			serverOut.println("Client");
			while(true) {
				serverOut.println(userInput());
		}
			}catch(IOException e) {
				try {
					socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Reads the user Input from keyboard 
	 * 
	 * @return the user input from the keyboard to be sent to the server
	 */
	protected String userInput(){
		try {
			userInputString = userInput.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userInputString;
	}
	
	
	public static void main(String[] args) throws IOException {
		ChatClient myChatClient = new ChatClient();
		myChatClient.commandLineArg(args);
		myChatClient.runChatClient(args);

	}
}
		
