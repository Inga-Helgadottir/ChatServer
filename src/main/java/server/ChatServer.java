package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatServer {
    private ServerSocket ss;
    public BlockingQueue<String> bq = new ArrayBlockingQueue<>(200);

    public ChatServer(ServerSocket ss){
        this.ss = ss;
    }

    //Call server with arguments like this: 8080
    public static void main(String[] args) throws IOException {
        int port = 8080;

        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }
            ServerSocket serverSocket = new ServerSocket(port);
            ChatServer server = new ChatServer(serverSocket);
            server.startServer();
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }
    }

    public void startServer(){
        while(!ss.isClosed()) {
            try {
                Dispatcher d = new Dispatcher(bq);
                d.start();
                while(true){
                    System.out.println("Waiting for client!");
                    Socket s = ss.accept();
                    System.out.println("A new client has connected!");

                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
                    ClientHandler ch = new ClientHandler(s, pw);
                    d.allWriters.add(pw);
                    ch.start();
                }
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
}
