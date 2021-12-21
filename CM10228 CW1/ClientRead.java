import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
 *This thread enables the ability for client to receive
 * messages from other clients before they start typing
 * on the keyboard.
 */
public class ClientRead implements Runnable {
	private Socket server;
	private BufferedReader ServerIn;
	
	
	public ClientRead(Socket s) throws IOException {
		this.server = s;
		ServerIn  = new BufferedReader(new InputStreamReader(server.getInputStream())); // Setup the ability to read data from the server
	}
	
	
	/**
	 * This method reads data from the server and
	 * responds it to the clients
	 * 
	 */
	private void serverHandle() {
		try {
			while (true) {
				String serverResponse = ServerIn.readLine(); 
				if(serverResponse == null) {
					System.out.println("Server has shut down");
					server.close();
					ServerIn.close();
					System.exit(0);
					break;
				}else {
					System.out.println(serverResponse);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ServerIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void run() {
		serverHandle();
	}
}
