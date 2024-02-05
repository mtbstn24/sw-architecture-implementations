import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException{
        if (args.length < 1){
            System.err.println("No port number specified");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Server started on port " + portNumber);

        try (ServerSocket serverSocket = new ServerSocket(portNumber);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             ){
            System.out.println("Client at " + clientSocket.toString() + " connected to Server on port " + portNumber);
            String clientInputLine;
            while ((clientInputLine = in.readLine()) != null){
                System.out.println("Received message from Client at " + clientSocket.toString() + ": " + clientInputLine);
                out.println(clientInputLine);
            }
        }catch (IOException e){
            System.err.println(e.toString());
        }
    }
}
