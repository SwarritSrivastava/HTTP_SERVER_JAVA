import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class secondary {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    try {
      ServerSocket serverSocket = new ServerSocket(4221);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
        while(true) {
            Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            new Thread(() -> {
              try {
                  Response(clientSocket);
              } catch (IOException e) {
                System.err.println("Error handling client : " + e.getMessage()); 
              }
              finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                  System.err.println("Error CLosing Client" + e.getMessage());
                }
              }
            }).start();
        }
    } 
    catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
      
  }
  public static void Response(Socket clientSocket) throws IOException {
    //InputStreamReader in = new InputStreamReader(clientSocket.getInputStream()); couldve used this but will require to read char by char instead we use buffer reader
    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
    StringBuilder strbuild = new StringBuilder();
    String line;
    while((line = in.readLine()) != null && !line.isEmpty()) 
    {
      line = in.readLine();
      strbuild.append(line).append("\r\n");
    }
    String request = strbuild.toString();
    System.out.println(request);

    char[] body = new char[5];
        if (5 > 0) {
            in.read(body, 0, 5);
        }
    }

}


