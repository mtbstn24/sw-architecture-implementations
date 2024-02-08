package pubsub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientHandler implements Runnable{

	public static HashMap<String, ArrayList<ClientHandler>> topic_map = new HashMap<>();


	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientRole;
	private ArrayList<String> clientTopics = new ArrayList<>();

	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String text = bufferedReader.readLine();

			String[] words = text.split(" ");
			this.clientRole = words[0];	
			
			for (int i = 1; i < words.length; i++) {
				clientTopics.add(words[i]);
	        }
					
			for (String clientTopic : clientTopics) {
				if(topic_map.containsKey(clientTopic)) {
					ArrayList<ClientHandler> tempp = topic_map.get(clientTopic);
					tempp.add(this);
					topic_map.put(clientTopic, tempp);
	
				}else {
					ArrayList<ClientHandler> temp = new ArrayList<>();
					temp.add(this);
					topic_map.put(clientTopic, temp);
				}
			}
		}catch(IOException e){
			closeEverything(socket, bufferedReader, bufferedWriter);
			
		}
	}
	
	@Override
	public void run() {
		
		String messageFromClient;
		
		while(socket.isConnected()) {
			try {
					messageFromClient = bufferedReader.readLine();
					if(messageFromClient.equalsIgnoreCase("terminate")) {
						removeClientHandler();
					}
					else if(clientRole.equals("PUBLISHER")) {
						broadcastMessage(messageFromClient);
					}
			}catch(IOException e) {
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
		
	}
	
	
	public void broadcastMessage(String messageToSend) {
		for (String clientTopic : clientTopics) {
			ArrayList<ClientHandler> temp =  topic_map.get(clientTopic);
			for(ClientHandler clientHandler: temp) {
				
				try {
					if(clientHandler.clientRole.equals("SUBSCRIBER")) {					
								clientHandler.bufferedWriter.write(messageToSend);
								clientHandler.bufferedWriter.newLine();
								clientHandler.bufferedWriter.flush();					
					}
				} catch(IOException e) {
					closeEverything(socket, bufferedReader, bufferedWriter);
				}
			}
		}
	}
	
	
	public void removeClientHandler() {
		for (String clientTopic : clientTopics) {
			ArrayList<ClientHandler> temp2 = topic_map.get(clientTopic);
			temp2.remove(this);
			if(temp2.size() != 0) {
				topic_map.put(clientTopic, temp2);
			}else {
				topic_map.remove(clientTopic);
			}
		}
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		
		removeClientHandler();
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
	
}
