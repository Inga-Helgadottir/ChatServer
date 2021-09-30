package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatServer {
    private ServerSocket ss;
    public BlockingQueue<String> bq = new ArrayBlockingQueue<>(200);
    public ArrayList<ClientHandler> clientHandlers;

    public ChatServer(ServerSocket ss){
        this.ss = ss;
        this.clientHandlers = new ArrayList<>();
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

    public void startServer() throws IOException {

            Dispatcher d = new Dispatcher(bq);
            d.start();
            System.out.println("Waiting for client!");
            while(true){

                Socket s = ss.accept();
                System.out.println("A new client has connected!");

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
                d.addToWriterList(pw);
                Scanner sc = new Scanner(s.getInputStream());
                //TODO: husk validering af userinput
                String enterMsg = sc.nextLine();
//                System.out.println(enterMsg);
                String[] arr = enterMsg.split("#");
                bq.add("SERVER: " + arr[1] + " has entered the chat!");
//                pw.println("hello#" + arr[1]);
                ClientHandler ch = new ClientHandler(sc, pw, clientHandlers, arr[1], bq);
                //TODO: valider bruger datastruktur
//                d.addToWriterList(pw);
                clientHandlers.add(ch);

                ch.start();
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
