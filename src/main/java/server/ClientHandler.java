package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private String clientUserName;

    public ClientHandler(Socket socket, PrintWriter pw) {
        try {
            this.socket = socket;
            this.pw = pw;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = br.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, pw, br);
        }
    }

    @Override
    public void run() {
        this.protocol();
    }

    public void protocol(){
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = br.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void closeEverything(Socket socket, PrintWriter pw, BufferedReader br) {
        removeClientHandler();
        try {
            if(pw != null){
                pw.close();
            }
            if(br != null){
                br.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
