import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
public class Main {
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
    while((line = in.readLine()) != null && !line.isEmpty()) {
      strbuild.append(line).append("\r\n");
    }
    String request = strbuild.toString();
    System.out.println(request);
    if(request.startsWith("GET")) {
      String[] arr = request.split(" ");
      System.out.println(Arrays.toString(arr));
      String httpResponse;
      if(arr[1].startsWith("/echo")) {
        String[] str = arr[1].split("/");
        int length = str[2].length();
        System.out.println(Arrays.toString(str));
        httpResponse = "HTTP/1.1 200 OK \r\n" + //
                  "Content-Type: text/plain \r\n" + //
                  "Content-Length: "+length+"\r\n" + //
                  "\r\n" + str[2];//
      }
      else if(arr[1].startsWith("/user-agent")) {
        String str = arr[arr.length - 1];
        int length = str.length();
        httpResponse = "HTTP/1.1 200 OK \r\n" + //
                  "Content-Type: text/plain \r\n" + //
                  "Content-Length: "+length+"\r\n" + //
                  "\r\n" + str;//
      }
      else if(arr[1].startsWith("/files" )){
        String[] str = arr[1].split("/");
        System.out.println(str[2]);
        Path filePath = Paths.get( str[2] + ".txt");
        System.out.println(filePath);
        if(!Files.exists(filePath)) {
          httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
        }
        else {
          String content = Files.readString(filePath);
          int length = content.length();
          httpResponse = "HTTP/1.1 200 OK \r\n" + //
                  "Content-Type: application/octet-stream \r\n" + //
                  "Content-Length: "+length+"\r\n" + //
                  "\r\n" + content;//
        }
      }
      else if(arr[1].equals("/") ) {
        httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
      }
      else {
        httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
      }
      OutputStream out = clientSocket.getOutputStream();
      out.write(httpResponse.getBytes());
      out.flush();
    }
    else if(request.startsWith("POST")) {
      String[] arr = request.split(" ");
      System.out.println(Arrays.toString(arr));
      String httpResponse;
      if(arr[1].startsWith("/files")) {
        String[] str = arr[1].split("/");
        int length = Integer.parseInt(arr[arr.length - 1 ].trim());
        byte[] bodybytes = new byte[length];
        clientSocket.getInputStream().read(bodybytes);
        FileOutputStream fout = new FileOutputStream(str[2]);
        fout.write(bodybytes);
        fout.close();
      }
    }
  }
}
