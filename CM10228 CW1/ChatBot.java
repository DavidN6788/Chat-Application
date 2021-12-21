import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class ChatBot extends ChatClient{
	
	/*
	 * The jokes will be stored in an ArrayList
	 */
	protected ArrayList<String> jokes = new ArrayList<String>();
	
	/*
	 * The facts will be stored in an ArrayList
	 */
	protected ArrayList<String> facts = new ArrayList<String>();
	
	/*
	 * A random generator for the jokes and facts
	 */
	private Random random;

	
	public ChatBot() throws IOException{	
		super();
		random = new Random();
		jokes.addAll(Arrays.asList("I made a pencil with 2 erasers today... it was pointless.",
				
		"What did the baby corn say to the mamma corn?... Where's popcorn",
								   
		"I asked an electrician to fix an issue at my house... he refused",
								   
		"My Doctor says that when you die, your pupils are the last thing to go... because they dilate",
								   
		"If you spell the words 'Absolutely nothing' backwards, you get 'Gnihton yletulosba, which means.. Absolutely nothing",
								   
		"Does every sentence need to include a vegetable... not neccescelery",
								   
		"I once swallowed a book of synonyms... it gave me thesaurus throat"));
								   
								   
		facts.addAll(Arrays.asList("The scientific term for brain freeze is 'sphenopalatine ganglioneuralgia'.",
				
		"Canadians say 'sorry' so much that a law was passed in 2009 declaring that an apology can't be used as evidence of admission to guilt.",
								   
		"The only letter that doesn't appear on the periodic table is J.",
								   
		"If you cut down a cactus in Arizona, you'll be penalized up to 25 years in jail.",
								   
		"Iceland does not have a railway system.",
								  
		"The tongue is the only muscle in one's body that is attached from one end.",
								   
		"Violin bows are commonly made from horse hair.",
								  
		"Until 2016, the 'Happy Birthday' song was not for public use. Meaning, prior to 2016, the song was copyrighted and you had to pay a license to use it."));
	}
	
	
	/**
	 * Randomly generates an index in the jokes array list
	 * @return index of the joke array listt
	 */
	protected int randomJokeGenerator() {
		int randomIntIndex = 0;
		randomIntIndex = random.nextInt(jokes.size());
		return randomIntIndex;
	}
	
	
	/**
	 * Randomly generates an index in the facts array list
	 * @return index of the fact array list
	 */
	protected int randomFactGenerator() {
		int randomFactIndex = 0;
		randomFactIndex = random.nextInt(facts.size());
		return randomFactIndex;
	}
	
	
	/**
	 * This method allows the ability to write to the server
	 * It also sends the "Bot" msg indicating that it is a client
	 * 
	 */
	@Override
	protected void goClientWrite() {
		try {
			serverOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Set up the ability to send the data to the server
		serverOut.println("Bot");
	}
	
	
	/**
	 * Removed the ability for the bot to read client messages.
	 * The clientRead thread is therefore removed
	 * @throws IOException 
	 */
	@Override
	protected void goChatClient(String IPAddress, int serverPortNumber) throws IOException {
		try {
			socket = new Socket(IPAddress, serverPortNumber);
			System.out.printf("Client has successfully connected to port number: %d \n\n", serverPortNumber);
			goClientWrite();
			userInput();
		} catch (IOException e) {
			validPort(serverPortNumber);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		ChatBot myChatBot = new ChatBot();
		myChatBot.commandLineArg(args);
		myChatBot.runChatClient(args);
	}
}

