package pubsub;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import pubsub.ClientHandler; 

public class Server {
	
	private ServerSocket serverSocket;

	
	public Server(ServerSocket serverSocket) {
		super();
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
		try {
			
			while(!serverSocket.isClosed()) {
				Socket socket =  serverSocket.accept();
				System.out.println("A new client has connected");
				ClientHandler clientHandler = new ClientHandler(socket);
				
				Thread thread = new Thread(clientHandler);
				thread.start();
				
			}
			
		}catch (IOException e) {
			e.printStackTrace();

		}		
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		 if (args.length != 1) {
	            System.err.println("Usage: java Server <port>");
	            System.exit(1);
	        }

	    int port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = new ServerSocket(port);
		Server server = new Server(serverSocket);
		server.startServer();
		
		
	}
	
	
}




