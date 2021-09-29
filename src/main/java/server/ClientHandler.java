package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread {

    public static ArrayList<ClientHandler> clientHandlers;
    private static PrintWriter pw;
    private Scanner sc;

    public ClientHandler(Scanner sc, PrintWriter pw, ArrayList<ClientHandler> clientHandlers) {
        this.sc = sc;
        this.pw = pw;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        this.protocol();
    }

    public void protocol(){
        String messageFromClient;

        while (true){
            messageFromClient = sc.nextLine();
            broadcastMessage(messageFromClient);
        }
    }

    public void broadcastMessage(String messageToSend){
//        for (ClientHandler clientHandler : clientHandlers) {
//            if (!clientHandler.clientUserName.equals(clientUserName)) {
//                clientHandler.pw.println(messageToSend);
//            }
//        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : " + this + " has left the chat!");
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

    public void whatToDo() throws IOException {
        String msg = "";
        String dataString = "";
        String dataString2 = "";
        while(!msg.equals("CLOSE#")) {
            msg = sc.nextLine();
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
        pw.println(msg + " hej fra server");
    }
}
