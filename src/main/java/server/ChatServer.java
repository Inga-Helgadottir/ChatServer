package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatServer {
    private ServerSocket ss;
    public ChatServer(ServerSocket ss){
        this.ss = ss;
    }

    public void startServer(){
        while(!ss.isClosed()) {
            try {
                Socket s = ss.accept();
                System.out.println("A new client has connected!");
                ClientHandler ch = new ClientHandler(s);

                Thread t = new Thread(ch);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServerSocket(){
        try {
            if(ss != null){
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Call server with arguments like this: 8088
    public static void main(String[] args) throws IOException {
        int port = 8088;
        
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }
            else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

        ServerSocket serverSocket = new ServerSocket(port);
        ChatServer server = new ChatServer(serverSocket);
        server.startServer();
    }
}
