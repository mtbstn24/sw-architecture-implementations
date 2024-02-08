package pubsub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	
	public Client(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		}catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	public void sendMessage() {
		try {
				
				Scanner scanner = new Scanner(System.in);
				while(socket.isConnected()) {
					String messageToSend = scanner.nextLine();
					bufferedWriter.write(messageToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
					
					
					if (messageToSend.equalsIgnoreCase("terminate")) {
		                closeEverything(socket, bufferedReader, bufferedWriter);
		                break;
		            }
					
					
					
				}
				
				
			
			
		}catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromGroupChat;
				
				while(socket.isConnected()) {
					try {
							msgFromGroupChat = bufferedReader.readLine();
							System.out.println(msgFromGroupChat);
						
					}catch (IOException e) {
						closeEverything(socket, bufferedReader, bufferedWriter);
					}
				}
			}
		}).start();
		
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if(socket != null) {
				socket.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		
        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String role = args[2].toUpperCase();
        
        StringBuilder topicsBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            topicsBuilder.append(args[i]).append(" ");
        }
        
		Socket socket = new Socket(serverIp, port);
		Client client = new Client(socket);

		client.bufferedWriter.write(role + " " + topicsBuilder);
		client.bufferedWriter.newLine();
		client.bufferedWriter.flush();
		client.listenForMessage();
		client.sendMessage();
		
	}


}


