package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private PrintWriter pw;
    private Scanner scan;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.scan = new Scanner(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = scan.nextLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, pw, scan);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            messageFromClient = scan.nextLine();
            broadcastMessage(messageFromClient);
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.clientUserName.equals(clientUserName)) {
                clientHandler.pw.println(messageToSend);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : " + clientUserName + " has left the chat!");
    }

    private void closeEverything(Socket socket, PrintWriter pw, Scanner scan) {
        removeClientHandler();
        try {
            if(pw != null){
                pw.close();
            }
            if(scan != null){
                scan.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//made clienthandler
}