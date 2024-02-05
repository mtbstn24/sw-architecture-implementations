import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2){
            System.err.println("ClientApp Usage : java Main <hostName or IP> <portNumber>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (Socket serverSocket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
             ){
            String clientInput;
            while ((clientInput = stdIn.readLine()) != null) {
                if(clientInput.equals("terminate")){
                    break;
                }
                out.println(clientInput);
                System.out.println("Server : " + in.readLine());
            }
        }catch (UnknownHostException e){
            System.err.println(e.toString());
            System.exit(1);
        }catch (IOException e){
            System.err.println("Couldn't get I/O connection to host : " + e.toString());
            System.exit(1);
        }
    }
}