package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static Socket socket;
    private static PrintWriter pw;
    private static BufferedReader br;
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

    public static void whatToDo() throws IOException {
        String msg = "";
        String dataString = "";
        String dataString2 = "";
        while(!msg.equals("CLOSE#")) {
            msg = br.readLine();
            String[] msgArr = msg.split("#");
            String action = msgArr[0].toUpperCase();
            if (msgArr.length > 1) {
                dataString = msgArr[1];
            }
            if(msgArr.length > 2){
                dataString2 = msgArr[2];
            }
            switch (action) {
                case "CONNECT":
                    Client.connectClient(dataString);
                    break;
                case "SEND":
                    if(dataString.contains("*")){
                        //send message to all
                    }else{
                        //send message to given user/users
                    }
                    break;
                default:
                    msg = "CLOSE#";
            }
        }
        socket.close();
        pw.println(msg + " hej fra server");
    }

    private void sendMsg(String username, String msg) {

        try {
            pw.println(username);

            while(socket.isConnected()){
                pw.println(username + ": " + msg);
            }
        } catch (IOException e) {
            closeEverything(socket, pw, br);
        }
    }
}
